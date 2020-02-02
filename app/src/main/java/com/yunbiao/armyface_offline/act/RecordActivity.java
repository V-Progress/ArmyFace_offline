package com.yunbiao.armyface_offline.act;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.adapter.SignAdapter;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.Sign;
import com.yunbiao.armyface_offline.model.ExportResponse;
import com.yunbiao.armyface_offline.utils.PersonUtil;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.armyface_offline.utils.ZipUtils;
import com.yunbiao.armyface_offline.utils.xutil.MyXutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;

public class RecordActivity extends BaseActivity {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ListView lvList;
    private Button btnDate;
    private List<Sign> signList = new ArrayList<>();
    private SignAdapter signAdapter;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_record;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {
        fv(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvList = fv(R.id.lv_sign_List);
        signAdapter = new SignAdapter(this, signList);
        lvList.setAdapter(signAdapter);

        btnDate = fv(R.id.btn_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });
    }

    @Override
    protected void initData() {
        Date date = new Date();
        String currDate = dateFormat.format(date);
        btnDate.setText(currDate);

        loadData(currDate);
    }

    private void loadData(String date) {
        signList.clear();
        List<Sign> signs = DaoManager.get().querySignByDate(date);
        if (signs == null) {
            signs = new ArrayList<>();
        }
        Collections.reverse(signs);
        signList.addAll(signs);
        signAdapter.notifyDataSetChanged();
    }

    private void showCalender() {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //年份
                String yearStr = year + "";

                //月份（自动补0）
                String monthStr;
                int realMonth = month + 1;
                monthStr = realMonth < 10 ? ("0" + realMonth) : ("" + realMonth);

                //日（自动补0）
                String dayStr = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);

                //转换成真实日期
                String date = yearStr + "-" + monthStr + "-" + dayStr;
                btnDate.setText(date);

                loadData(date);
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void exportLocal(View view) {
        showProgressDialog("正在导出...");
        PersonUtil.exportRecord(new PersonUtil.Callback() {
            @Override
            public void progress(int percent) {

            }

            @Override
            public void onFailed(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        UIUtils.showTitleTip(RecordActivity.this, msg);
                    }
                });
            }

            @Override
            public void onComplete(final File file) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        UIUtils.showTitleTip(RecordActivity.this, "导出完成\n文件路径：" + file.getPath());
                    }
                });
            }
        });
    }

    private static final String TAG = "RecordActivity";

    public void exportRemote(View view) {
//        "http://39.107.125.63:8081/sendsignlist.json"
        inputUrl("http://192.168.43.226:8081/sendsignlist.json", new Consumer<String>() {
            @Override
            public void accept(final String url) throws Exception {

                showProgressDialog("正在导出，请稍等...");
                PersonUtil.exportRecord(new PersonUtil.Callback() {
                    @Override
                    public void progress(int percent) {

                    }

                    @Override
                    public void onFailed(String msg) {
                        UIUtils.showTitleTip(RecordActivity.this, msg);
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete(final File file) {
                        OkHttpUtils.post()
                                .url(url)
                                .addFile("file", file.getName(), file).build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception ex, int id) {
                                        Log.e(TAG, "onError: " + url + " --- " +(ex == null ? "NULL" : ex.getMessage()));
                                        UIUtils.showTitleTip(RecordActivity.this, "导出失败：" + (ex == null ? "NULL" : ex.getMessage()));
                                    }

                                    @Override
                                    public void onAfter(int id) {
                                        super.onAfter(id);
                                        if (file.exists()) {
                                            file.delete();
                                        }

                                        dismissProgressDialog();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Log.e(TAG, "onSuccess: " + response);
                                        if (TextUtils.isEmpty(response)) {
                                            UIUtils.showTitleTip(RecordActivity.this, "导出失败，响应为空");
                                            return;
                                        }

                                        ExportResponse exportResponse = new Gson().fromJson(response, ExportResponse.class);
                                        if (exportResponse.getCode() != 1) {
                                            Log.e(TAG, "导出失败：" + (TextUtils.isEmpty(exportResponse.getMsg()) ? "未知错误" : exportResponse.getMsg()));
                                            UIUtils.showTitleTip(RecordActivity.this, "导出失败，" + (TextUtils.isEmpty(exportResponse.getMsg()) ? "未知错误" : exportResponse.getMsg()));
                                            return;
                                        }

                                        UIUtils.showTitleTip(RecordActivity.this, "导出成功");
                                    }
                                });
                    }
                });
            }
        });

    }
}
