package com.guc.babyslife.ui.adapter;

import android.databinding.BindingAdapter;

import com.guc.babyslife.widget.ToolBar;

/**
 * Created by guc on 2021/2/20.
 * 描述：ToolBar databing 属性
 */
public class ToolBarBindingAdapter {
    @BindingAdapter(value = {"android:onRightClickedListener"}, requireAll = false)
    public static void setupAdapter(ToolBar toolBar, final ToolBar.OnRightClickedListener rightClickedListener) {
        toolBar.setOnRightClickedListener(rightClickedListener);
    }

}
