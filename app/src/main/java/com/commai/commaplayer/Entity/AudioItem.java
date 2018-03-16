package com.commai.commaplayer.Entity;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class AudioItem {

    //音频名称
    private String name;

    // 艺术家
    private String artist;

    private String path;
    private int duration;
    private long size;


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

    @Override
    public String toString() {
        return "[Audio name="+name+" ; path="+path+" artist="+artist+" ]";
    }
}
