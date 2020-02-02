package com.yunbiao.armyface_offline;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.x;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class APP extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //初始化xutils 3.0
        x.Ext.init(this);

        OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        OkHttpUtils.initClient(build);
    }

    public static Context getContext() {
        return context;
    }

    public static void bindProtectedService() {
        context.startService(new Intent(context, MyProtectService.class));
    }

    public static void unbindProtectedService() {
        context.stopService(new Intent(context, MyProtectService.class));
    }

    public static void exit() {
        unbindProtectedService();
        //关闭整个应用
        System.exit(0);
    }
}
