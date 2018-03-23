package com.commai.commaplayer.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.commai.commaplayer.notification.Notifier;

/**
 * Created by fanqi on 2018/3/22.
 * Description:
 */

public class MusicPlayService extends Service {

    private static final String TAG = "MusicService";

    public class PlayBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        MusicPlayer.get().init(this);
        Notifier.getInstance().init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, MusicPlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Actions.ACTION_STOP:
                    stop();
                    break;
                default:
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void stop() {
        MusicPlayer.get().stopPlayer();
        Notifier.getInstance().cancelAll();
    }

}
