package com.commai.commaplayer.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.commai.commaplayer.greendao.bean.PlayListBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PLAY_LIST_BEAN".
*/
public class PlayListBeanDao extends AbstractDao<PlayListBean, Long> {

    public static final String TABLENAME = "PLAY_LIST_BEAN";

    /**
     * Properties of entity PlayListBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PlayListName = new Property(1, String.class, "playListName", false, "PLAY_LIST_NAME");
        public final static Property PlayListJson = new Property(2, String.class, "playListJson", false, "PLAY_LSIT_JSON");
        public final static Property CreateTime = new Property(3, String.class, "createTime", false, "CREATE_TIME");
    }


    public PlayListBeanDao(DaoConfig config) {
        super(config);
    }
    
    public PlayListBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PLAY_LIST_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PLAY_LIST_NAME\" TEXT," + // 1: playListName
                "\"PLAY_LSIT_JSON\" TEXT," + // 2: playListJson
                "\"CREATE_TIME\" TEXT);"); // 3: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PLAY_LIST_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PlayListBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String playListName = entity.getPlayListName();
        if (playListName != null) {
            stmt.bindString(2, playListName);
        }
 
        String playListJson = entity.getPlayListJson();
        if (playListJson != null) {
            stmt.bindString(3, playListJson);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PlayListBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String playListName = entity.getPlayListName();
        if (playListName != null) {
            stmt.bindString(2, playListName);
        }
 
        String playListJson = entity.getPlayListJson();
        if (playListJson != null) {
            stmt.bindString(3, playListJson);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PlayListBean readEntity(Cursor cursor, int offset) {
        PlayListBean entity = new PlayListBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // playListName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // playListJson
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // createTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PlayListBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlayListName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPlayListJson(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCreateTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PlayListBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PlayListBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PlayListBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
