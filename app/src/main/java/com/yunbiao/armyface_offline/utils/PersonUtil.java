package com.yunbiao.armyface_offline.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.Sign;
import com.yunbiao.armyface_offline.db.User;
import com.yunbiao.faceview.FaceManager;

import net.lingala.zip4j.util.Zip4jUtil;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonUtil {
    private static final String TAG = "PersonUtil";
    private static Callback callback;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static boolean isRunning = false;

    private static final String IMPORT_INFO_NAME = "person_info.txt";
    private static final String EXPORT_INFO_NAME = "sign_info.txt";

    public static void start(final String path, Callback cb) {
        callback = cb;
        if (isRunning) {
            callback.onFailed("正在处理，请稍等");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                checkZip(path);
                isRunning = false;
            }
        }).start();
    }

    //检查压缩包
    private static void checkZip(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            if (callback != null) {
                callback.onFailed("目录不存在：" + fileDir.getPath());
            }
            return;
        }
        File[] peoples = fileDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("PERSON");
            }
        });
        Log.e(TAG, "checkZip: 检测到压缩包：" + (peoples == null ? 0 : peoples.length));
        if (peoples == null || peoples.length <= 0) {
            if (callback != null) {
                callback.onFailed("未检测到压缩包");
            }
            return;
        }

        File zip = null;
        Date mDate = null;
        for (File people : peoples) {
            String name = people.getName();
            String dateTime = name.substring(7, 21);
            try {
                Date parse = dateFormat.parse(dateTime);
                if (mDate == null || parse.after(mDate)) {
                    mDate = parse;
                    zip = people;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (zip == null) {
            if (callback != null) {
                callback.onFailed("未检测到压缩包");
            }
            return;
        }
        Log.e(TAG, "checkZip: 最新的压缩包:  " + zip.getPath());

        //解压ZIP
        unZip(zip);
    }

    //解压缩包
    private static void unZip(File zipFile) {
        try {
            Log.e(TAG, "unZip: 检查Zip包：" + zipFile.getPath());
            List<File> fileList = ZipUtils.GetFileList(zipFile.getPath(), false, true);
            if(fileList != null){
                for (File file : fileList) {
                    Log.e(TAG, "unZip: " + file.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        boolean b = ZipUtils.UnZipFolder(zipFile.getPath(), Path.PERSON_PATH_TEMP);
        if(!b){
            if (callback != null) {
                callback.onFailed("解压失败");
            }
            return;
        }

        //加载文件
        File tempDir = new File(Path.PERSON_PATH_TEMP);
        HashMap<String, File> fileMap = new HashMap<>();
        listFile(tempDir, fileMap);
        Log.e(TAG, "unZip: 共有文件：" + fileMap.size());

        Log.e(TAG, "onFinish: 解压完毕后检查文件");
        for (Map.Entry<String, File> stringFileEntry : fileMap.entrySet()) {
            Log.e(TAG, "onFinish: " + stringFileEntry.getValue());
        }

        //检查person文件夹
        File personDir = new File(Path.PERSON_PATH);
        if (!personDir.exists()) {
            boolean mkdirs = personDir.mkdirs();
            Log.e(TAG, "unZip: 创建person目录：" + mkdirs);
        }

        int num = 0;

        //拷贝文件
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            File file = entry.getValue();
            try {
                FileUtils.copyFileToDirectory(file, new File(Path.PERSON_PATH));
                num++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "unZip: 共拷贝文件：" + num);

        //解压后把所有文件复制到别的地方
        loadPersonInfo();


        /*ZipManager.unzip(zipFile.getPath(), Path.PERSON_PATH_TEMP, new IZipCallback() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart: 开始解压");
            }

            @Override
            public void onProgress(int percentDone) {
                Log.e(TAG, "onStart: 解压进度：" + percentDone);
                if (callback != null) {
                    callback.progress(percentDone);
                }
            }

            @Override
            public void onFinish(boolean success) {
                Log.e(TAG, "unZip: 解压结果：" + success);
                if (!success) {
                    if (callback != null) {
                        callback.onFailed("解压失败");
                    }
                    return;
                }

                //加载文件
                File tempDir = new File(Path.PERSON_PATH_TEMP);
                HashMap<String, File> fileMap = new HashMap<>();
                listFile(tempDir, fileMap);
                Log.e(TAG, "unZip: 共有文件：" + fileMap.size());

                Log.e(TAG, "onFinish: 解压完毕后检查文件");
                for (Map.Entry<String, File> stringFileEntry : fileMap.entrySet()) {
                    Log.e(TAG, "onFinish: " + stringFileEntry.getValue());
                }


//                File personInfo = fileMap.get(IMPORT_INFO_NAME);
//                if (personInfo == null) {
//                    if (callback != null) {
//                        callback.onFailed("未检测到person_info.txt，请检查");
//                    }
//                    return;
//                }

                //检查person文件夹
                File personDir = new File(Path.PERSON_PATH);
                if (!personDir.exists()) {
                    boolean mkdirs = personDir.mkdirs();
                    Log.e(TAG, "unZip: 创建person目录：" + mkdirs);
                }

                int num = 0;

                //拷贝文件
                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    File file = entry.getValue();
                    try {
                        FileUtils.copyFileToDirectory(file, new File(Path.PERSON_PATH));
                        num++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "unZip: 共拷贝文件：" + num);

                //解压后把所有文件复制到别的地方
                loadPersonInfo();
            }
        });*/
    }

    private static void loadPersonInfo() {
        File personDir = new File(Path.PERSON_PATH);
        HashMap<String, File> fileMap = new HashMap<>();
        listFile(personDir, fileMap);//检查所有文件生成Map

        Log.e(TAG, "loadPersonInfo: 拷贝完毕后检查文件");
        for (Map.Entry<String, File> stringFileEntry : fileMap.entrySet()) {
            Log.e(TAG, "loadPersonInfo: " + stringFileEntry.getValue());

        }

        File personInfo = fileMap.get(IMPORT_INFO_NAME);//取出person_info.txt
        Log.e(TAG, "loadPersonInfo: person_info.txt文件：" + (personInfo != null ? personInfo.getPath() : "NULL"));

        //读取person_info.txt
        String personInfoStr = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            FileInputStream fis = new FileInputStream(personInfo);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                stringBuffer.append(new String(buf, 0, len));
            }
            fis.close();
            bis.close();
            personInfoStr = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(personInfoStr)) {
            if (callback != null) {
                callback.onFailed("读取person_info.txt失败，请检查");
            }
            return;
        }

        List<User> userList = null;
        try {
            userList = new Gson().fromJson(personInfoStr, new TypeToken<List<User>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userList == null) {
            if (callback != null) {
                callback.onFailed("解析person_info.txt失败，请检查");
            }
            return;
        }

        //解析person_info.txt
        Log.e(TAG, "loadPersonInfo: 共有人员：" + (userList == null ? 0 : userList.size()));
        if (userList.size() <= 0) {
            if (callback != null) {
                callback.onFailed("没有可同步人员，同步结束");
            }
            return;
        }

        compareDB(userList, fileMap);

        //删除缓存文件夹
        File tempDir = new File(Path.PERSON_PATH_TEMP);
        ZipUtils.delAllFile(tempDir);

        if (callback != null) {
            callback.onComplete(null);
        }
    }

    private static void compareDB(List<User> newList, HashMap<String, File> fileMap) {
        Log.e(TAG, "compareDB: 新数据：" + newList.size());
        //生成新数据Map
        HashMap<String, User> newMap = new HashMap<>();
        if (newList != null) {
            for (User user : newList) {
                File file = fileMap.get(user.getIcon());
                if (file == null) {
                    continue;
                }
                user.setIcon(file.getPath());
                newMap.put(user.getUserCode(), user);
            }
        }
        Log.e(TAG, "compareDB: 生成新数据：" + newMap.size());

        //对比删除
        /*Map<String, File> allFaceMap = FaceManager.getInstance().getAllFaceMap();
        List<User> oldList = DaoManager.get().queryAll(User.class);
        if (oldList != null) {
            for (User user : oldList) {
                String userCode = user.getUserCode();
                if (newMap.containsKey(userCode)) {//如果新map里包含这个人
                    continue;
                }

                long deleteL = DaoManager.get().delete(user);//删除数据库
                boolean isDelFile;
                if (allFaceMap.containsKey(userCode)) {//如果人脸库包含这个人
                    File file = allFaceMap.get(userCode);
                    isDelFile = file.delete();//删除文件
                } else {
                    isDelFile = true;
                }
                Log.e(TAG, "compareDB: 删除：" + deleteL + " --- " + isDelFile);
            }
        }*/

        Log.e(TAG, "compareDB: 生成新数据：" + newMap.size());
        //添加入库
        Set<Map.Entry<String, User>> entries = newMap.entrySet();
        for (Map.Entry<String, User> entry : entries) {
            User value = entry.getValue();
            String userCode = entry.getKey();
            long l = DaoManager.get().addOrUpdate(value);
            boolean b = FaceManager.getInstance().addUser(userCode, value.getIcon());
            Log.e(TAG, "compareDB: 添加入库：" + l + " --- " + b);
        }
    }

    private static void listFile(File dir, HashMap<String, File> fileMap) {
        File[] files = dir.listFiles();   //列出所有的子文件
        for (File file : files) {
            if (file.isFile()) {
                fileMap.put(file.getName(), file);
            } else if (file.isDirectory()) {
                listFile(file, fileMap);//递归遍历
            }
        }
    }

    public interface Callback {
        void progress(int percent);

        void onFailed(String msg);

        void onComplete(File file);
    }

    /***
     * 导出签到记录
     * @param callback
     */
    public static void exportRecord(final Callback callback) {
        if (isRunning) {
            callback.onFailed("正在处理，请稍等");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;

                Log.e(TAG, "run: 导出线程开启");

                List<Sign> signs = DaoManager.get().queryAll(Sign.class);
                if (signs == null || signs.size() <= 0) {
                    callback.onFailed("无可导出记录");
                    isRunning = false;
                    return;
                }
                Log.e(TAG, "run: 所有数据：" + signs.size());

                //生成文件列表
                List<SignModel> signModels = new ArrayList<>();
                List<String> fileList = new ArrayList<>();
                for (Sign sign : signs) {
                    SignModel signModel = new SignModel();
                    signModel.setSign(sign);
                    signModels.add(signModel);
                    Log.e(TAG, "run: " + signModel.toString());
                    if (TextUtils.equals("1", sign.getSignType())) {
                        fileList.add(sign.getSignIcon());
                    }
                }
                Log.e(TAG, "run: 共有记录：" + signModels.size());
                Log.e(TAG, "run: 共有图片：" + fileList.size());

                //生成导出文件夹
                final File exportDir = new File(Path.ROOT_PATH + File.separator + "SIGN_INFO_" + dateFormat.format(new Date()));
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                Log.e(TAG, "run: 需要导出的目录：" + exportDir.getPath());

                //输出txt文件
                String signsStr = new Gson().toJson(signModels);
                try {
                    byte[] bytes = signsStr.getBytes("UTF-8");
                    FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(exportDir, EXPORT_INFO_NAME));
                    fileOutputStream.write(bytes, 0, bytes.length);
                    fileOutputStream.close();
                    Log.e(TAG, "run: 拷贝person_info.txt完成");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: 拷贝person_info.txt失败：" + (e == null ? "NULL" : e.getMessage()));
                    callback.onFailed("拷贝文件失败");
                }

                File exportImageDir = new File(exportDir, "image");
                if (!exportImageDir.exists()) {
                    exportImageDir.mkdirs();
                }

                //拷贝文件列表
                if (fileList.size() > 0) {
                    for (String iconPath : fileList) {
                        try {
                            FileUtils.copyFileToDirectory(new File(iconPath), exportImageDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e(TAG, "run: 没有图片需要拷贝");
                }

                final File zipFile = new File(Path.EXPORT_SIGN_PATH + File.separator + exportDir.getName() + ".zip");
                Log.e(TAG, "run: 压缩到文件：" + zipFile.getPath());
                ZipManager.zip(exportDir.getPath(), zipFile.getPath(), new IZipCallback() {
                    @Override
                    public void onStart() {
                        Log.e(TAG, "onFinish: 压缩开始");
                    }

                    @Override
                    public void onProgress(int percentDone) {
                        Log.e(TAG, "onFinish: 压缩进度：" + percentDone);
                        if (callback != null) {
                            callback.progress(percentDone);
                        }
                    }

                    @Override
                    public void onFinish(boolean success) {
                        Log.e(TAG, "onFinish: 压缩结束：" + success);
                        if (success) {
                            Log.e(TAG, "exportRecord: 压缩完成：" + zipFile.getPath());
                            callback.onComplete(zipFile);
                        } else {
                            callback.onFailed("压缩文件失败");
                        }

                        Log.e(TAG, "onFinish: 删除导出目录");
                        ZipUtils.delAllFile(exportDir);
                    }
                });

                isRunning = false;
            }
        }).start();
    }

    static class SignModel {
        private String userCode;
        private String userName;
        private String signIcon;
        private String signType;
        private String companyCode;
        private String companyName;
        private String deptCode;
        private String icNo;
        private String startTime;
        private String endTime;
        private String device_code;

        @Override
        public String toString() {
            return "SignModel{" +
                    "userCode='" + userCode + '\'' +
                    ", userName='" + userName + '\'' +
                    ", signIcon='" + signIcon + '\'' +
                    ", signType='" + signType + '\'' +
                    ", companyCode='" + companyCode + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", deptCode='" + deptCode + '\'' +
                    ", icNo='" + icNo + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", device_code='" + device_code + '\'' +
                    '}';
        }

        public void setSign(Sign sign) {
            userCode = sign.getUserCode();
            userName = sign.getUserName();
            signType = sign.getSignType();

            if (TextUtils.equals("1", signType)) {
                signIcon = new File(sign.getSignIcon()).getName();
            } else {
                signIcon = "";
            }

            companyCode = sign.getCompanyCode();
            companyName = sign.getCompanyName();
            deptCode = sign.getDeptCode();
            icNo = sign.getIcNo();
            startTime = sign.getStartTime();
            endTime = sign.getEndTime();
            device_code = sign.getDevice_code();
        }
    }
}
