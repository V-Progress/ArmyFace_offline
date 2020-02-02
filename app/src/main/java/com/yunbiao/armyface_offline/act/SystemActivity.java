package com.yunbiao.armyface_offline.act;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.utils.HeartBeatClient;
import com.yunbiao.armyface_offline.utils.SpUtils;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.faceview.DisplayOrientationEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;

public class SystemActivity extends BaseActivity {

    private Dialog dialog;
    private Button btnAngle;
    private Switch swMirro;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_system;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_system;
    }

    @Override
    protected void initView() {
        if (SpUtils.getInt(SpUtils.APP_VERSION_ID) == 99) {
            fv(R.id.btn_sign_record).setVisibility(View.GONE);
        } else {
            fv(R.id.btn_change_url).setVisibility(View.GONE);
        }

        btnAngle = fv(R.id.btnAngle);
        swMirro = fv(R.id.sw_mirror);

        TextView tvDeviceNo = fv(R.id.tv_device_no);
        tvDeviceNo.setText(HeartBeatClient.getDeviceNo());
        fv(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initSetting();
    }

    @Override
    protected void initData() {
    }

    private void initSetting(){
        //人脸框镜像
        final boolean mirror = SpUtils.isMirror();
        swMirro.setChecked(mirror);
        swMirro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.setMirror(isChecked);
            }
        });

        //摄像头角度
        int angle = SpUtils.getInt(SpUtils.CAMERA_ANGLE);
        btnAngle.setText("摄像头角度:" + angle);
    }

    public void setAngle(final View view) {
        int anInt = SpUtils.getInt(SpUtils.CAMERA_ANGLE);
        if (anInt == 0) {
            anInt = 90;
        } else if (anInt == 90) {
            anInt = 180;
        } else if (anInt == 180) {
            anInt = 270;
        } else {
            anInt = 0;
        }
        ((Button) view).setText("摄像头角度:" + anInt);
        SpUtils.saveInt(SpUtils.CAMERA_ANGLE, anInt);
        EventBus.getDefault().post(new DisplayOrientationEvent());
    }

    public void personManage(View view) {
        startActivity(new Intent(this, ManageActivity.class));
    }

    public void signRecord(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    public void systemSetting(View view) {
        UIUtils.showTitleTip(this, "正在建设中");
    }

    public void changePassword(View view) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_change_pwd);
        TextView tvOrigin = dialog.findViewById(R.id.tv_original_pwd);
        final EditText edtChange = dialog.findViewById(R.id.edt_change_pwd);
        Button btnChangePwd = dialog.findViewById(R.id.btn_change_pwd);

        String str = SpUtils.getStr(SpUtils.MENU_PWD, "123456");
        tvOrigin.setText("当前密码：" + str);

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = edtChange.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    edtChange.setError("请输入密码");
                    return;
                }

                if (pwd.length() < 6) {
                    edtChange.setError("密码太短，请输入6-12位密码");
                    return;
                }

                if (pwd.length() > 12) {
                    edtChange.setError("密码太长，请输入6-12位密码");
                    return;
                }

                SpUtils.saveStr(SpUtils.MENU_PWD, pwd);
                UIUtils.showTitleTip(SystemActivity.this, "修改密码成功");

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void changeUrl(View view) {
        //"http://39.107.125.63:8081/getTrainList?userCode="
        String str = SpUtils.getStr(SpUtils.WEB_URL,"http://192.168.43.226:8081/getTrainList?userCode=");
        inputUrl(str, new Consumer<String>() {
            @Override
            public void accept(String url) throws Exception {
                SpUtils.saveStr(SpUtils.WEB_URL, url);
                UIUtils.showTitleTip(SystemActivity.this, "修改成功");
            }
        });
    }
}
