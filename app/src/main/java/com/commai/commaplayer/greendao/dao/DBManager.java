package com.commai.commaplayer.greendao.dao;

import android.content.Context;
import org.greenrobot.greendao.database.Database;

/**
 * Created by fanqi on 2018/4/2.
 * Description:
 */

public class DBManager {

    private static final String DB_NAME = "comma_player.db";
    private RecentPlayDao recentPlayDao;
    private PlayListBeanDao playListBeanDao;

    public static DBManager get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static DBManager instance = new DBManager();
    }

    public void init(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        recentPlayDao = daoSession.getRecentPlayDao();
        playListBeanDao=daoSession.getPlayListBeanDao();
    }

    private DBManager() {
    }

    public RecentPlayDao getRecentPlayDao() {
        return recentPlayDao;
    }

    public PlayListBeanDao getPlayListBeanDao(){
        return playListBeanDao;
    }
}
