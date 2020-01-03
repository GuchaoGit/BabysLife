package com.guc.babyslife.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.model.VideoInfo;
import com.guc.babyslife.ui.adapter.AdapterVideo;
import com.guc.babyslife.utils.AssersUtil;

import org.salient.artplayer.MediaPlayerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2020/1/3.
 * 描述：育儿知识
 */
public class RearingActivity extends BaseActivity {
    @BindView(R.id.rcv_video)
    RecyclerView mRcvVideo;
    private AdapterVideo mAdapter;
    private List<VideoInfo> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_rearing);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mDatas = new ArrayList<>();
        mAdapter = new AdapterVideo(mContext, mDatas);
        mRcvVideo.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvVideo.setAdapter(mAdapter);
    }

    private void initData() {
        mAdapter.update(AssersUtil.getRearingDatas(mContext));
    }

    @Override
    protected void onDestroy() {
        MediaPlayerManager.instance().releasePlayerAndView(mContext);
        super.onDestroy();
    }
}
