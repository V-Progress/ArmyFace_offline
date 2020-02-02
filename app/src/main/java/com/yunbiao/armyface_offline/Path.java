package com.yunbiao.armyface_offline;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Path {
    private static final String TAG = "Path";
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    public static final String ROOT_PATH = SD_PATH + File.separator + "ybData";//应用资源根目录

    public static final String ADS_PATH = ROOT_PATH + File.separator + "ads";//大屏广告目录

    public static final String INFO_PATH = ROOT_PATH + File.separator + "info";//小屏宣传目录

    public static final String PERSON_PATH = ROOT_PATH + File.separator + "person";//人员信息会复制到该目录

    public static final String FEATURE_PATH = ROOT_PATH + File.separator + "feature";//人脸特征目录

    public static final String RECORD_PATH = ROOT_PATH + File.separator + "record";//签到记录图片目录

    public static final String PERSON_PATH_TEMP = ROOT_PATH + File.separator + "person_temp";//人员信息包解压目录

    public static final String PERSON_DOWNLOAD_TEMP = ROOT_PATH + File.separator + "person_download_temp";//人脸信息下载临时目录

    public static final String EXPORT_SIGN_PATH = SD_PATH + File.separator + "导出记录";

    public static void init() {
        File file = new File(ROOT_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.e(TAG, "init: " + file.getPath() + " --- " + file.exists() + " --- " + file.canRead() + " --- " + file.canWrite());

        File adsFile = new File(ADS_PATH);
        if (!adsFile.exists()) {
            adsFile.mkdirs();
        }
        Log.e(TAG, "init: " + adsFile.getPath() + " --- " + adsFile.exists() + " --- " + adsFile.canRead() + " --- " + adsFile.canWrite());

        File infoFile = new File(INFO_PATH);
        if (!infoFile.exists()) {
            infoFile.mkdirs();
        }
        Log.e(TAG, "init: " + infoFile.getPath() + " --- " + infoFile.exists() + " --- " + infoFile.canRead() + " --- " + infoFile.canWrite());

        File personFile = new File(PERSON_PATH);
        if (!personFile.exists()) {
            personFile.mkdirs();
        }
        Log.e(TAG, "init: " + personFile.getPath() + " --- " + personFile.exists() + " --- " + personFile.canRead() + " --- " + personFile.canWrite());

        File featureFile = new File(FEATURE_PATH);
        if (!featureFile.exists()) {
            featureFile.mkdirs();
        }
        Log.e(TAG, "init: " + featureFile.getPath() + " --- " + featureFile.exists() + " --- " + featureFile.canRead() + " --- " + featureFile.canWrite());

        File recordFile = new File(RECORD_PATH);
        if (!recordFile.exists()) {
            recordFile.mkdirs();
        }
        Log.e(TAG, "init: " + recordFile.getPath() + " --- " + recordFile.exists() + " --- " + recordFile.canRead() + " --- " + recordFile.canWrite());

        File exportSignDir = new File(EXPORT_SIGN_PATH);
        if(!exportSignDir.exists()){
            exportSignDir.mkdirs();
        }
    }
}
