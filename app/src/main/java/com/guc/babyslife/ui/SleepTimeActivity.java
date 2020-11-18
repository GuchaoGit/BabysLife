package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guc.babyslife.R;
import com.guc.babyslife.model.SleepTime;
import com.guc.babyslife.ui.adapter.CommonRecycleAdapter;
import com.guc.babyslife.ui.adapter.CommonViewHolder;
import com.guc.babyslife.utils.AssersUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guc on 2020/11/18.
 * Description：不同年龄段睡眠时间
 */
public class SleepTimeActivity extends AppCompatActivity {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;

    private CommonRecycleAdapter<SleepTime> adapter;

    public static void jump(Context context) {
        Intent intent = new Intent(context, SleepTimeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_time);
        ButterKnife.bind(this);
        loadData();
    }

    private void loadData() {
        rcvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CommonRecycleAdapter<SleepTime>(this, AssersUtil.getSleepTimeData(this), R.layout.item_sleep_time) {
            @Override
            public void bindData(CommonViewHolder holder, SleepTime data, int position) {
                TextView age = holder.getView(R.id.tv_age);
                TextView recommend = holder.getView(R.id.tv_recommend);
                TextView possibleFit = holder.getView(R.id.tv_possible_fit);
                TextView notRecommend = holder.getView(R.id.tv_not_recommend);
                age.setTypeface(position == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                recommend.setTypeface(position == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                notRecommend.setTypeface(position == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                age.setTypeface(position == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                holder.setText(R.id.tv_age, data.age);
                holder.setText(R.id.tv_recommend, data.recommend);
                holder.setText(R.id.tv_possible_fit, data.possibleFit);
                holder.setText(R.id.tv_not_recommend, data.notRecommend);
            }

            @Override
            public CommonViewHolder createViewHolder(int layoutId, @NonNull ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(SleepTimeActivity.this).inflate(layoutId, parent, false);
                return new CommonViewHolder(rootView);
            }
        };
        rcvContent.setAdapter(adapter);
    }
}