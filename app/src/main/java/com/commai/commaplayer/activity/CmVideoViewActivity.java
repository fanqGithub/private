package com.commai.commaplayer.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.commai.commaplayer.Entity.AllPlayLists;
import com.commai.commaplayer.Entity.SelectedMediaItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseActivity;
import com.commai.commaplayer.service.MusicPlayer;
import com.commai.commaplayer.service.OnPlayerEventListener;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.widget.CmVideoView;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author:范启 Created on 2018/3/25.
 * Description:
 */

public class CmVideoViewActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    @BindView(R.id.cm_video_player)
    VideoView videoView;

    @BindView(R.id.seek_video)
    SeekBar videoSeek;

    @BindView(R.id.video_play_pause)
    ImageView ivPlayPause;

    @BindView(R.id.video_previous)
    ImageView ivPrevious;

    @BindView(R.id.video_next)
    ImageView ivNext;

    @BindView(R.id.video_current_total_time)
    TextView tvCurrentTimeAndTotalTime;

    private String mediaPath="";

    private static final long TIME_UPDATE = 300L;

    private Handler handler;

    private boolean isDraggingProgress=false;

    @BindView(R.id.bottomControl)
    FrameLayout bottomControl;

    @BindView(R.id.topControl)
    FrameLayout topControl;

    @BindView(R.id.playLayout)
    RelativeLayout playLayout;

    @BindView(R.id.video_title)
    TextView tvVideoTitle;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.more_vert)
    ImageView ivMorevert;

    private boolean controlShowing=true;

    private Handler showCotrollHandler;

    private String totalVideoTime="";

    private String videoTitle="";

    //用于播放列表文件
    private boolean isPlayList=false;
    private Gson gson;
    private AllPlayLists playLists;
    private String listJson=null;
    private int currentIndex=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window=this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        setContentView(R.layout.activity_cm_videoview_player);
        ButterKnife.bind(this);
        initViewData();
    }

    private void initViewData(){
        gson=new Gson();
        showCotrollHandler=new Handler();
        showCotrollHandler.postDelayed(mShowControllRunnable,5000);

        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        //更新进度
                        if (videoView.isPlaying()) {
                            videoSeek.setProgress(videoView.getCurrentPosition());
                            String currentTime=getUpdateTimeInfo(videoView.getCurrentPosition());
                            tvCurrentTimeAndTotalTime.setText(currentTime+"/"+totalVideoTime);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivPlayPause.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        videoView.setOnTouchListener(this);
        playLayout.setOnTouchListener(this);

        if (getIntent().getExtras()!=null){
            isPlayList=getIntent().getBooleanExtra("isPlayList",false);
            if (isPlayList){
                listJson=getIntent().getExtras().getString("listPlayJson");
                playLists=gson.fromJson(listJson,AllPlayLists.class);
                //先播放列表中的第一个媒体文件
                mediaPath=playLists.getSelectedList().get(0).getMediaPath();
                videoTitle=playLists.getSelectedList().get(0).getMediaName();
            }else {
                mediaPath = getIntent().getStringExtra("mediaPath");
                videoTitle = getIntent().getStringExtra("videoTitle");
            }
        }

        if (!TextUtils.isEmpty(mediaPath)) {
            videoView.setVideoPath(mediaPath);
        }
        tvVideoTitle.setText(videoTitle);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                preparedAndStartPlay(mp.getDuration());
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isPlayList) {
                    if (currentIndex<playLists.getSelectedList().size()-1){
                        Toast.makeText(CmVideoViewActivity.this, "为您播放下一个媒体文件", Toast.LENGTH_SHORT).show();
                        currentIndex=++currentIndex;
                        String path=playLists.getSelectedList().get(currentIndex).getMediaPath();
                        videoView.setVideoPath(path);
                        tvVideoTitle.setText(playLists.getSelectedList().get(currentIndex).getMediaName());
                        preparedAndStartPlay(playLists.getSelectedList().get(currentIndex).getDuration());
                    }else {
                        Toast.makeText(CmVideoViewActivity.this, "列表已播放完毕", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CmVideoViewActivity.this, "播放完毕", Toast.LENGTH_SHORT).show();
                }
                ivPlayPause.setSelected(false);
                handler.removeCallbacks(mUpdateRunnable);
            }
        });

        videoSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDraggingProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isDraggingProgress = false;
                if (videoView.isPlaying()) {
                    int progress = seekBar.getProgress();
                    videoView.seekTo(progress);
                }
            }
        });

    }

    private void preparedAndStartPlay(int duration) {
        videoView.start();
        ivPlayPause.setSelected(true);
        handler.post(mUpdateRunnable);
        //设置视频信息
        videoSeek.setMax(duration);
        totalVideoTime=getUpdateTimeInfo(duration);
        tvCurrentTimeAndTotalTime.setText("00:00:00/"+totalVideoTime);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_play_pause:
                if (videoView.isPlaying()){
                    videoView.pause();
                    ivPlayPause.setSelected(false);
                }else {
                    startPlayVideo();
                }
                break;
            case R.id.video_previous:
                break;
            case R.id.video_next:
                break;
            case R.id.iv_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    private void startPlayVideo(){
        videoView.start();
        handler.post(mUpdateRunnable);
        ivPlayPause.setSelected(true);
    }

    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            handler.post(this);
            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId()==R.id.cm_video_player||v.getId()==R.id.playLayout){
            if (MotionEvent.ACTION_DOWN==event.getAction()) {
                if (controlShowing) {
                    bottomControl.setVisibility(View.GONE);
                    topControl.setVisibility(View.GONE);
                    controlShowing = false;
                    if (showCotrollHandler!=null){
                        showCotrollHandler.removeCallbacks(mShowControllRunnable);
                    }
                } else {
                    bottomControl.setVisibility(View.VISIBLE);
                    topControl.setVisibility(View.VISIBLE);
                    controlShowing = true;
                    showCotrollHandler=new Handler();
                    showCotrollHandler.postDelayed(mShowControllRunnable,5000);
                }
            }
        }
        return true;
    }

    private Runnable mShowControllRunnable=new Runnable() {
        @Override
        public void run() {
            bottomControl.setVisibility(View.GONE);
            topControl.setVisibility(View.GONE);
            controlShowing = false;
        }
    };

    private String getUpdateTimeInfo(int millseconds){
        String timeStr="";
        int second=millseconds/1000;
        int hh=second/3600;
        int mm=second%3600/60;
        int ss=second%60;
        if (hh!=0){
            timeStr=String.format("%02d:%02d:%02d",hh,mm,ss);
        }else {
            timeStr=String.format("%02d:%02d",mm,ss);
        }
        return timeStr;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacks(mUpdateRunnable);
        }
        if (showCotrollHandler!=null){
           showCotrollHandler.removeCallbacks(mShowControllRunnable);
        }
    }
}
