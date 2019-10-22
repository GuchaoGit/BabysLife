package com.guc.babyslife.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by guc on 2019/10/15.
 * 描述：配置文件
 */
public class Profile {
    public static final String FN_BABY = "babies.bak";
    private static final String TAG = "Profile";
    private static final String ROOT_DIR = "Backup4BabyLife";    // 根目录
    private static Profile mInstance;
    private String mBackupPath;
    public Context mContext;
    static final boolean OPEN_LOG = true;//true : 开启  false :关闭
    private Profile(Context context){
        this.mContext = context.getApplicationContext();
        if (isSDCardExits()) {
            StringBuffer sb = new StringBuffer();
            sb.append(Environment.getExternalStorageDirectory());
            sb.append(File.separator);
            sb.append(ROOT_DIR);
            mBackupPath = sb.toString();
        }
        Logger.e(TAG, mBackupPath);
    }

    static void createInstance(Context context){
        if (mInstance == null){
            synchronized (Profile.class){
                if (mInstance == null){
                    mInstance = new Profile(context);
                }
            }
        }else {
            mInstance.mContext = context;
        }
    }
    public static Profile getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("Profile is not created");
        } else {
            return mInstance;
        }
    }

    public String getBackupPath() {
        return mBackupPath;
    }

    private boolean isSDCardExits() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
