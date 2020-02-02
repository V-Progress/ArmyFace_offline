package com.yunbiao.armyface_offline.act;


import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.adapter.DepartAdapter;
import com.yunbiao.armyface_offline.adapter.PersonAdapter;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.armyface_offline.model.ImportResponse;
import com.yunbiao.armyface_offline.utils.PersonUtil;
import com.yunbiao.armyface_offline.utils.SpUtils;
import com.yunbiao.armyface_offline.utils.UIUtils;
import com.yunbiao.armyface_offline.utils.ZipUtils;
import com.yunbiao.armyface_offline.utils.xutil.MyXutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;

public class ManageActivity extends BaseActivity {
    private static final String TAG = "ManageActivity";

    private ListView lvList;
    private Spinner spDepart;

    private List<User> allUserList;
    private List<String> departList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    private PersonAdapter personAdapter;
    private DepartAdapter departAdapter;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_manage;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_manage;
    }

    @Override
    protected void initView() {
        fv(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvList = fv(R.id.lv_employ_List);

        personAdapter = new PersonAdapter(this, userList);
        lvList.setAdapter(personAdapter);

        spDepart = fv(R.id.sp_depart);
        Drawable drawable = getResources().getDrawable(R.drawable.shape_employ_button);
        spDepart.setPopupBackgroundDrawable(drawable);
        spDepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: " + departList.get(position));
                loadUserData(departList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        departAdapter = new DepartAdapter(this, departList);
        spDepart.setAdapter(departAdapter);
    }

    @Override
    protected void initData() {
        allUserList = DaoManager.get().queryAll(User.class);
        if (allUserList == null) {
            allUserList = new ArrayList<>();
        }

        Log.e(TAG, "initData: 所有用户：" + allUserList.size());

        initDepart();
    }

    private void initDepart() {
        departList.clear();
        departList.add("全部");
        for (User user : allUserList) {
            if (departList.contains(user.getDeptName())) {
                continue;
            }
            departList.add(user.getDeptName());
        }
        departAdapter.notifyDataSetChanged();

        loadUserData(departList.get(0));
    }

    private void loadUserData(String depart) {
        userList.clear();
        if (TextUtils.equals("全部", depart)) {
            userList.addAll(allUserList);
        } else {
            for (User user : allUserList) {
                if (TextUtils.equals(user.getDeptName(), depart)) {
                    userList.add(user);
                }
            }
        }

        personAdapter.notifyDataSetChanged();
    }

    public void importLocal(View view) {
        showProgressDialog("正在导入...");
        loadPerson(Environment.getExternalStorageDirectory().getPath());
    }

    public void importRemote(View view) {
        //"http://39.107.125.63:8081/personlist.json"
        String url = SpUtils.getStr(SpUtils.IMPORT_URL,"http://192.168.43.226:8081/personlist.json");
        inputUrl(url, new Consumer<String>() {
            @Override
            public void accept(final String url) throws Exception {

                SpUtils.saveStr(SpUtils.IMPORT_URL, url);

                OkHttpUtils.post().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        //请求获取Zip包
                        showProgressDialog("正在获取Zip包...");
                    }

                    @Override
                    public void onError(Call call, Exception ex, int id) {
                        dismissProgressDialog();
                        UIUtils.showTitleTip(ManageActivity.this, "获取失败：" + (ex == null ? "NULL" : ex.getMessage()) + "\n" + url);
                    }

                    @Override
                    public void onResponse(String result, int id) {
                        Log.e(TAG, "onSuccess: 获取结果：" + result);
                        if (TextUtils.isEmpty(result)) {
                            UIUtils.showTitleTip(ManageActivity.this, "远程导入失败" + "\n" + url);
                            return;
                        }
                        ImportResponse importResponse = new Gson().fromJson(result, ImportResponse.class);
                        if (importResponse.getCode() != 1) {
                            UIUtils.showTitleTip(ManageActivity.this, "远程导入失败，" + (TextUtils.isEmpty(importResponse.getMsg()) ? "未知错误" : importResponse.getMsg()) + "\n" + url);
                            return;
                        }

                        ImportResponse.Data data = importResponse.getData();
                        if (data == null) {
                            UIUtils.showTitleTip(ManageActivity.this, "远程导入失败， data为null" + "\n" + url);
                            return;
                        }

                        final String resousePath = data.getResousePath();
                        if (TextUtils.isEmpty(resousePath)) {
                            UIUtils.showTitleTip(ManageActivity.this, "远程导入失败， resousePath为空" + "\n" + url);
                            return;
                        }

                        File downloadTemp = new File(Path.PERSON_DOWNLOAD_TEMP);
                        if (!downloadTemp.exists()) {
                            downloadTemp.mkdirs();
                        }

                        String zipName = resousePath.substring(resousePath.lastIndexOf("/") + 1);
                        Log.e(TAG, "onResponse: 下载地址：" + resousePath);
                        Log.e(TAG, "onResponse: 文件名称：" + zipName);

                        //开始下载Zip包
                        showProgressDialog("正在下载...");
                        MyXutils.getInstance().downLoadFile(resousePath, downloadTemp.getPath() + File.separator + zipName, true, new MyXutils.XDownLoadCallBack() {
                            @Override
                            public void onLoading(long total, long current, boolean isDownloading) {

                            }

                            @Override
                            public void onSuccess(File result) {
                                Log.e(TAG, "onResponse: 下载完毕：" + result.getPath());
                                loadPerson(result.getParent());
                            }

                            @Override
                            public void onError(Throwable ex) {
                                dismissProgressDialog();
                                UIUtils.showTitleTip(ManageActivity.this, "下载Zip包失败：" + (ex == null ? "NULL" : ex.getMessage())  + "\n" + resousePath);
                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
            }
        });
    }

    private void loadPerson(String path) {
        showProgressDialog("正在导入...");
        Log.e(TAG, "loadPerson: 导入路径：" + path);
        PersonUtil.start(path, new PersonUtil.Callback() {
            @Override
            public void progress(int percent) {

            }

            @Override
            public void onFailed(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        UIUtils.showTitleTip(ManageActivity.this, msg);
                    }
                });
            }

            @Override
            public void onComplete(final File file) {
                File personDownloadTemp = new File(Path.PERSON_DOWNLOAD_TEMP);
                ZipUtils.delAllFile(personDownloadTemp);
                Log.e(TAG, "onComplete: 删除临时下载目录");

                File personFile = new File(Path.PERSON_PATH);
                File[] files = personFile.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".txt");
                    }
                });
                if(files != null && files.length > 0){
                    for (File file1 : files) {
                        file1.delete();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        Log.e(TAG, "run: 添加完成：" + (file == null ? "" : file.getPath()));
                        UIUtils.showTitleTip(ManageActivity.this, "导入完成");
                        initData();
                    }
                });
            }
        });
    }


}
