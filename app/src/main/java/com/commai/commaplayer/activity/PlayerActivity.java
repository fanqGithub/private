package com.commai.commaplayer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseActivity;
import com.commai.commaplayer.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author:范启 Created on 2018/3/18.
 * Description:
 */

public class PlayerActivity extends BaseActivity {

    private IjkVideoView ijkVideoView;

    private String mediaPath="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ijkVideoView=findViewById(R.id.ijkPlayerView);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        if (getIntent().getExtras()!=null){
            mediaPath=getIntent().getStringExtra("mediaPath");
        }
        ijkVideoView.setVideoURI(Uri.parse(mediaPath));
        ijkVideoView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkVideoView != null) {
            ijkVideoView.stopPlayback();
        }
    }

}
