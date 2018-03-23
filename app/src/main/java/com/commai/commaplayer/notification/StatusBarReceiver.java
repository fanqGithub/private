package com.commai.commaplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.commai.commaplayer.service.MusicPlayer;

/**
 *
 */
public class StatusBarReceiver extends BroadcastReceiver {

    public static final String ACTION_STATUS_BAR = "com.comma.music.STATUS_BAR_ACTIONS";
    public static final String EXTRA = "extra";
    public static final String EXTRA_NEXT = "next";
    public static final String EXTRA_PLAY_PAUSE = "play_pause";
    public static final String EXTRA_PREVIOUS="previous";
    public static final String EXTRA_EXIST="exist";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }

        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, EXTRA_NEXT)) {
            MusicPlayer.get().next();
        } else if (TextUtils.equals(extra, EXTRA_PLAY_PAUSE)) {
            MusicPlayer.get().playPause();
        }else if (TextUtils.equals(extra,EXTRA_PREVIOUS)){
            MusicPlayer.get().prev();
        }else if (TextUtils.equals(extra,EXTRA_EXIST)){
            MusicPlayer.get().stopPlayer();
            Notifier.getInstance().cancelAll();
        }
    }
}
