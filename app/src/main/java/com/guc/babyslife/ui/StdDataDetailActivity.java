package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guc.babyslife.R;
import com.guc.babyslife.app.Constants;
import com.guc.babyslife.databinding.StdDataDetailBinding;
import com.guc.babyslife.ui.adapter.AdapterStdDatas;
import com.guc.babyslife.utils.AssersUtil;

public class StdDataDetailActivity extends AppCompatActivity {
    private int mDataType = Constants.STD_UNKNOW;

    private StdDataDetailBinding mBinding;
    private AdapterStdDatas mAdapter;

    public static void jump(Context context, int type) {
        Intent intent = new Intent(context, StdDataDetailActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataType = getIntent().getIntExtra("type", Constants.STD_UNKNOW);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_std_data_detail);
        mBinding.setTitle(getTitle(mDataType));
        mAdapter = new AdapterStdDatas(this, AssersUtil.getStdDatas(this, mDataType));
        mBinding.setStdAdapter(mAdapter);
    }

    private String getTitle(int type) {
        CharSequence title;
        switch (type) {
            case Constants.STD_BOY_HEIGHT:
                title = getText(R.string.child_height_boy);
                break;
            case Constants.STD_BOY_WEIGHT:
                title = getText(R.string.child_weight_boy);
                break;
            case Constants.STD_GIRL_HEIGHT:
                title = getText(R.string.child_height_girl);
                break;
            case Constants.STD_GIRL_WEIGHT:
                title = getText(R.string.child_weight_girl);
                break;
            default:
                title = "未知";
                break;
        }
        return title.toString();
    }
}
