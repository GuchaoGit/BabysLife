package com.guc.babyslife.app;

import android.content.Context;

/**
 * Created by guc on 2019/10/15.
 * 描述：配置文件
 */
public class Profile {
    private static Profile mInstance;
    public Context mContext;
    static final boolean OPEN_LOG = true;//true : 开启  false :关闭
    private Profile(Context context){
        this.mContext = context.getApplicationContext();
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
}
