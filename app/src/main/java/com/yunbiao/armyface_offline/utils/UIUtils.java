package com.yunbiao.armyface_offline.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbiao.armyface_offline.R;

/**
 * Created by Administrator on 2019/4/18.
 */

public class UIUtils {

    private static Toast mToast;

    public static void showShort(Activity context,String message){
        showTitleTip(context,message);
    }

    private static Toast mTipsToast;

    @SuppressLint("NewApi")
    public static void showTitleTip(Context context, String title) {
        if(mTipsToast != null){
            mTipsToast.cancel();
            mTipsToast = null;
        }
        int padding = 20;
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackground(context.getResources().getDrawable(R.drawable.shape_spinner_drop));
        textView.setTextSize(36);
        textView.setText(title);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(padding,padding,padding,padding);
        textView.setGravity(Gravity.CENTER);
        textView.setElevation(10);

        mTipsToast = new Toast(context);
        mTipsToast.setDuration(Toast.LENGTH_SHORT);
        mTipsToast.setGravity(Gravity.CENTER, 0, 0);
        mTipsToast.setView(textView);
        mTipsToast.show();
    }
}
