package com.yunbiao.armyface_offline.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by YuShuangPing on 2018/11/11.
 */
public class ZipUtils {
    public static final String TAG = "ZIP";

    public ZipUtils() {

    }

    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     */
    public static boolean UnZipFolder(String zipFileString, String outPathString) {
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    //获取部件的文件夹名
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();
                } else {
                    File file = new File(outPathString + File.separator + szName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    // 获取文件的输出流
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 读取（字节）字节到缓冲区
                    while ((len = inZip.read(buffer)) != -1) {
                        // 从缓冲区（0）位置写入（字节）字节
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void UnZipFolder(String zipFileString, String outPathString, String szName) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            //szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Log.e(TAG, outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()) {
                    Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 解压完成的Zip路径
     */
    public static boolean ZipFolder(String srcFileString, String zipFileString) {

        //创建ZIP
        try {
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
            //创建文件
            File file = new File(srcFileString);
            //压缩
            Log.e(TAG, "---->" + file.getParent() + "===" + file.getAbsolutePath());
            ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
            //完成和关闭
            outZip.finish();
            outZip.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        Log.e(TAG, "folderString:" + folderString + "\n" +
                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }

    /**
     * 返回zip的文件输入流
     *
     * @param zipFileString zip的名称
     * @param fileString    ZIP的文件名
     * @return InputStream
     */
    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);
        return zipFile.getInputStream(zipEntry);
    }

    /**
     * 返回ZIP中的文件列表（文件和文件夹）
     *
     * @param zipFileString  ZIP的名称
     * @param bContainFolder 是否包含文件夹
     * @param bContainFile   是否包含文件
     * @return
     */
    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // 获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }
            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }


    /**
     * 文件复制，将指定的一个文件内容复制到另一个指定的文件中
     *
     * @param sourceFile 待复制的文件
     * @param targetFile 目标文件
     * @return 复制是否成功
     */
    public static boolean copyFile(String sourceFile, String targetFile) {
        Log.e(TAG, "copyFile: " + sourceFile + " 拷贝到 " + targetFile);
        if (!isExistFile(sourceFile)) return false;
        Boolean flag = true;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = new File(sourceFile);
        try {
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int count;
            while ((count = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            closeIOStream(inputStream, outputStream);
        }
        return flag;
    }

    /**
     * 关闭IO流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    public static void closeIOStream(InputStream inputStream, OutputStream outputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件路径指向的文件是否存在
     *
     * @param filePath 文件路径
     * @return true 文件存在 false 文件不存在
     */
    public static Boolean isExistFile(String filePath) {

        if (filePath == null) return false;

        File file = new File(filePath);
        if (!file.exists()) return false;
        if (!file.isFile()) return false;
        return true;
    }

    /**
     * 保存bitmap到本地
     *
     * @return
     */
    public static File saveBitmap(String path, long time, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        long start = System.currentTimeMillis();
        File filePic;
        try {
            //格式化时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String sdfTime = sdf.format(time);

            filePic = new File(path + File.separator + sdfTime + ".jpg");
            if (!filePic.exists()) {
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        long end = System.currentTimeMillis();
        Log.e("Compress", "saveBitmap: 压缩耗时----- " + (end - start));
        return filePic;
    }

    /**
     * 删除文件或文件夹
     *
     * @param directory
     */
    public static void delAllFile(File directory) {
        if (!directory.isDirectory()) {
            directory.delete();
        } else {
            File[] files = directory.listFiles();

            // 空文件夹
            if (files.length == 0) {
                directory.delete();
                return;
            }

            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delAllFile(file);
                } else {
                    file.delete();
                }
            }

            // 删除文件夹本身
            directory.delete();
        }
    }

    public static class ZipCompress {
        private String zipFileName;      // 目的地Zip文件
        private String sourceFileName;   //源文件（带压缩的文件或文件夹）

        public ZipCompress(String zipFileName, String sourceFileName) {
            this.zipFileName = zipFileName;
            this.sourceFileName = sourceFileName;
        }

        public void zip() throws Exception {
            //File zipFile = new File(zipFileName);
            Log.e(TAG, "zip: 压缩中...");

            //创建zip输出流
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));

            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(out);

            File sourceFile = new File(sourceFileName);

            //调用函数
            compress(out, bos, sourceFile, sourceFile.getName());

            bos.close();
            out.close();
            Log.e(TAG, "zip: 压缩完成... " + zipFileName);
        }

        public void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
            //如果路径为目录（文件夹）
            if (sourceFile.isDirectory()) {

                //取出文件夹中的文件（或子文件夹）
                File[] flist = sourceFile.listFiles();

                if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                {
                    System.out.println(base + "/");
                    out.putNextEntry(new ZipEntry(base + "/"));
                } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                {
                    for (int i = 0; i < flist.length; i++) {
                        compress(out, bos, flist[i], base + "/" + flist[i].getName());
                    }
                }
            } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            {
                out.putNextEntry(new ZipEntry(base));
                FileInputStream fos = new FileInputStream(sourceFile);
                BufferedInputStream bis = new BufferedInputStream(fos);

                int tag;
                System.out.println(base);
                //将源文件写入到zip文件中
                while ((tag = bis.read()) != -1) {
                    out.write(tag);
                }
                bis.close();
                fos.close();

            }
        }
    }
}