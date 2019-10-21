package com.guc.babyslife.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guc.babyslife.R;
import com.guc.babyslife.databinding.ItemBabyBinding;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.ui.BabyDetailActivity;

import java.util.List;

/**
 * Created by guc on 2019/10/15.
 * 描述：
 */
public class AdapterBabies extends RecyclerView.Adapter<CommonViewHolder> {
    private List<Baby> babies;
    private Context context;

    public AdapterBabies(Context context, List<Baby> babies) {
        this.babies = babies;
        this.context = context;
    }

    public void update(List<Baby> babies) {
        this.babies = babies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemBabyBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_baby, viewGroup, false);
        return new CommonViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int i) {
        ItemBabyBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
        binding.setBaby(babies.get(i));
        binding.executePendingBindings();
        viewHolder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                BabyDetailActivity.jump(context, babies.get(position));
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return babies == null ? 0 : babies.size();
    }
}
