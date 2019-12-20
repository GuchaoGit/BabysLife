package com.guc.babyslife.ui.adapter;

import android.databinding.BindingAdapter;

import com.guc.babyslife.widget.ViewCounterTips;

/**
 * Created by guc on 2019/12/20.
 * 描述：TextView databing 属性
 */
public class ViewCounterTipsBindingAdapter {
    @BindingAdapter(value = {"android:endYear", "android:endMonth", "android:endDay"}, requireAll = true)
    public static void setupAdapter(ViewCounterTips viewCounterTips, final int year, final int month, final int day) {
        viewCounterTips.setEndDate(year, month, day);
    }

}
