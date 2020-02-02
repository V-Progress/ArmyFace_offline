package com.yunbiao.armyface_offline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yunbiao.armyface_offline.act.SplashActivity;

public class BootRestartReceiver extends BroadcastReceiver {
    private static final String TAG = "BootRestartReceiver";
    private String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action.equals(ACTION)) {
            //自动开关机

            Intent i = new Intent(context, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
