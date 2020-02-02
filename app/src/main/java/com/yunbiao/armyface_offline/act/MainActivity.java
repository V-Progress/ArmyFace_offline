package com.yunbiao.armyface_offline.act;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.yunbiao.armyface_offline.APP;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.Sign;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.armyface_offline.utils.KDXFSpeechManager;
import com.yunbiao.armyface_offline.utils.ReadCardUtils;
import com.yunbiao.armyface_offline.fragment.AdsFragment;
import com.yunbiao.armyface_offline.fragment.InsideFragment;
import com.yunbiao.armyface_offline.fragment.RecordFragment;
import com.yunbiao.armyface_offline.utils.SignUtil;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.armyface_offline.utils.VipDialogManager;
import com.yunbiao.armyface_offline.utils.ZipUtils;
import com.yunbiao.faceview.CompareResult;
import com.yunbiao.faceview.DisplayOrientationEvent;
import com.yunbiao.faceview.FaceManager;
import com.yunbiao.faceview.FacePreviewInfo;
import com.yunbiao.faceview.FaceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FaceView faceView;
    private ReadCardUtils readCardUtils;
    private AdsFragment adsFragment;
    private RecordFragment recordFragment;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_main_h;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        faceView = fv(R.id.face_view);
        faceView.setCallback(faceCallback);
        fv(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPwd(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SystemActivity.class));
                    }
                });
            }
        });

        recordFragment = new RecordFragment();
        replaceFragment(R.id.fl_record, recordFragment);

        adsFragment = new AdsFragment();
        addFragment(R.id.fl_ads, adsFragment);

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            InsideFragment insideFragment = new InsideFragment();
            replaceFragment(R.id.fl_info, insideFragment);
        }
    }

    @Override
    protected void initData() {
        KDXFSpeechManager.instance().init(this);

        readCardUtils = new ReadCardUtils();
        readCardUtils.setReadSuccessListener(new ReadCardUtils.OnReadSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                Log.e(TAG, "onScanSuccess: " + barcode);
                Sign sign = SignUtil.checkCardSign(mOrientation, barcode);
                if (sign == null) {
                    UIUtils.showTitleTip(MainActivity.this, "查无此人");
                    return;
                }

                //弹窗提示
                VipDialogManager.showVipDialog(MainActivity.this, sign);
                KDXFSpeechManager.instance().playText(TextUtils.equals("1", sign.getStatus()) ? sign.getUserName() : "用户已被禁用");

                //用户状态是启用时再更新数据库
                if (TextUtils.equals("1", sign.getStatus())) {//如果是禁用
                    DaoManager.get().addOrUpdate(sign);
                    if (recordFragment != null) {
                        recordFragment.updateList(sign);
                    }
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(DisplayOrientationEvent event){
        Log.e(TAG, "update: 收到摄像头更新事件");
        faceView.changeAngle();
    }

    private FaceView.FaceCallback faceCallback = new FaceView.FaceCallback() {
        @Override
        public void onReady() {
            FaceManager.getInstance().init(MainActivity.this, Path.FEATURE_PATH);
        }

        @Override
        public void onFaceDetection(Boolean hasFace, List<FacePreviewInfo> facePreviewInfoList) {
            if (hasFace && adsFragment != null) {
                adsFragment.detectFace();
            }
        }

        @Override
        public void onFaceVerify(CompareResult faceAuth) {
            Sign sign = SignUtil.checkFaceSign(mOrientation, faceAuth);
            if (sign == null) {
                UIUtils.showTitleTip(MainActivity.this, "查无此人");
                return;
            }

            Bitmap headImgByte = faceView.getHeadImgByte(faceAuth.getTrackId());
            if (headImgByte != null) {
                File file = ZipUtils.saveBitmap(Path.RECORD_PATH, sign.getTime(), headImgByte);
                sign.setSignIcon(file.getPath());
            }

            VipDialogManager.showVipDialog(MainActivity.this, sign);
            KDXFSpeechManager.instance().playText(TextUtils.equals("1", sign.getStatus()) ? sign.getUserName() : "用户已被禁用");

            if (TextUtils.equals("1", sign.getStatus())) {
                long l = DaoManager.get().addOrUpdate(sign);
                Log.e(TAG, "onFaceVerify: 保存入库：" + l);
                if (recordFragment != null) {
                    recordFragment.updateList(sign);
                }
            }
        }
    };

    private static final String TAG = "MainActivity";

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (ReadCardUtils.isInputFromReader(this, event)) {
            if (readCardUtils != null) {
                readCardUtils.resolveKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        faceView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        faceView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        faceView.destory();
        if (readCardUtils != null) {
            readCardUtils.removeScanSuccessListener();
            readCardUtils = null;
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputPwd(new Runnable() {
                    @Override
                    public void run() {
                        moveTaskToBack(true);
                    }
                });
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputPwd(new Runnable() {
                    @Override
                    public void run() {
                        APP.exit();
                    }
                });
            }
        });
    }

}
