package com.yunbiao.armyface_offline.utils;

import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import com.yunbiao.armyface_offline.act.MainActivity;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.Sign;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.faceview.CompareResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUtil {
    private static final String TAG = "SignUtil";
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Sign checkCardSign(int orientation, String card) {
        if (TextUtils.isEmpty(card)) {
            return null;
        }

        User user = DaoManager.get().queryUserByCard(card);
        if (user == null) {
            return null;
        }

        Sign sign = new Sign();
        sign.setUserCode(user.getUserCode());
        sign.setUserName(user.getUserName());
        sign.setCompanyCode(user.getCompanyCode());
        sign.setCompanyName(user.getCompanyName());
        sign.setDeptCode(user.getDeptCode());
        sign.setSignType("2");
        sign.setIcNo(user.getIcNo());
        sign.setDevice_code(HeartBeatClient.getDeviceNo());
        sign.setStatus(user.getUserStatus());
        sign.setIcon(user.getIcon());

        long time = System.currentTimeMillis();

        sign.setTime(time);
        sign.setDate(dateFormat.format(new Date(time)));

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            sign.setStartTime(dateTimeFormat.format(new Date(time)));
        } else {
            sign.setEndTime(dateTimeFormat.format(new Date(time)));
        }

        return sign;

    }

    public static Sign checkFaceSign(int orientation, CompareResult result) {
        if (result == null) {
            return null;
        }

        String userName = result.getUserName();
        User user = DaoManager.get().queryUserByUserName(userName);
        if (user == null) {
            return null;
        }

        Sign sign = new Sign();
        sign.setUserCode(user.getUserCode());
        sign.setUserName(user.getUserName());
        sign.setCompanyCode(user.getCompanyCode());
        sign.setCompanyName(user.getCompanyName());
        sign.setDeptCode(user.getDeptCode());
        sign.setSignType("1");
        sign.setIcNo(user.getIcNo());
        sign.setDevice_code(HeartBeatClient.getDeviceNo());
        sign.setStatus(user.getUserStatus());
        sign.setIcon(user.getIcon());

        long time = System.currentTimeMillis();

        sign.setTime(time);
        sign.setDate(dateFormat.format(new Date(time)));

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            sign.setStartTime(dateTimeFormat.format(new Date(time)));
        } else {
            sign.setEndTime(dateTimeFormat.format(new Date(time)));
        }

        return sign;
    }

}
