package com.commai.commaplayer.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fanqi on 2018/4/2.
 * Description:最近播放 包括音频，视频的记录
 */

@Entity
public class RecentPlay {

    @Id
    private Long id;

    @Property(nameInDb = "MEDIA_NAME")
    private String mediaName;

    @Property(nameInDb = "MEDIA_PATH")
    private String mediaPath;

    @Property(nameInDb = "THUMB_IMG_PATH")
    private String thumbImgPath;

    @Property(nameInDb = "SIZE")
    private long size;

    @Property(nameInDb = "DURATION")
    private int duration;

    @Property(nameInDb = "ARTIST")
    private String artist;

    @Property(nameInDb = "MEDIA_TYPE")
    private String mediaType;

    @Property(nameInDb = "PLAY_TIME")
    private String playTime;

    @Property(nameInDb = "TOTAL_TIMES")
    private int totalTimes;

    @Generated(hash = 1456665191)
    public RecentPlay(Long id, String mediaName, String mediaPath,
            String thumbImgPath, long size, int duration, String artist,
            String mediaType, String playTime, int totalTimes) {
        this.id = id;
        this.mediaName = mediaName;
        this.mediaPath = mediaPath;
        this.thumbImgPath = thumbImgPath;
        this.size = size;
        this.duration = duration;
        this.artist = artist;
        this.mediaType = mediaType;
        this.playTime = playTime;
        this.totalTimes = totalTimes;
    }

    @Generated(hash = 1055499186)
    public RecentPlay() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaName() {
        return this.mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPath() {
        return this.mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getThumbImgPath() {
        return this.thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public int getTotalTimes() {
        return this.totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }


}
