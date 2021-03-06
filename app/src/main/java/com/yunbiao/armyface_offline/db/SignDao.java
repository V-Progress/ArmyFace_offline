package com.yunbiao.armyface_offline.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SIGN".
*/
public class SignDao extends AbstractDao<Sign, Long> {

    public static final String TABLENAME = "SIGN";

    /**
     * Properties of entity Sign.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserCode = new Property(1, String.class, "userCode", false, "USER_CODE");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property SignIcon = new Property(3, String.class, "signIcon", false, "SIGN_ICON");
        public final static Property Icon = new Property(4, String.class, "icon", false, "ICON");
        public final static Property SignType = new Property(5, String.class, "signType", false, "SIGN_TYPE");
        public final static Property CompanyCode = new Property(6, String.class, "companyCode", false, "COMPANY_CODE");
        public final static Property CompanyName = new Property(7, String.class, "companyName", false, "COMPANY_NAME");
        public final static Property DeptCode = new Property(8, String.class, "deptCode", false, "DEPT_CODE");
        public final static Property IcNo = new Property(9, String.class, "icNo", false, "IC_NO");
        public final static Property StartTime = new Property(10, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(11, String.class, "endTime", false, "END_TIME");
        public final static Property Date = new Property(12, String.class, "date", false, "DATE");
        public final static Property Device_code = new Property(13, String.class, "device_code", false, "DEVICE_CODE");
        public final static Property Time = new Property(14, long.class, "time", false, "TIME");
        public final static Property Status = new Property(15, String.class, "status", false, "STATUS");
    }


    public SignDao(DaoConfig config) {
        super(config);
    }
    
    public SignDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SIGN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_CODE\" TEXT," + // 1: userCode
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"SIGN_ICON\" TEXT," + // 3: signIcon
                "\"ICON\" TEXT," + // 4: icon
                "\"SIGN_TYPE\" TEXT," + // 5: signType
                "\"COMPANY_CODE\" TEXT," + // 6: companyCode
                "\"COMPANY_NAME\" TEXT," + // 7: companyName
                "\"DEPT_CODE\" TEXT," + // 8: deptCode
                "\"IC_NO\" TEXT," + // 9: icNo
                "\"START_TIME\" TEXT," + // 10: startTime
                "\"END_TIME\" TEXT," + // 11: endTime
                "\"DATE\" TEXT," + // 12: date
                "\"DEVICE_CODE\" TEXT," + // 13: device_code
                "\"TIME\" INTEGER NOT NULL ," + // 14: time
                "\"STATUS\" TEXT);"); // 15: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SIGN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Sign entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userCode = entity.getUserCode();
        if (userCode != null) {
            stmt.bindString(2, userCode);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String signIcon = entity.getSignIcon();
        if (signIcon != null) {
            stmt.bindString(4, signIcon);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }
 
        String signType = entity.getSignType();
        if (signType != null) {
            stmt.bindString(6, signType);
        }
 
        String companyCode = entity.getCompanyCode();
        if (companyCode != null) {
            stmt.bindString(7, companyCode);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(8, companyName);
        }
 
        String deptCode = entity.getDeptCode();
        if (deptCode != null) {
            stmt.bindString(9, deptCode);
        }
 
        String icNo = entity.getIcNo();
        if (icNo != null) {
            stmt.bindString(10, icNo);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(11, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(12, endTime);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(13, date);
        }
 
        String device_code = entity.getDevice_code();
        if (device_code != null) {
            stmt.bindString(14, device_code);
        }
        stmt.bindLong(15, entity.getTime());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(16, status);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Sign entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userCode = entity.getUserCode();
        if (userCode != null) {
            stmt.bindString(2, userCode);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String signIcon = entity.getSignIcon();
        if (signIcon != null) {
            stmt.bindString(4, signIcon);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }
 
        String signType = entity.getSignType();
        if (signType != null) {
            stmt.bindString(6, signType);
        }
 
        String companyCode = entity.getCompanyCode();
        if (companyCode != null) {
            stmt.bindString(7, companyCode);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(8, companyName);
        }
 
        String deptCode = entity.getDeptCode();
        if (deptCode != null) {
            stmt.bindString(9, deptCode);
        }
 
        String icNo = entity.getIcNo();
        if (icNo != null) {
            stmt.bindString(10, icNo);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(11, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(12, endTime);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(13, date);
        }
 
        String device_code = entity.getDevice_code();
        if (device_code != null) {
            stmt.bindString(14, device_code);
        }
        stmt.bindLong(15, entity.getTime());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(16, status);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Sign readEntity(Cursor cursor, int offset) {
        Sign entity = new Sign( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userCode
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // signIcon
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // icon
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // signType
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // companyCode
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // companyName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // deptCode
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // icNo
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // startTime
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // endTime
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // date
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // device_code
            cursor.getLong(offset + 14), // time
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Sign entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSignIcon(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSignType(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCompanyCode(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCompanyName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDeptCode(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIcNo(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStartTime(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setEndTime(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setDate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDevice_code(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setTime(cursor.getLong(offset + 14));
        entity.setStatus(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Sign entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Sign entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Sign entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
