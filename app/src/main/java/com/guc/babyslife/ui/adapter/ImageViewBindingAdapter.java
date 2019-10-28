package com.guc.babyslife.ui.adapter;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.guc.babyslife.R;

/**
 * Created by guc on 2019/10/28.
 * 描述：ImageView databing 属性
 */
public class ImageViewBindingAdapter {

    @BindingAdapter(value = {"android:imageBitmap",}, requireAll = false)
    public static void setupAdapter(ImageView imageView, final Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.icon_empty);
        }

    }
}
