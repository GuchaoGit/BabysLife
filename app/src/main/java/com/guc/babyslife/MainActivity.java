package com.guc.babyslife;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.MainBinding;
import com.guc.babyslife.ui.AddNewBabyActivity;
import com.guc.babyslife.widget.ToolBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MainBinding mBinding;
    private ToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setClick(this);
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setOnRightClickedListener(() -> startActivity(new Intent(this, AddNewBabyActivity.class)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prince:
                ToastUtils.toast("小王子");
                break;
            case R.id.btn_princess:
                ToastUtils.toast("小公主");
                break;
        }

    }
}
