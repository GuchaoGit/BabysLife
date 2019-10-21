package com.guc.babyslife.ui.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by guc on 2019/10/16.
 * 描述：使用@BindingAdapter定义相关属性：
 * 我们针对setOnItemClickListener,setOnLoadMoreListener,setEnableLoadMore这三个方法定义三个属性
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"android:onItemClick"}, requireAll = false)
    public static void setupAdapter(RecyclerView recyclerView, final ItemClickListener itemClickListener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || !(adapter instanceof CommonRecycleAdapter)) {
            return;
        }
        CommonRecycleAdapter quickAdapter = (CommonRecycleAdapter) adapter;
        quickAdapter.setOnItemClickListener((RecyclerView.Adapter adapter2, View view, int position) ->
                itemClickListener.onItemClick(adapter2, view, position)
        );

    }

    public interface ItemClickListener {
        void onItemClick(RecyclerView.Adapter adapter, View view, int position);
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
