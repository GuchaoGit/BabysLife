package com.guc.babyslife.app;

import android.util.Log;

/**
 * Created by guc on 2019/10/15.
 * 描述：日志类
 */
public class Logger {
    static boolean isOpen = true;

    public static void i(String tag, String msg) {
        if (isOpen) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isOpen) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isOpen) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isOpen) {
            Log.e(tag, msg);
        }
    }
}
