package com.commai.commaplayer.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanqi on 2018/3/14.
 * Description:
 */

public class VideoItem implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeInt(this.duration);
        dest.writeLong(this.size);
        dest.writeString(this.thumbImgPath);
    }

    public VideoItem() {
    }

    protected VideoItem(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.duration = in.readInt();
        this.size = in.readLong();
        this.thumbImgPath = in.readString();
    }

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel source) {
            return new VideoItem(source);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}
