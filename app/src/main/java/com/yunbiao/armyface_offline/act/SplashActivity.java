package com.yunbiao.armyface_offline.act;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.yunbiao.armyface_offline.APP;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.query.WebActivity;
import com.yunbiao.armyface_offline.utils.KDXFSpeechManager;
import com.yunbiao.armyface_offline.utils.SpUtils;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.faceview.Constants;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private Button btnSign;
    private Button btnQuery;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        btnSign = fv(R.id.btn_sign);
        btnQuery = fv(R.id.btn_query);
    }

    @Override
    protected void initData() {
        checkPermission();

        APP.bindProtectedService();
    }

    private void checkPermission() {
        ybPermission = new YBPermission(permissionListener);
        ybPermission.checkPermission(this, PERMISSONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ybPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private YBPermission.PermissionListener permissionListener = new YBPermission.PermissionListener() {
        @Override
        public void onPermissionFailed(String[] objects) {

        }

        @Override
        public void onFinish(boolean isComplete) {
            if (isComplete) {
                activeSDK();
            } else {
                UIUtils.showTitleTip(SplashActivity.this, "权限获取失败，请同意所有权限");
            }
        }
    };

    private void activeSDK() {
        int active = FaceEngine.active(APP.getContext(), Constants.APP_ID, Constants.SDK_KEY);
        if (active == ErrorInfo.MOK || active == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
            UIUtils.showTitleTip(this, "已激活");
            jump();
        } else {
            UIUtils.showTitleTip(this, "激活失败：" + active);
        }
    }

    private void jump() {
        Path.init();

        DaoManager.get().initDb();

        int appVersionId = SpUtils.getIntOrDef(SpUtils.APP_VERSION_ID, -1);
        if (appVersionId == -1) {
            btnQuery.setEnabled(true);
            btnSign.setEnabled(true);
        } else {
            if (appVersionId == 11) {
                jumpSign(null);
            } else {
                jumpQuery(null);
            }
        }
    }

    public void jumpSign(View view) {
        SpUtils.saveInt(SpUtils.APP_VERSION_ID, 11);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void jumpQuery(View view) {
        SpUtils.saveInt(SpUtils.APP_VERSION_ID, 99);
        startActivity(new Intent(this, WebActivity.class));
        finish();
    }

    /***
     * =============================================================================================
     */
    public static String[] PERMISSONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE
            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            , android.Manifest.permission.ACCESS_FINE_LOCATION
            , android.Manifest.permission.ACCESS_COARSE_LOCATION
            , android.Manifest.permission.READ_PHONE_STATE
            , android.Manifest.permission.CAMERA
            /*,Manifest.permission.SYSTEM_ALERT_WINDOW*/};
    private YBPermission ybPermission;

    public static class YBPermission {
        public static final int PERMISSION_REQUEST_CODE = 101;
        private PermissionListener permissionListener;

        public YBPermission(PermissionListener permissionListener) {
            this.permissionListener = permissionListener;
        }

        interface PermissionListener {
            void onPermissionFailed(String[] objects);

            void onFinish(boolean isComplete);
        }

        public void checkPermission(Activity activity, String[] permissions) {
            if (permissions == null || permissions.length <= 0) {
                if (permissionListener != null) {
                    permissionListener.onFinish(false);
                }
                return;
            }

            List<String> deniedPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissionList.add(permission);
                    }
                }
            }

            if (deniedPermissionList.size() > 0) {
                String[] strings = deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(strings, PERMISSION_REQUEST_CODE);
                }
            } else {
                if (permissionListener != null) {
                    permissionListener.onFinish(true);
                }
            }
        }

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (requestCode != PERMISSION_REQUEST_CODE) {
                return;
            }

            if (permissions == null || permissions.length <= 0) {
                if (permissionListener != null) {
                    permissionListener.onFinish(false);
                }
                return;
            }

            List<String> permiList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permiList.add(permissions[i]);
                }
            }
            if (permiList.size() <= 0) {
                if (permissionListener != null) {
                    permissionListener.onFinish(true);
                }
                return;
            }

            if (permissionListener != null) {
                permissionListener.onPermissionFailed(permiList.toArray(new String[permiList.size()]));
            }
        }
    }
}
