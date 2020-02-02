package com.yunbiao.armyface_offline.query;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.yunbiao.armyface_offline.APP;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.act.BaseActivity;
import com.yunbiao.armyface_offline.act.SystemActivity;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.armyface_offline.utils.KDXFSpeechManager;
import com.yunbiao.armyface_offline.utils.ReadCardUtils;
import com.yunbiao.armyface_offline.utils.SpUtils;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.armyface_offline.view.DragFloatActionButton;
import com.yunbiao.faceview.FaceManager;

public class WebActivity extends BaseActivity {

    private DragFloatActionButton dfaBtnSetting;
    private DragFloatActionButton dfaBtnQuery;
    private View flProgress;
    private ReadCardUtils readCardUtils;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_web;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        dfaBtnQuery = fv(R.id.btn_query);
        dfaBtnSetting = fv(R.id.btn_setting);
        flProgress = fv(R.id.fl_progress);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_query:
                        dfaBtnQuery.setEnabled(false);
                        flProgress.setVisibility(View.VISIBLE);
                        goQuery();
                        break;
                    case R.id.btn_setting:
                        goSetting();
                        break;
                }
            }
        };
        dfaBtnQuery.setOnClickListener(clickListener);
        dfaBtnSetting.setOnClickListener(clickListener);
    }

    @Override
    protected void initData() {
        KDXFSpeechManager.instance().init(this);

        initCardReader();

        FaceManager.getInstance().init(this, Path.FEATURE_PATH);
    }

    private static final String TAG = "WebActivity";

    private void initCardReader() {
        readCardUtils = new ReadCardUtils();
        readCardUtils.setReadSuccessListener(new ReadCardUtils.OnReadSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                Log.e(TAG, "onScanSuccess: " + barcode);
                User user = DaoManager.get().queryUserByCard(barcode);
                if (user == null) {
                    UIUtils.showTitleTip(WebActivity.this, "查无此人");
                    return;
                }

                jumpResult(user.getUserCode());
            }
        });
    }

    private void jumpResult(final String userCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WebActivity.this, QueryResultActivity.class);
                intent.putExtra("userCode", userCode);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        flProgress.setVisibility(View.GONE);
        dfaBtnQuery.setEnabled(true);
        dfaBtnSetting.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readCardUtils != null) {
            readCardUtils.removeScanSuccessListener();
            readCardUtils = null;
        }
    }

    public void goQuery() {
        startActivity(new Intent(this, QueryActivity.class));
    }

    public void goSetting() {
        inputPwd(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WebActivity.this, SystemActivity.class));
            }
        });
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
