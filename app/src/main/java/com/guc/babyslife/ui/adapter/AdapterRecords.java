package com.guc.babyslife.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guc.babyslife.R;
import com.guc.babyslife.databinding.ItemRecordsBinding;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.ui.PictureViewActivity;

import java.util.List;

/**
 * Created by guc on 2019/10/21.
 * 描述：
 */
public class AdapterRecords extends CommonRecycleAdapter<GrowData> {
    public AdapterRecords(Context context, List<GrowData> dataList) {
        super(context, dataList, R.layout.item_records);
    }

    @Override
    public void bindData(CommonViewHolder holder, GrowData data, int position) {
        ItemRecordsBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setData(mDataList.get(position));
        binding.setClick(v -> {
            PictureViewActivity.jump(mContext, data.getPhoto(), data.getMeasureDate()); //查看图片
        });
        binding.executePendingBindings();
    }

    @Override
    public CommonViewHolder createViewHolder(int layoutId, @NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemRecordsBinding binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return new CommonViewHolder(binding.getRoot());
    }
}
