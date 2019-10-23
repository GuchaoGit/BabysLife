package com.guc.babyslife.utils;

import com.guc.babyslife.app.Logger;
import com.guc.babyslife.app.Profile;
import com.guc.babyslife.model.BackupInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guc on 2019/10/22.
 * 描述：文件处理工具
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    private static SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static BackupInfo getBackupInfo(String fileName) {
        File file = new File(Profile.getInstance().getBackupPath(), fileName);
        boolean hasBackup = file.exists();
        BackupInfo info = new BackupInfo();
        info.hasBackup = hasBackup;
        if (hasBackup) {
            info.path = file.getParent();
            info.fileName = file.getName();
            info.time = mSdf.format(new Date(file.lastModified()));
        }
        return info;
    }

    public static String writeStr2File(String data) {
        String path = Profile.getInstance().getBackupPath();
        if (createFile(path, Profile.FN_BABY)) {
            File file = new File(path, Profile.FN_BABY);
            try {
                OutputStreamWriter oos = new OutputStreamWriter(new FileOutputStream(file));
                oos.write(data);
                oos.flush();
                oos.close();
                return file.getName();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String readFile2Str(File f) {
        StringBuilder sb = new StringBuilder();
        if (f != null && f.exists()) {
            FileInputStream inputStream = null;
            BufferedReader br = null;
            try {
                inputStream = new FileInputStream(f);
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (br != null) br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static boolean createFile(String path, String fileName) {
        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }
        boolean sucess = false;
        try {
            file.getParentFile().mkdirs();
            sucess = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sucess;
    }

    /**
     * @Name creatDir
     * @Description 判断目录是否存在，不存在则创建
     **/
    public static void creatDir(String path) {

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    /**
     * 删除文件
     *
     * @param f file
     * @return true:删除成功
     */
    public static boolean deleteFile(File f) {
        if (f != null && f.exists() && !f.isDirectory()) {
            return f.delete();
        }
        return false;
    }

    /**
     * 删除目录
     *
     * @param f 目录file
     */
    public static void deleteDir(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            for (File file : f.listFiles()) {
                if (file.isDirectory())
                    deleteDir(file);
                file.delete();
            }
            f.delete();
        }
    }

    /**
     * 删除目录
     *
     * @param f path
     */
    public static void deleteDir(String f) {
        if (f != null && f.length() > 0) {
            deleteDir(new File(f));
        }
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dst
     * @return true 成功
     */
    public static boolean copyFile(File src, File dst) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        boolean success = false;
        try {
            if (!dst.exists()) {
                dst.getParentFile().mkdirs();
                dst.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            success = true;
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
            dst.delete();
            Logger.e(TAG, "文件复制失败");
        } finally {
            try {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * 格式化单位
     *
     * @param size b
     * @return 格式化后的单位 K/M/GB
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 计算文件夹的大小
     *
     * @param file
     * @return size 单位:b
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
