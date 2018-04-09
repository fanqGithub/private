package com.commai.commaplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.enums.PlayModeEnum;
import com.commai.commaplayer.service.Actions;
import com.commai.commaplayer.service.MusicPlayer;
import com.commai.commaplayer.service.OnPlayerEventListener;
import com.commai.commaplayer.shareprefrence.Preferences;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanqi on 2018/3/22.
 * Description:
 */

public class PlayingFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, OnPlayerEventListener,View.OnClickListener{

    //中间的圆图
    public ImageView playing_music_img;
    //播放标题
    public TextView playging_music_title;

    private ImageView img_back;

    private ImageLoader imageLoader;

    private Pair<String,String> temp;

    private ImageView ivPlayingBg;
    private SeekBar sbProgress;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private ImageView ivMode;
    private ImageView ivPlay;
    private ImageView ivNext;
    private ImageView ivPrev;
//    private SeekBar sbVolume;

    private AudioManager mAudioManager;
    private int mLastProgress;
    private boolean isDraggingProgress=false;

    public static int durationInMilliSec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_player_music, container, false);
        playing_music_img=view.findViewById(R.id.playing_music_img);
        sbProgress = (SeekBar) view.findViewById(R.id.progressBar);
        tvCurrentTime = (TextView) view.findViewById(R.id.currTime);
        tvTotalTime=view.findViewById(R.id.totalTime);
        playging_music_title=view.findViewById(R.id.music_title);
        playging_music_title.setSelected(true);
        img_back=view.findViewById(R.id.img_back);
        ivPlay=view.findViewById(R.id.controller);
        ivNext = (ImageView) view.findViewById(R.id.next);
        ivPrev = (ImageView) view.findViewById(R.id.previous);
        ivMode=view.findViewById(R.id.repeat_controller);
        imageLoader=new ImageLoader(getContext());

        img_back.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVolume();
        initPlayMode();
        onChangeImpl(MusicPlayer.get().getPlayMusic());
        MusicPlayer.get().addOnPlayEventListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Actions.VOLUME_CHANGED_ACTION);
        getContext().registerReceiver(mVolumeReceiver, filter);
    }

    private void initVolume() {
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void initPlayMode() {
        int mode = Preferences.getPlayMode();
        ivMode.setImageLevel(mode);
    }

    @Override
    public void onChange(AudioItem music) {
        onChangeImpl(music);
    }

    @Override
    public void onPlayerStart() {
        ivPlay.setSelected(true);
        Animation anim= AnimationUtils.loadAnimation(getContext(),R.anim.music_playing_img_round_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        anim.setInterpolator(interpolator);
        if (anim!=null){
            playing_music_img.startAnimation(anim);
        }
    }

    @Override
    public void onPlayerPause() {
        ivPlay.setSelected(false);
        playing_music_img.clearAnimation();
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        if (!isDraggingProgress) {
            temp = MediaUtil.getTime(progress);
            sbProgress.setProgress(progress);
            tvCurrentTime.setText(temp.first + ":" + temp.second);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.repeat_controller:
                switchPlayMode();
                break;
            case R.id.controller:
                play();
                break;
            case R.id.next:
                next();
                break;
            case R.id.previous:
                prev();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbProgress) {
            if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
//                tvCurrentTime.setText(formatTime(progress));
                mLastProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            isDraggingProgress = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            isDraggingProgress = false;
            if (MusicPlayer.get().isPlaying() || MusicPlayer.get().isPausing()) {
                int progress = seekBar.getProgress();
                MusicPlayer.get().seekTo(progress);
            } else {
                seekBar.setProgress(0);
            }
        }
    }

    private void onChangeImpl(AudioItem music) {
        if (music == null) {
            return;
        }

        playging_music_title.setText(music.getTitle());
        imageLoader.DisplayImage(music.getPath(),playing_music_img);
        sbProgress.setProgress((int) MusicPlayer.get().getAudioPosition());
        sbProgress.setSecondaryProgress(0);
        sbProgress.setMax((int) music.getDuration());
        mLastProgress = 0;
        durationInMilliSec=music.getDuration();
        temp = MediaUtil.getTime(durationInMilliSec);
        tvTotalTime.setText(temp.first + ":" + temp.second);
        tvCurrentTime.setText("00:00");
        if (MusicPlayer.get().isPlaying() || MusicPlayer.get().isPreparing()) {
            ivPlay.setSelected(true);
        } else {
            ivPlay.setSelected(false);
        }
    }

    private void play() {
        MusicPlayer.get().playPause();
    }

    private void next() {
        MusicPlayer.get().next();
    }

    private void prev() {
        MusicPlayer.get().prev();
    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                showToast("随机播放");
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                showToast("单曲循环");
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                showToast("列表循环");
                break;
            default:
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
    }




    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mVolumeReceiver);
        MusicPlayer.get().removeOnPlayEventListener(this);
        super.onDestroy();
    }


}
