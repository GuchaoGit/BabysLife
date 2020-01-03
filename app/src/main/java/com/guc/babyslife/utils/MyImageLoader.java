package com.guc.babyslife.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by guc on 2020/1/3.
 * 描述：图片加载
 */
public class MyImageLoader {
    private MyImageLoader() {
        throw new AssertionError();
    }

    public static void display(Context context, String url, ImageView target) {
        Glide.with(context).load(url).into(target);
    }
}
