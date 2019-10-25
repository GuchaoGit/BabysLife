package com.guc.babyslife;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Constants;
import com.guc.babyslife.app.Logger;
import com.guc.babyslife.databinding.MainBinding;
import com.guc.babyslife.ui.AddNewBabyActivity;
import com.guc.babyslife.ui.StdDataDetailActivity;
import com.guc.babyslife.widget.ToolBar;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private MainBinding mBinding;
    private ToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, "onCreate" + System.currentTimeMillis());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setClick(this);
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setOnRightClickedListener(() -> startActivity(new Intent(this, AddNewBabyActivity.class)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prince_height:
                StdDataDetailActivity.jump(this, Constants.STD_BOY_HEIGHT);
                break;
            case R.id.btn_prince_weight:
                StdDataDetailActivity.jump(this, Constants.STD_BOY_WEIGHT);
                break;
            case R.id.btn_princess_height:
                StdDataDetailActivity.jump(this, Constants.STD_GIRL_HEIGHT);
                break;
            case R.id.btn_princess_weight:
                StdDataDetailActivity.jump(this, Constants.STD_GIRL_WEIGHT);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e(TAG, "onResume" + System.currentTimeMillis());
    }
}
