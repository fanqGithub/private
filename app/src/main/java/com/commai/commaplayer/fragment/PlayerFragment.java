package com.commai.commaplayer.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 遗弃
 */
@Deprecated
public class PlayerFragment extends Fragment implements View.OnClickListener{

    public static MediaPlayer mMediaPlayer;

    public static boolean isReplayIconVisible = false;

    public static boolean isPrepared = false;

    private TextView currTime, totalTime;

    //中间的圆图
    public ImageView playing_music_img;
    //播放标题
    public TextView playging_music_title;

    public ImageView mainTrackController;
    public ImageView nextTrackController;
    public ImageView previousTrackController;

    boolean isFav = false;

    public RelativeLayout bottomContainer;
    public RelativeLayout seekBarContainer;
    public RelativeLayout toggleContainer;

    //缩小的播放器部分
    public ImageView small_playing_music_img;
    public TextView small_music_title;
    public TextView small_artist;
    public ImageView small_player_controller,small_player_next;

    public RelativeLayout smallPlayer;

    ImageView favControllerSp, nextControllerSp;

    public SeekBar progressBar;

    public static int durationInMilliSec;
    public static boolean completed = false;
    public boolean pauseClicked = false;
    boolean isTracking = false;

    public static boolean localIsPlaying = false;

    static boolean isRefreshed = false;

    private ImageLoader imageLoader;

    private Pair<String,String> temp;

    public Timer timer;

    private ImageView img_back;

    public AudioItem currentMusic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_music, container, false);
        playing_music_img=view.findViewById(R.id.playing_music_img);
        progressBar = (SeekBar) view.findViewById(R.id.progressBar);
        currTime = (TextView) view.findViewById(R.id.currTime);
        totalTime=view.findViewById(R.id.totalTime);
        playging_music_title=view.findViewById(R.id.music_title);
        playging_music_title.setSelected(true);
        mainTrackController=view.findViewById(R.id.controller);
        nextTrackController = (ImageView) view.findViewById(R.id.next);
        previousTrackController = (ImageView) view.findViewById(R.id.previous);
        img_back=view.findViewById(R.id.img_back);

        //小播放器
        smallPlayer = (RelativeLayout) view.findViewById(R.id.smallPlayer);
        small_playing_music_img=view.findViewById(R.id.selected_track_image_sp);
        small_music_title=view.findViewById(R.id.selected_track_title_sp);
        small_artist=view.findViewById(R.id.selected_track_artist_sp);
        small_player_controller=view.findViewById(R.id.player_control_sp);
        small_player_next=view.findViewById(R.id.player_control_sp);


        mainTrackController.setOnClickListener(this);
        img_back.setOnClickListener(this);
        imageLoader=new ImageLoader(getContext());
        Log.d("Current",MainActivity.currentPlayingMusic.getPath());
        initMediaPlayer();

        smallPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediaPlayer = new MediaPlayer();
    }

    //初始化播放器
    private void initMediaPlayer(){
        currentMusic=MainActivity.currentPlayingMusic;
        durationInMilliSec = MainActivity.currentPlayingMusic.getDuration();
        progressBar.setMax(durationInMilliSec);
        temp = MediaUtil.getTime(durationInMilliSec);
        totalTime.setText(temp.first + ":" + temp.second);
        playging_music_title.setText(MainActivity.currentPlayingMusic.getTitle());
        imageLoader.DisplayImage(MainActivity.currentPlayingMusic.getPath(),small_playing_music_img);
        small_music_title.setText(MainActivity.currentPlayingMusic.getTitle());
        small_artist.setText(MainActivity.currentPlayingMusic.getArtist());
        imageLoader.DisplayImage(MainActivity.currentPlayingMusic.getPath(),playing_music_img);
        try {
            mMediaPlayer.setDataSource(MainActivity.currentPlayingMusic.getPath());
//            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
//                        if (isPrepared && !isTracking && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    float[] hsv = new float[3];
                                    hsv[0] = (float) 0.5;
                                    hsv[1] = (float) 0.8;
                                    hsv[2] = (float) 0.5;
                                    progressBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN));
                                }
                            });
                            try {
                                temp = MediaUtil.getTime(mMediaPlayer.getCurrentPosition());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currTime.setText(temp.first + ":" + temp.second);
                                    }
                                });
                                progressBar.setProgress(mMediaPlayer.getCurrentPosition());
                            } catch (Exception e) {
                                Log.e("MEDIA", e.getMessage() + ":");
                            }
                        }
//                    }
                }, 0, 50);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp = MediaUtil.getTime(progress);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currTime.setText(temp.first + ":" + temp.second);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                isTracking = false;
            }

        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                completed=true;
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                double ratio = percent / 100.0;
                double bufferingLevel = (int) (mp.getDuration() * ratio);
                if (progressBar != null) {
                    progressBar.setSecondaryProgress((int) bufferingLevel);
                }
            }
        });

        mMediaPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return true;
                    }
                }
        );
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        isPrepared = false;
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        isPrepared = true;
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.controller:
                break;
            case R.id.img_back:

                break;
            default:
                break;
        }
    }

}
