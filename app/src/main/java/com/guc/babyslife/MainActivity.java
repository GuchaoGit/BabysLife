package com.guc.babyslife;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Constants;
import com.guc.babyslife.app.Logger;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.MainBinding;
import com.guc.babyslife.ui.AddNewBabyActivity;
import com.guc.babyslife.ui.BmiActivity;
import com.guc.babyslife.ui.RearingActivity;
import com.guc.babyslife.ui.StdDataDetailActivity;
import com.guc.babyslife.widget.ToolBar;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, BaseActivity.PermissionListener {
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
        mToolBar.setOnLeftClickedListener(() -> startActivity(new Intent(this, RearingActivity.class)));
        requestRuntimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, this, false);
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
            case R.id.btn_bmi:
                BmiActivity.jump(this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e(TAG, "onResume" + System.currentTimeMillis());
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(List<String> deniedPermissions) {
        ToastUtils.toastLong("你拒绝了访问设备照片，将无法展示记录中的图片\n可前往“备份”再次开启");
    }
}
