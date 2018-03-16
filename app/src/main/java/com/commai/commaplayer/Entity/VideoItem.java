package com.commai.commaplayer.Entity;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class VideoItem {

    //视频名称
    private String name;

    private String path;
    private int duration;
    private long size;

    private String thumbImgPath;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    @Override
    public String toString() {
        return  "[Video name="+name+" ; path="+path+" size="+size+" thumb="+thumbImgPath+" ]";
    }
}
