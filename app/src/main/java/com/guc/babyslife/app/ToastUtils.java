package com.guc.babyslife.app;

import android.widget.Toast;

/**
 * Created by guc on 2019/10/15.
 * 描述：点击类
 */
public class ToastUtils {
    public static void toast(String msg){
        Toast.makeText(Profile.getInstance().mContext,msg,Toast.LENGTH_SHORT).show();
    }
    public static void toastLong(String msg){
        Toast.makeText(Profile.getInstance().mContext,msg,Toast.LENGTH_LONG).show();
    }
    public static void toast(String msg,int duration){
        Toast.makeText(Profile.getInstance().mContext,msg,duration).show();
    }
}
