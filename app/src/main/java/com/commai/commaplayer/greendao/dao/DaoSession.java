package com.commai.commaplayer.greendao.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.greendao.bean.PlayListBean;

import com.commai.commaplayer.greendao.dao.RecentPlayDao;
import com.commai.commaplayer.greendao.dao.PlayListBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig recentPlayDaoConfig;
    private final DaoConfig playListBeanDaoConfig;

    private final RecentPlayDao recentPlayDao;
    private final PlayListBeanDao playListBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        recentPlayDaoConfig = daoConfigMap.get(RecentPlayDao.class).clone();
        recentPlayDaoConfig.initIdentityScope(type);

        playListBeanDaoConfig = daoConfigMap.get(PlayListBeanDao.class).clone();
        playListBeanDaoConfig.initIdentityScope(type);

        recentPlayDao = new RecentPlayDao(recentPlayDaoConfig, this);
        playListBeanDao = new PlayListBeanDao(playListBeanDaoConfig, this);

        registerDao(RecentPlay.class, recentPlayDao);
        registerDao(PlayListBean.class, playListBeanDao);
    }
    
    public void clear() {
        recentPlayDaoConfig.clearIdentityScope();
        playListBeanDaoConfig.clearIdentityScope();
    }

    public RecentPlayDao getRecentPlayDao() {
        return recentPlayDao;
    }

    public PlayListBeanDao getPlayListBeanDao() {
        return playListBeanDao;
    }

}
