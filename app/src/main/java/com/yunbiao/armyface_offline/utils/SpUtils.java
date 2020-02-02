package com.yunbiao.armyface_offline.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yunbiao.armyface_offline.APP;

/**
 * Created by LiuShao on 2016/2/21.
 */
public class SpUtils {

    private static SharedPreferences sp;
    private static final String SP_NAME = "YB_FACE";//
    public static final String APP_VERSION_ID = "appVersionId";//APP版本，签入、签出
    public static final String DEVICE_UNIQUE_NO = "deviceUniqueNo";//设备唯一码
    public static final String MENU_PWD = "menuPwd";//管理密码
    public static final String IMPORT_URL = "importUrl";
    public static final String WEB_URL = "webUrl";
    public static final String IS_MIRROR = "isMirror";//是否镜像
    public static final String CAMERA_ANGLE = "cameraAngle";//摄像头角度

    static {
        sp = APP.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isMirror() {
        return getBoolean(IS_MIRROR, false);
    }

    public static void setMirror(boolean b) {
        saveBoolean(IS_MIRROR, b);
    }

    public static void saveStr(String key, String value) {
        if (sp != null) {
            sp.edit().putString(key, value).commit();
        }
    }

    public static void saveInt(String key, int value) {
        if (sp != null) {
            sp.edit().putInt(key, value).commit();
        }
    }

    public static void saveFloat(String key, float value) {
        if (sp != null) {
            sp.edit().putFloat(key, value).commit();
        }
    }

    public static void saveLong(String key, long value) {
        if (sp != null) {
            sp.edit().putLong(key, value).commit();
        }
    }

    public static long getLong(String key) {
        if (sp != null) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public static String getStr(String key) {
        if (sp != null) {
            return sp.getString(key, "");
        }
        return "";
    }

    public static String getStr(String key, String defaultValue) {
        if (sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public static int getInt(String key) {
        if (sp != null) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public static Float getFloat(String key, float defaultValue) {
        if (sp != null) {
            return sp.getFloat(key, defaultValue);
        }
        return 0f;
    }

    public static int getIntOrDef(String key, int def) {
        if (sp != null) {
            return sp.getInt(key, def);
        }
        return def;
    }

    public static void clear(Context context) {
        if (sp != null) {
            sp.edit().clear().apply();
        }
    }

    public static void saveBoolean(String key, boolean b) {
        if (sp != null) {
            sp.edit().putBoolean(key, b).commit();
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (sp != null) {
            return sp.getBoolean(key, defValue);
        }
        return defValue;
    }

//    public static void saveString(Context context, String key, String value) {
//        if (sp == null)
//            sp = context.getSharedPreferences(SP_NAME, 0);
//        sp.edit().putString(key, value).apply();
//    }
//
//    public static String getString(Context context, String key, String defValue) {
//        if (sp == null)
//            sp = context.getSharedPreferences(SP_NAME, 0);
//        return sp.getString(key, defValue);
//    }
//
//    public static void saveInt(Context context, String key, int value) {
//        if (sp == null)
//            sp = context.getSharedPreferences(SP_NAME, 0);
//        sp.edit().putInt(key, value).apply();
//    }
//
//    public static int getInt(Context context, String key, int value) {
//        if (sp == null)
//            sp = context.getSharedPreferences(SP_NAME, 0);
//        return sp.getInt(key, value);
//    }
}
