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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.fragment.PlayerFragment;
import com.commai.commaplayer.listener.ServiceCallBack;

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

    private ServiceCallBack serviceCallbacks;
    private PlayerFragment pFragment;
    private Intent startIntent;

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of MyService so clients can call public methods
            return MediaPlayerService.this;
        }
    }

    public void setCallbacks(ServiceCallBack callbacks) {
        serviceCallbacks = callbacks;
        pFragment = serviceCallbacks.getPlayerFragment();
        if (pFragment != null) {
            pFragment.mCallback7 = this;
        }
        if (m_objMediaSessionManager == null) {
//            initMediaSessions();
        }
//        handleIntent(startIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startIntent = intent;
        return new LocalBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            pFragment = ((MainActivity) PlayerFragment.ctx).getPlayerFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pFragment != null) {
            pFragment.mCallback7 = this;
        }

        if (m_objMediaSessionManager == null) {
//            initMediaSessions();
        }
//        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onUnbind(Intent intent) {
        m_objMediaSession.release();
        return super.onUnbind(intent);
    }

    @Override
    public void onPlayPause() {
        if (PlayerFragment.mMediaPlayer != null && PlayerFragment.mMediaPlayer.isPlaying()) {
//            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
        } else {
//            buildNotification(generateAction(R.drawable.ic_play_notif, "Play", Constants.ACTION_PLAY));
        }
    }
}