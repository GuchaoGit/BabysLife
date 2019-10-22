package com.guc.babyslife.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guc.babyslife.model.Baby;

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
     *
     * @param babiesStr 数据
     */
    public void setBabies(String babiesStr) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
    }
    public List<Baby> getBabies() {
        String babiesStr = mSharedPreferences.getString(KEY_BABIES, "[]");
        return mGson.fromJson(babiesStr, new TypeToken<List<Baby>>() {
        }.getType());
    }

    public void saveBaby(Baby baby) {
        baby.uuid = UUID.randomUUID().toString();
        List<Baby> babies = getBabies();
        babies.add(baby);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String babiesStr = mGson.toJson(babies);
        Logger.e(TAG, babiesStr);
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
    }

    public void updateBabies() {
        Calendar calendarNow = Calendar.getInstance();
        List<Baby> babies = getBabies();
        for (Baby baby : babies) {
            caculateAge(baby, calendarNow);
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String babiesStr = mGson.toJson(babies);
        Logger.e(TAG, babiesStr);
        edit.putString(KEY_BABIES, babiesStr);
        edit.apply();
    }

    //计算年龄
    private void caculateAge(Baby baby, Calendar calendarNow) {
        StringBuilder ageDescSb = new StringBuilder();
        Calendar calendarBirth = Calendar.getInstance();
        calendarBirth.set(Calendar.YEAR, baby.birthYear);
        calendarBirth.set(Calendar.MONTH, baby.birthMonth);
        calendarBirth.set(Calendar.DAY_OF_MONTH, baby.birthDay);

        int mAge = 0;
        int mNowYear, mNowMonth, mNowDay;
        mNowYear = calendarNow.get(Calendar.YEAR);
        mNowMonth = calendarNow.get(Calendar.MONTH);
        mNowDay = calendarNow.get(Calendar.DAY_OF_MONTH);
        String mAgeDesc;
        int year = mNowYear - baby.birthYear;
        int month = mNowMonth - baby.birthMonth;
        int day = mNowDay - baby.birthDay;
        if (mNowMonth < baby.birthMonth) {
            year -= 1;
            month += 12;
        }
        if (mNowDay < baby.birthDay) {
            month -= 1;
            day += 30;
        }
        ageDescSb.append(year == 0 ? "" : year + "岁");
        ageDescSb.append(month != 0 ? month + "月" : year == 0 ? "" : "零");
        ageDescSb.append(day != 0 ? day + "天" : year == 0 && month == 0 ? "出生" : "");
        mAgeDesc = ageDescSb.toString();
        baby.ageDesc = mAgeDesc;
        if (isNow(calendarBirth, mNowYear, mNowMonth, mNowDay)) {
            mAge = 0;
        } else {
            while (!isNow(calendarBirth, mNowYear, mNowMonth, mNowDay)) {
                mAge++;
                calendarBirth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        baby.age = mAge;
        Log.e(TAG, "caculateAge: " + mAge + "天");
    }

    private boolean isNow(Calendar calendar, int year, int month, int day) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }


}
