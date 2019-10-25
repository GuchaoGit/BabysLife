package com.guc.babyslife.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guc.babyslife.R;
import com.guc.babyslife.databinding.ItemStdDataBinding;
import com.guc.babyslife.model.StdData;

import java.util.List;

/**
 * Created by guc on 2019/10/25.
 * 描述：标准信息加载
 */
public class AdapterStdDatas extends CommonRecycleAdapter<StdData> {
    public AdapterStdDatas(Context context, List<StdData> dataList) {
        super(context, dataList, R.layout.item_std_data);
    }

    @Override
    public void bindData(CommonViewHolder holder, StdData data, int position) {
        ItemStdDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setData(data);
        binding.executePendingBindings();
    }

    @Override
    public CommonViewHolder createViewHolder(int layoutId, @NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemStdDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return new CommonViewHolder(binding.getRoot());
    }
}
