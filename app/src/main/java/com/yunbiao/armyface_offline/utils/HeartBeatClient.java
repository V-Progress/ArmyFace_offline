package com.yunbiao.armyface_offline.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HeartBeatClient {
    /**
     * 获取设备唯一编号
     *
     * @return
     */
    public static String getDeviceNo() {
        String sbDeviceId = SpUtils.getStr(SpUtils.DEVICE_UNIQUE_NO);
        if(TextUtils.isEmpty(sbDeviceId)){
            sbDeviceId = CommonUtils.getMacAddress();
            SpUtils.saveStr(SpUtils.DEVICE_UNIQUE_NO,sbDeviceId);
        }
        return sbDeviceId;
    }
}
