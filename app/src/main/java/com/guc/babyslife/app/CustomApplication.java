package com.guc.babyslife.app;

import android.app.Application;

import com.guc.babyslife.db.DBUtil;

/**
 * Created by guc on 2019/10/15.
 * 描述：入口
 */
public class CustomApplication extends Application {
    private static final String TAG = "CustomApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG, String.valueOf(System.currentTimeMillis()));
        Profile.createInstance(this);
        Logger.isOpen = Profile.OPEN_LOG;//日志开关
        SpManager.getInstance().updateBabies();
        DBUtil.getInstance(this);
        Logger.e(TAG, String.valueOf(System.currentTimeMillis()));
    }
}
