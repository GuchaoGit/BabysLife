package com.guc.babyslife.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by guc on 2019/10/15.
 * 通用RecycleView适配器
 */

public class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
    // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
    // 用来存放 View 以减少 findViewById 的次数
    private SparseArray<View> mViewSparseArray;

    private onItemCommonClickListener mCommonClickListener;

    public CommonViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mViewSparseArray = new SparseArray<>();
    }

    /**
     * 根据 ID 来获取 View
     *
     * @param viewId viewID
     * @param <T>    泛型
     * @return 将结果强转为 View 或 View 的子类型
     */
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找，找打的话则直接返回
        // 如果找不到则 findViewById ，再把结果存入缓存中
        View view = mViewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViewSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public CommonViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public CommonViewHolder setText(int viewId, int resId) {
        TextView tv = getView(viewId);
        tv.setText(resId);
        return this;
    }

    public CommonViewHolder setTextBackground(int viewId, int resourceId) {
        TextView tv = getView(viewId);
        tv.setBackgroundResource(resourceId);
        return this;
    }

    public CommonViewHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }

    public void setCommonClickListener(onItemCommonClickListener commonClickListener) {
        this.mCommonClickListener = commonClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mCommonClickListener != null) {
            mCommonClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mCommonClickListener != null) {
            mCommonClickListener.onItemLongClickListener(getAdapterPosition());
        }
        return false;
    }

    public interface onItemCommonClickListener {
        void onItemClickListener(int position);

        void onItemLongClickListener(int position);
    }
}
