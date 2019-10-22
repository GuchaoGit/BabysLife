package com.guc.babyslife.utils;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guc on 2019/10/22.
 * 描述：文件处理工具
 */
public class FileUtils {
    private static SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static BackupInfo getBackupInfo() {
        File file = new File(Profile.getInstance().getBackupPath(), Profile.FN_BABY);
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
}
