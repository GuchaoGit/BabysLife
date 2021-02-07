package com.guc.babyslife.utils;


/**
 * Created by guc on 2021/2/6.
 * Description：工具
 */
public class Utils {
    /**
     * 计算BMI
     *
     * @param height 身高 cm
     * @param weight 体重 kg
     * @return bmi
     */
    public static double getBMI(double height, double weight) {
        height = height / 100;
        double bmi = weight / (height * height);
        return Math.round(bmi * 100) / 100.0;
    }

    /**
     * 获取提示信息
     *
     * @param bmi bmi 值
     * @return 建议 1：过重 0：标准 -1：
     */
    public static String getBmiSuggest(double bmi) {
        if (bmi > 25) {
            return "偏重";
        } else if (bmi < 20) {
            return "偏瘦";
        } else {
            return "很标准";
        }
    }
}
