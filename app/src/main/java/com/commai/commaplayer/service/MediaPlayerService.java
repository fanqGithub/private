package com.commai.commaplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.commai.commaplayer.fragment.PlayerFragment;

/**
 * 音乐播放服务
 * @author commai
 */
public class MediaPlayerService extends Service implements PlayerFragment.onPlayPauseListener {


    private MediaSessionManager m_objMediaSessionManager;
    private MediaSession m_objMediaSession;
    private MediaController m_objMediaController;
    private MediaPlayer m_objMediaPlayer;
    private NotificationManager notificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayPause() {

    }
}