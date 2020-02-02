package com.yunbiao.armyface_offline.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yunbiao.armyface_offline.APP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by LiuShao on 2016/4/6.
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    @SuppressLint("HardwareIds")
    public static String getMacAddress() {
        String macAddress = "";
        WifiManager wifiManager = (WifiManager) APP.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
        }

        if (!TextUtils.isEmpty(macAddress) && macAddress.equals("02:00:00:00:00:00")) {//6.0及以上系统获取的mac错误
            macAddress = CommonUtils.getMacAddr();
        }

        if (TextUtils.isEmpty(macAddress)) {
            macAddress = CommonUtils.getSixOSMac();
        }

        macAddress = macAddress.toUpperCase();
        String macS = "";
        for (int i = macAddress.length() - 1; i >= 0; i--) {
            macS += macAddress.charAt(i);
        }
        UUID uuid2 = new UUID(macS.hashCode(), macAddress.hashCode());
        return uuid2.toString();
    }

    // Android 6.0以上获取WiFi的Mac地址
    //由于android6.0对wifi mac地址获取进行了限制，用原来的方法获取会获取到02:00:00:00:00:00这个固定地址。
    //但是可以通过读取节点进行获取"/sys/class/net/wlan0/address"
    public static String getMacAddr() {
        try {
            return loadFileAsString("/sys/class/net/wlan0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String loadFileAsString(String filePath)
            throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * 获取主板的信息存到sp里 供以后判断主板厂家使用
     */
    public static String saveBroadInfo() {
        Process process = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
            InputStream outs = process.getInputStream();
            InputStreamReader isrout = new InputStreamReader(outs);
            br = new BufferedReader(isrout, 8 * 1024);
            StringBuffer result = new StringBuffer("");
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            String broadInfo = result.toString();
            Log.e(TAG, "主板信息: " + broadInfo);
            return broadInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 6.0及以上的安卓系统获取mac为02:00:00:00:00:00，使用这个方法修改
     */
    public static String getSixOSMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
