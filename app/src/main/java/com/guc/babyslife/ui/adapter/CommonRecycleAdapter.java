package com.guc.babyslife.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    protected Context mContext;
    protected List<T> mDataList;
    protected int mLayoutId;
    protected LayoutInflater mLayoutInflater;
    private RecyclerViewBindingAdapter.ItemClickListener itemClickListener;
    private RecyclerViewBindingAdapter.ItemLongClickListener itemLongClickListener;

    public CommonRecycleAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
    }

    public void setOnItemClickListener(RecyclerViewBindingAdapter.ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(RecyclerViewBindingAdapter.ItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    @NonNull
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createViewHolder(mLayoutId, parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        bindData(holder, mDataList.get(position), position);
        holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(CommonRecycleAdapter.this, holder.itemView, position);
                }
            }

            @Override
            public void onItemLongClickListener(int position) {
                if (itemLongClickListener != null) {
                    itemLongClickListener.onItemLongClick(CommonRecycleAdapter.this, holder.itemView, position);
                }
            }
        });
    }

    public T getItem(int position) {
        return mDataList == null ? null : mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public void update(List<T> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void addList(List<T> dataList) {
        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addItem(T data) {
        this.mDataList.add(data);
        notifyDataSetChanged();
    }

    public abstract void bindData(CommonViewHolder holder, T data, int position);

    public abstract CommonViewHolder createViewHolder(int layoutId, @NonNull ViewGroup parent, int viewType);
}
