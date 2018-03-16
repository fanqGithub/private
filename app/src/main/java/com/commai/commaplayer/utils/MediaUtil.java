package com.commai.commaplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class MediaUtil {

    /**
     * 直接查询数据库
     * @param context
     * @return
     */
    public static List<AudioItem> scanAudios(Context context){
        List<AudioItem> musicList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AudioItem musicBean = new AudioItem();
                musicBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                musicBean.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                musicBean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                musicBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                musicBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                musicList.add(musicBean);
            }
        }
        cursor.close();
        return musicList;
    }


    public static List<VideoItem> scanVideos(Context context){
        List<VideoItem> videoItemLis=new ArrayList<>();
        Cursor cursor =context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.MediaColumns.DATE_MODIFIED + " DESC");
        if (cursor!=null){
            while (cursor.moveToNext()){
                VideoItem videoBean = new VideoItem();
                videoBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                videoBean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                videoBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                videoBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                videoBean.setThumbImgPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)));
                videoItemLis.add(videoBean);
            }
        }
        cursor.close();
        return videoItemLis;
    }


}
