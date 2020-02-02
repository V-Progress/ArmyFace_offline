package com.yunbiao.armyface_offline.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.utils.SpUtils;

import io.reactivex.functions.Consumer;

public abstract class BaseActivity extends FragmentActivity {

    protected int mOrientation;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏navigation
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOrientation = getResources().getConfiguration().orientation;

        int layout;
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            layout = getLayout_s();
        } else {
            layout = getLayout_h();
        }
        setContentView(layout);

        initView();

        initData();
    }

    protected abstract int getLayout_h();

    protected abstract int getLayout_s();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View> T fv(@IdRes int id) {
        return findViewById(id);
    }

    protected void replaceFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    protected void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(id, fragment).commit();
    }

    public static void showExitDialog(Activity act, DialogInterface.OnClickListener cancel, DialogInterface.OnClickListener confirm) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("是否退出应用？");
        alertDialog.setNegativeButton("隐藏到后台", cancel);
        alertDialog.setPositiveButton("退出", confirm);
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    //密码弹窗
    protected void inputPwd(final Runnable runnable) {
        final String spPwd = SpUtils.getStr(SpUtils.MENU_PWD, "123456");
        if (TextUtils.isEmpty(spPwd)) {
            runnable.run();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_input_pwd);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_edt_shake);
        final View rootView = dialog.findViewById(R.id.ll_input_pwd);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_input_confirm);
        final EditText edtPwd = (EditText) dialog.findViewById(R.id.edt_input_pwd);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = edtPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    edtPwd.setError("请输入管理密码");
                    rootView.startAnimation(animation);
                    return;
                }

                if (!TextUtils.equals(pwd, spPwd)) {
                    edtPwd.setError("密码错误，请重新输入");
                    rootView.startAnimation(animation);
                    return;
                }

                if (runnable != null) {
                    runnable.run();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        window.setLayout(width / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    //密码弹窗
    protected void inputUrl(String url, final Consumer<String> consumer) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_input_url);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_edt_shake);
        final View rootView = dialog.findViewById(R.id.ll_input_pwd);
        final EditText edtPwd = dialog.findViewById(R.id.edt_input_url);
        Button btnConfirm = dialog.findViewById(R.id.btn_input_confirm);
        edtPwd.setText(url);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(url)) {
                    edtPwd.setError("请输入URL地址");
                    rootView.startAnimation(animation);
                    return;
                }

                if (consumer != null) {
                    try {
                        consumer.accept(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        window.setLayout(width / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    protected void showProgressDialog(String msg) {
        dismissProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
