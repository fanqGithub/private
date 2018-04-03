package com.commai.commaplayer.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fanqi on 2018/4/3.
 * Description:播放列表
 */

@Entity
public class PlayListBean {

    @Id
    private Long id;

    @Property(nameInDb = "PLAY_LIST_NAME")
    private String playListName;

    @Property(nameInDb = "PLAY_LSIT_JSON")
    private String playListJson;

    @Property(nameInDb = "CREATE_TIME")
    private String createTime;

    @Generated(hash = 2091748161)
    public PlayListBean(Long id, String playListName, String playListJson,
            String createTime) {
        this.id = id;
        this.playListName = playListName;
        this.playListJson = playListJson;
        this.createTime = createTime;
    }

    @Generated(hash = 1039443864)
    public PlayListBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayListName() {
        return this.playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public String getPlayListJson() {
        return this.playListJson;
    }

    public void setPlayListJson(String playListJson) {
        this.playListJson = playListJson;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



}
