package com.guc.babyslife.app;

import android.app.Application;

/**
 * Created by guc on 2019/10/15.
 * 描述：入口
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Profile.createInstance(this);
        Logger.isOpen = Profile.OPEN_LOG;//日志开关
    }
}
