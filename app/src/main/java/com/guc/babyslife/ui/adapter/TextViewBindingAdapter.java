package com.guc.babyslife.ui.adapter;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.widget.TextView;

/**
 * Created by guc on 2019/10/25.
 * 描述：TextView databing 属性
 */
@BindingMethods({
        @BindingMethod(type = TextView.class, attribute = "android:textStyle", method = "setTypeface")
})
public class TextViewBindingAdapter {
}
