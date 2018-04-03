package com.commai.commaplayer.Entity;

import android.text.TextUtils;

/**
 * Created by fanqi on 2018/4/3.
 * Description:选择添加到播放列表的实体(重组，结合video和audio)
 */

public class SelectedMediaItem {

    private String mediaName;

    private String mediaPath;

    private String thumbImgPath;

    private long size;

    private int duration;

    private String artist;

    private String mediaType;

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SelectedMediaItem)) {
            return false;
        }
        SelectedMediaItem item = (SelectedMediaItem) obj;
        if (TextUtils.equals(item.mediaName, this.mediaName)
                && TextUtils.equals(item.artist, this.artist)
                && TextUtils.equals(item.mediaPath, this.mediaPath)
                && item.duration == this.duration) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[Selected name="+mediaName+" ; path="+mediaPath+" artist="+artist+"]";
    }

}
