package com.commai.commaplayer.Entity;

import android.text.TextUtils;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class AudioItem {

    private int songId;

    //音频名称
    private String name;

    // 艺术家
    private String artist;

    private String path;
    private int duration;
    private long size;

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    private int album_id;

    private String title;

    private String album;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AudioItem)) {
            return false;
        }
        AudioItem music = (AudioItem) obj;
        if (music.songId > 0 && music.songId == this.songId) {
            return true;
        }
        if (TextUtils.equals(music.title, this.title)
                && TextUtils.equals(music.artist, this.artist)
                && TextUtils.equals(music.album, this.album)
                && music.duration == this.duration) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[Audio name="+name+" ; path="+path+" artist="+artist+" albumid"+album_id +" title"+title+" ]";
    }
}
