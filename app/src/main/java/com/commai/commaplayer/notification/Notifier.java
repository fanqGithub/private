package com.commai.commaplayer.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.commai.commaplayer.Constants;
import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.service.MusicPlayService;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanqi on 2018/3/23.
 * Description:
 */

public class Notifier {
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNELID="com.comma.player";
    private MusicPlayService playService;
    private NotificationManager notificationManager;

    public static Notifier getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static Notifier instance = new Notifier();
    }

    private Notifier() {
    }

    public void init(MusicPlayService playService) {
        this.playService = playService;
        notificationManager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showPlay(AudioItem music) {
        if (music == null) {
            return;
        }
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music, true));
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music, true));
    }

    public void showPause(AudioItem music) {
        if (music == null) {
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music, false));
    }

    public void cancelAll() {

        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, AudioItem music, boolean isPlaying) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNELID, "Commaai", NotificationManager.IMPORTANCE_DEFAULT);
            //是否在桌面icon右上角展示小红点
            channel.enableLights(true);
            //小红点颜色
            channel.setLightColor(Color.RED);
            //是否在长按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNELID)
                    .setTicker("CommaPlayer正在播放")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_music_default)
                    .setOngoing(true)
                    .setCustomBigContentView(getRemoteViews(context, music, isPlaying));
            return builder.build();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,null)
                .setTicker("CommaPlayer正在播放")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_music_default)
                .setOngoing(true)
                .setCustomBigContentView(getRemoteViews(context, music, isPlaying));
        return builder.build();
    }

    private RemoteViews getRemoteViews(Context context, AudioItem music, boolean isPlaying) {
        ImageLoader loader=new ImageLoader(context);
        String title = music.getTitle();
        String subtitle =music.getArtist()+" - "+music.getAlbum();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        Bitmap bitmap=loader.getBitmap(music.getPath());
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.noti_img, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.noti_img, R.mipmap.ic_music_default);
        }
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_alubm, subtitle);

        Intent prevIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        prevIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_PREVIOUS);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.ic_skip_previous_black_24dp);
        remoteViews.setOnClickPendingIntent(R.id.noti_previous, prevPendingIntent);

        Intent playIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_PLAY_PAUSE);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.noti_play_pause, getPlayIcon(isPlaying));
        remoteViews.setOnClickPendingIntent(R.id.noti_play_pause, playPendingIntent);

        Intent nextIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        nextIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.noti_next, R.drawable.ic_skip_next_black_24dp);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, nextPendingIntent);

        Intent exitIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        exitIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_EXIST);
        PendingIntent existPendingIntent = PendingIntent.getBroadcast(context, 3, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.cancel_noti, R.drawable.ic_close_black_24dp);
        remoteViews.setOnClickPendingIntent(R.id.cancel_noti, existPendingIntent);

        return remoteViews;
    }

    private int getPlayIcon(boolean isPlaying) {
        if (isPlaying) {
            return R.drawable.ic_pause_black_24dp;
        }else {
            return R.drawable.ic_play_arrow_black_24dp;
        }
    }

}
