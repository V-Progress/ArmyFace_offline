package com.yunbiao.armyface_offline.query;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.act.BaseActivity;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.armyface_offline.utils.ReadCardUtils;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.faceview.CompareResult;
import com.yunbiao.faceview.FacePreviewInfo;
import com.yunbiao.faceview.FaceView;

import java.util.List;

public class QueryActivity extends BaseActivity {

    private static final String TAG = "QueryActivity";
    private FaceView faceView;
    private ReadCardUtils readCardUtils;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_query;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_query;
    }

    @Override
    protected void initView() {
        fv(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        faceView = fv(R.id.face_view);
        faceView.setCallback(new FaceView.FaceCallback() {
            @Override
            public void onReady() {

            }

            @Override
            public void onFaceDetection(Boolean hasFace, List<FacePreviewInfo> facePreviewInfoList) {
                if (hasFace) {
                    faceView.removeCallbacks(finishRunnable);
                    faceView.postDelayed(finishRunnable, 8 * 1000);
                }
            }

            @Override
            public void onFaceVerify(CompareResult faceAuth) {
                if (faceAuth != null) {
                    String userName = faceAuth.getUserName();
                    User user = DaoManager.get().queryUserByUserName(userName);
                    if (user == null) {
                        UIUtils.showTitleTip(QueryActivity.this, "查无此人");
                        return;
                    }

                    jumpResult(userName);
                }
            }
        });
        faceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpResult("");
            }
        });
    }

    @Override
    protected void initData() {
        initCardReader();
    }

    private void initCardReader() {
        readCardUtils = new ReadCardUtils();
        readCardUtils.setReadSuccessListener(new ReadCardUtils.OnReadSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                Log.e(TAG, "onScanSuccess: " + barcode);
                User user = DaoManager.get().queryUserByCard(barcode);
                if (user == null) {
                    UIUtils.showTitleTip(QueryActivity.this, "查无此人");
                    return;
                }

                jumpResult(user.getUserCode());
            }
        });
    }

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
    protected void onDestroy() {
        super.onDestroy();
        if (readCardUtils != null) {
            readCardUtils.removeScanSuccessListener();
            readCardUtils = null;
        }
    }

    private void jumpResult(final String userCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(QueryActivity.this, QueryResultActivity.class);
                intent.putExtra("userCode", userCode);
                if (TextUtils.isEmpty(userCode)) {
                    intent.putExtra("userCode", "70ce57f1d819423081c24a7894445649");
                }
//                intent.putExtra("userCode","22725f6cc5cd4b6e8e733b18a7f78b63");
                startActivity(intent);

                faceView.removeCallbacks(finishRunnable);
                finish();
            }
        });
    }

    private Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
}
