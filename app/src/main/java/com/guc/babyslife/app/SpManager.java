package com.guc.babyslife.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.utils.AgeCalculateUtils;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by guc on 2019/10/15.
 * 描述：SharedPreferences 管理类
 */
public class SpManager {
    private static final String TAG = "SpManager";
    private static final String KEY_BABIES = "babies";
    private final static String SP_NAME_DEFAULT = "sp_cache";
    private static SpManager mInstance;
    private SharedPreferences mSharedPreferences;
    private Gson mGson;

    private SpManager() {
        mSharedPreferences = Profile.getInstance().mContext.getSharedPreferences(SP_NAME_DEFAULT, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public static SpManager getInstance() {
        if (mInstance == null) {
            synchronized (SpManager.class) {
                if (mInstance == null) {
                    mInstance = new SpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 备份用
     *
     * @return List字符串
     */
    public String getBabiesStr() {
        return mSharedPreferences.getString(KEY_BABIES, "[]");
    }

    /**
     * 备份恢复用
     */
    public boolean restoreBabies(List<Baby> babies) {
        Calendar calendarNow = Calendar.getInstance();
        if (babies == null || babies.size() == 0) return false;
        for (Baby baby : babies) {
            baby.age = AgeCalculateUtils.caculateAge(baby, calendarNow);
            baby.ageDesc = AgeCalculateUtils.getAgeDesc(baby, calendarNow);
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String babiesStr = mGson.toJson(babies);
        Logger.e(TAG, babiesStr);
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
        return true;
    }

    public List<Baby> getBabies() {
        String babiesStr = mSharedPreferences.getString(KEY_BABIES, "[]");
        Logger.e(TAG, babiesStr);
        return mGson.fromJson(babiesStr, new TypeToken<List<Baby>>() {
        }.getType());
    }

    public void saveBaby(Baby baby) {
        if (baby.uuid == null)
            baby.uuid = UUID.randomUUID().toString();
        List<Baby> babies = getBabies();
        babies.add(baby);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String babiesStr = mGson.toJson(babies);
        Logger.e(TAG, babiesStr);
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
    }

    void updateBabies() {
        Calendar calendarNow = Calendar.getInstance();
        List<Baby> babies = getBabies();
        for (Baby baby : babies) {
            baby.age = AgeCalculateUtils.caculateAge(baby, calendarNow);
            baby.ageDesc = AgeCalculateUtils.getAgeDesc(baby, calendarNow);
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String babiesStr = mGson.toJson(babies);
        Logger.e(TAG, babiesStr);
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
    }

}
