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
import com.commai.commaplayer.service.AudioPlayerBroadcastReceiver;
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
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements AudioPlayerBroadcastReceiver.onCallbackListener,View.OnClickListener{

    public static MediaPlayer mMediaPlayer;
    public static Visualizer mVisualizer;
    public static Equalizer mEqualizer;
    public static BassBoost bassBoost;
    public static PresetReverb presetReverb;

    public static boolean isReplayIconVisible = false;

    public static boolean isPrepared = false;

    TextView currTime, totalTime;

    //中间的圆图
    public ImageView playing_music_img;
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
    public ImageView small_player_controller;

    public RelativeLayout smallPlayer;

    ImageView favControllerSp, nextControllerSp;

    public SeekBar progressBar;

    public static int durationInMilliSec;
    public static boolean completed = false;
    public boolean pauseClicked = false;
    boolean isTracking = false;

    public static boolean localIsPlaying = false;

    static boolean isRefreshed = false;

    public PlayerFragmentCallbackListener mCallback;
    public onPlayPauseListener mCallback7;

    private ImageLoader imageLoader;

    private Pair<String,String> temp;

    public Timer timer;

    private ImageView img_back;

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
        img_back=view.findViewById(R.id.img_back);

        smallPlayer = (RelativeLayout) view.findViewById(R.id.smallPlayer);


        mainTrackController.setOnClickListener(this);
        img_back.setOnClickListener(this);
        imageLoader=new ImageLoader(getContext());
        Log.d("Current",MainActivity.currentPlayingMusic.getPath());
        initMediaPlayer();

        smallPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSmallPlayerTouched();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity= (MainActivity) context;
        try {
            mCallback = (PlayerFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        mMediaPlayer = new MediaPlayer();
    }

    //初始化播放器
    private void initMediaPlayer(){
        durationInMilliSec = MainActivity.currentPlayingMusic.getDuration();
        progressBar.setMax(durationInMilliSec);
        temp = MediaUtil.getTime(durationInMilliSec);
        totalTime.setText(temp.first + ":" + temp.second);
        playging_music_title.setText(MainActivity.currentPlayingMusic.getTitle());
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
                startTrack = System.currentTimeMillis();
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                endTrack = System.currentTimeMillis();
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
        imageLoader.DisplayImage(MainActivity.currentPlayingMusic.getPath(),playing_music_img);
        try {
            mMediaPlayer.setDataSource(MainActivity.currentPlayingMusic.getPath());
            //设置为循环播放
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        isRefreshed = true;
        pauseClicked = false;
        completed = false;
        isTracking = false;

        if (homeActivity.isPlayerVisible) {
            mainTrackController.setVisibility(View.VISIBLE);
            mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
        } else {
            mainTrackController.setVisibility(View.VISIBLE);
            mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
        }

        isFav = false;

        temp = MediaUtil.getTime(durationInMilliSec);
        totalTime.setText(temp.first + ":" + temp.second);
        progressBar.setMax(durationInMilliSec);
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (isPrepared && !isTracking && getActivity() != null) {
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
                    }
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
                startTrack = System.currentTimeMillis();
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                endTrack = System.currentTimeMillis();
                mMediaPlayer.seekTo(seekBar.getProgress());
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                isTracking = false;
            }

        });
    }


    @Override
    public void onCallbackCalled(int i) {

    }

    @Override
    public void togglePLayPauseCallback() {

    }

    @Override
    public boolean getPauseClicked() {
        return false;
    }

    @Override
    public void setPauseClicked(boolean bool) {

    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.controller:
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mainTrackController.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                }else {
                    if (!completed){
                        mMediaPlayer.start();
                        mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    }
                }
                break;
            case R.id.img_back:
                homeActivity.hidePlayer();
                homeActivity.isPlayerVisible = false;
                break;
            default:
                break;
        }
    }


    public interface PlayerFragmentCallbackListener {
        void onComplete();

        void onPreviousTrack();

        void onEqualizerClicked();

        void onQueueClicked();

        void onPrepared();

        void onFullScreen();

        void onSettingsClicked();

        void onAddedtoFavfromPlayer();

        void onShuffleEnabled();

        void onShuffleDisabled();

        void onSmallPlayerTouched();

        void addCurrentSongtoPlaylist(AudioItem ut);
    }

    public interface onPlayPauseListener {
        void onPlayPause();
    }

    MainActivity homeActivity;
    public static Context ctx;

    public RelativeLayout spToolbar;
    ImageView overflowMenuAB;
    ImageView spImgAB;
    TextView spTitleAB;
    TextView spArtistAB;

    public boolean isStart = true;

    long startTrack;
    long endTrack;

}
