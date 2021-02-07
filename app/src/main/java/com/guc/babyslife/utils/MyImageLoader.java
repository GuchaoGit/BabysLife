package com.guc.babyslife.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guc.babyslife.R;

/**
 * Created by guc on 2020/1/3.
 * 描述：图片加载
 */
public class MyImageLoader {
    private MyImageLoader() {
        throw new AssertionError();
    }

    private static RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.load_failure).error(R.drawable.load_failure);

    public static void display(Context context, String url, ImageView target) {
        Glide.with(context).load(url).apply(requestOptions).into(target);
    }

    public static void display(String url, ImageView target) {
        Glide.with(target).load(url).apply(requestOptions).into(target);
    }
}
