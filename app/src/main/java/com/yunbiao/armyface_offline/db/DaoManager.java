package com.yunbiao.armyface_offline.db;

import android.util.Log;

import com.yunbiao.armyface_offline.APP;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class DaoManager {
    private static final String TAG = "DaoManager";
    private static DaoManager daoManager = new DaoManager();
    private final String DB_NAME = "yb_meeting_db";
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static final long FAILURE = -1;
    public static final long SUCCESS = 0;

    public static DaoManager get() {
        return daoManager;
    }

    private DaoManager() {
    }

    public void initDb() {
        initDb(DB_NAME);
    }

    public void initDb(Object uniqueId) {
        initDb("db_" + uniqueId);
    }

    public void initDb(String name) {
        Log.e(TAG, "initDb: ");
        MySQLiteHelper helper = new MySQLiteHelper(APP.getContext(), name, null);
        Log.e(TAG, "initDb: " + helper);
        Database db = helper.getWritableDb();
        Log.e(TAG, "initDb: " + db);
        daoMaster = new DaoMaster(db);
        Log.e(TAG, "initDb: " + daoMaster);
        daoSession = daoMaster.newSession();
        Log.e(TAG, "initDb: " + daoSession);
        daoSession.clear();
        daoSession.getUserDao().detachAll();
        daoSession.getSignDao().detachAll();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public <T> long add(T clazz) {
        if (daoSession == null) {
            return FAILURE;
        }
        return daoSession.insert(clazz);
    }

    public <T> long addOrUpdate(T clazz) {
        if (daoSession == null) {
            return FAILURE;
        }
        return daoSession.insertOrReplace(clazz);
    }

    public <T> long update(T t) {
        if (daoSession == null) {
            return FAILURE;
        }
        daoSession.update(t);
        return SUCCESS;
    }

    public <T> List<T> queryAll(Class<T> clazz) {
        if (daoSession == null) {
            return null;
        }
        return daoSession.loadAll(clazz);
    }

    public <T> long delete(T t) {
        if (daoSession == null) {
            return FAILURE;
        }
        daoSession.delete(t);
        return SUCCESS;
    }

    public List<Sign> querySignByDate(String date) {
        if (daoSession == null) {
            return null;
        }
        return daoSession.getSignDao().queryBuilder().where(SignDao.Properties.Date.eq(date)).list();
    }

    public User queryUserByUserName(String userId) {
        if (daoSession == null) {
            return null;
        }
        return daoSession.getUserDao().queryBuilder().where(UserDao.Properties.UserCode.eq(userId)).unique();
    }

    public User queryUserByCard(String barcode) {
        if (daoSession == null) {
            return null;
        }
        return daoSession.getUserDao().queryBuilder().where(UserDao.Properties.IcNo.eq(barcode)).unique();

    }
}
