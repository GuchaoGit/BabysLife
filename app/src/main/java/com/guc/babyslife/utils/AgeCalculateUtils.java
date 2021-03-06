package com.guc.babyslife.utils;

import android.util.Log;

import com.guc.babyslife.model.Baby;

import java.util.Calendar;

/**
 * Created by guc on 2019/10/16.
 * 描述：年龄计算工具类
 */
public class AgeCalculateUtils {

    private static final String TAG = "AgeCalculateUtils";

    /**
     * 计算年龄  天数
     *
     * @param baby     儿童信息
     * @param calendar 当前日期
     * @return 返回当前 年龄 天数
     */
    public static int caculateAge(final Baby baby, Calendar calendar) {
        Calendar calendarBirth = Calendar.getInstance();
        calendarBirth.set(Calendar.YEAR, baby.birthYear);
        calendarBirth.set(Calendar.MONTH, baby.birthMonth);
        calendarBirth.set(Calendar.DAY_OF_MONTH, baby.birthDay);

        int mAge = 0;
        int mNowYear, mNowMonth, mNowDay;
        mNowYear = calendar.get(Calendar.YEAR);
        mNowMonth = calendar.get(Calendar.MONTH);
        mNowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (isNow(calendarBirth, mNowYear, mNowMonth, mNowDay)) {
            mAge = 0;
        } else {
            while (!isNow(calendarBirth, mNowYear, mNowMonth, mNowDay)) {
                mAge++;
                calendarBirth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        Log.e(TAG, "caculateAge: " + mAge + "天");
        return mAge;
    }

    /**
     * 获取年龄描述
     *
     * @param baby     儿童信息
     * @param calendar 当前日期
     * @return 返回当前 年龄描述
     */
    public static String getAgeDesc(final Baby baby, Calendar calendar) {
        StringBuilder ageDescSb = new StringBuilder();
        int mNowYear, mNowMonth, mNowDay;
        mNowYear = calendar.get(Calendar.YEAR);
        mNowMonth = calendar.get(Calendar.MONTH);
        mNowDay = calendar.get(Calendar.DAY_OF_MONTH);
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
            day += getMaxDayOfMonth(baby.birthYear, baby.birthMonth);
        }
        ageDescSb.append(year == 0 ? "" : year + "岁");
        ageDescSb.append(month != 0 ? month + "月" : year == 0 ? "" : "零");
        ageDescSb.append(day != 0 ? day + "天" : year == 0 && month == 0 ? "出生" : "");
        mAgeDesc = ageDescSb.toString();
        return mAgeDesc;
    }

    public static boolean isNow(Calendar calendar, int year, int month, int day) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }

    public static int getMaxDayOfMonth(int year, int month) {
        int max = 30;
        switch (month) {
            case 0://1月
            case 2://3月
            case 4://5月
            case 6://7月
            case 7://8月
            case 9://10月
            case 11://12月
                max = 31;
                break;
            case 1://2月
                if (isLeapYear(year)) {
                    max = 29;
                } else {
                    max = 28;
                }
                break;
            case 3://4月
            case 5://6月
            case 8://9月
            case 10://11月
                max = 30;
                break;

        }
        return max;
    }

    /**
     * @param year 年
     * @return 返 true :平年  false:润年
     */
    private static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0)) {
            return false;
        } else if (year % 400 == 0) {
            return false;
        }
        return true;
    }
}
