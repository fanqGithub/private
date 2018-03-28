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

import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseActivity;
import com.commai.commaplayer.service.MusicPlayer;
import com.commai.commaplayer.service.OnPlayerEventListener;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.widget.CmVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author:范启 Created on 2018/3/25.
 * Description:
 */

public class CmVideoViewActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    @BindView(R.id.cm_video_player)
    CmVideoView videoView;

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
            mediaPath=getIntent().getStringExtra("mediaPath");
            videoTitle=getIntent().getStringExtra("videoTitle");
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
                videoView.start();
                ivPlayPause.setSelected(true);
                handler.post(mUpdateRunnable);
                //设置视频信息
                videoSeek.setMax(mp.getDuration());
                totalVideoTime=getUpdateTimeInfo(mp.getDuration());
                tvCurrentTimeAndTotalTime.setText("00:00:00/"+totalVideoTime);
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
                Toast.makeText(CmVideoViewActivity.this,"播放完了",Toast.LENGTH_SHORT).show();
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