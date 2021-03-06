package com.commai.commaplayer.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.commai.commaplayer.Entity.RecentPlay;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECENT_PLAY".
*/
public class RecentPlayDao extends AbstractDao<RecentPlay, Long> {

    public static final String TABLENAME = "RECENT_PLAY";

    /**
     * Properties of entity RecentPlay.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MediaName = new Property(1, String.class, "mediaName", false, "MEDIA_NAME");
        public final static Property MediaPath = new Property(2, String.class, "mediaPath", false, "MEDIA_PATH");
        public final static Property ThumbImgPath = new Property(3, String.class, "thumbImgPath", false, "THUMB_IMG_PATH");
        public final static Property Size = new Property(4, long.class, "size", false, "SIZE");
        public final static Property Duration = new Property(5, int.class, "duration", false, "DURATION");
        public final static Property Artist = new Property(6, String.class, "artist", false, "ARTIST");
        public final static Property MediaType = new Property(7, String.class, "mediaType", false, "MEDIA_TYPE");
        public final static Property PlayTime = new Property(8, String.class, "playTime", false, "PLAY_TIME");
        public final static Property TotalTimes = new Property(9, int.class, "totalTimes", false, "TOTAL_TIMES");
    }


    public RecentPlayDao(DaoConfig config) {
        super(config);
    }
    
    public RecentPlayDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECENT_PLAY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"MEDIA_NAME\" TEXT," + // 1: mediaName
                "\"MEDIA_PATH\" TEXT," + // 2: mediaPath
                "\"THUMB_IMG_PATH\" TEXT," + // 3: thumbImgPath
                "\"SIZE\" INTEGER NOT NULL ," + // 4: size
                "\"DURATION\" INTEGER NOT NULL ," + // 5: duration
                "\"ARTIST\" TEXT," + // 6: artist
                "\"MEDIA_TYPE\" TEXT," + // 7: mediaType
                "\"PLAY_TIME\" TEXT," + // 8: playTime
                "\"TOTAL_TIMES\" INTEGER NOT NULL );"); // 9: totalTimes
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECENT_PLAY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RecentPlay entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mediaName = entity.getMediaName();
        if (mediaName != null) {
            stmt.bindString(2, mediaName);
        }
 
        String mediaPath = entity.getMediaPath();
        if (mediaPath != null) {
            stmt.bindString(3, mediaPath);
        }
 
        String thumbImgPath = entity.getThumbImgPath();
        if (thumbImgPath != null) {
            stmt.bindString(4, thumbImgPath);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(7, artist);
        }
 
        String mediaType = entity.getMediaType();
        if (mediaType != null) {
            stmt.bindString(8, mediaType);
        }
 
        String playTime = entity.getPlayTime();
        if (playTime != null) {
            stmt.bindString(9, playTime);
        }
        stmt.bindLong(10, entity.getTotalTimes());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RecentPlay entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mediaName = entity.getMediaName();
        if (mediaName != null) {
            stmt.bindString(2, mediaName);
        }
 
        String mediaPath = entity.getMediaPath();
        if (mediaPath != null) {
            stmt.bindString(3, mediaPath);
        }
 
        String thumbImgPath = entity.getThumbImgPath();
        if (thumbImgPath != null) {
            stmt.bindString(4, thumbImgPath);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(7, artist);
        }
 
        String mediaType = entity.getMediaType();
        if (mediaType != null) {
            stmt.bindString(8, mediaType);
        }
 
        String playTime = entity.getPlayTime();
        if (playTime != null) {
            stmt.bindString(9, playTime);
        }
        stmt.bindLong(10, entity.getTotalTimes());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RecentPlay readEntity(Cursor cursor, int offset) {
        RecentPlay entity = new RecentPlay( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // mediaName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // mediaPath
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // thumbImgPath
            cursor.getLong(offset + 4), // size
            cursor.getInt(offset + 5), // duration
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // artist
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // mediaType
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // playTime
            cursor.getInt(offset + 9) // totalTimes
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RecentPlay entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMediaName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMediaPath(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setThumbImgPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSize(cursor.getLong(offset + 4));
        entity.setDuration(cursor.getInt(offset + 5));
        entity.setArtist(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMediaType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPlayTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTotalTimes(cursor.getInt(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RecentPlay entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RecentPlay entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RecentPlay entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
