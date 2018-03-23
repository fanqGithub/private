package com.commai.commaplayer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;

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
                null, null, null,MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AudioItem musicBean = new AudioItem();
                musicBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                musicBean.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                musicBean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                musicBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                musicBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                musicBean.setAlbum_id(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                musicBean.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                musicBean.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                musicBean.setSongId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                musicList.add(musicBean);
            }
        }
        cursor.close();
        return musicList;
    }


    public static List<VideoItem> scanVideos(Context context){
        List<VideoItem> videoItemLis=new ArrayList<>();
        Cursor cursor =context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (cursor!=null){
            while (cursor.moveToNext()){
                VideoItem videoBean = new VideoItem();
                videoBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                videoBean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                videoBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                videoBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
                String[] selectionArgs = new String[]{id+""};
                Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID},
                        selection, selectionArgs, null);
                if(thumbCursor.moveToFirst()){
                    videoBean.setThumbImgPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)));

                }
                videoBean.setThumbImgPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)));
                videoItemLis.add(videoBean);
            }
        }
        cursor.close();
        return videoItemLis;
    }


    public static Bitmap getAlbumArt(Context  context,int album_id){
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cursor = context.getContentResolver().query(  Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),  projection, null, null, null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0){  cursor.moveToNext();
            album_art = cursor.getString(0);
        }
        Bitmap bm = null;
        if (album_art == null){
            bm= BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_music_default);
        }else{
            bm = BitmapFactory.decodeFile(album_art);
        }
        cursor.close();
        return bm;
    }


    public static Pair<String, String> getTime(int millsec) {
        int min, sec;
        sec = millsec / 1000;
        min = sec / 60;
        sec = sec % 60;
        String minS, secS;
        minS = String.valueOf(min);
        secS = String.valueOf(sec);
        if (sec < 10) {
            secS = "0" + secS;
        }
        Pair<String, String> pair = Pair.create(minS, secS);
        return pair;
    }

    public static void hideKeyboard(Context ctx) {
        View view = ((MainActivity) ctx).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
