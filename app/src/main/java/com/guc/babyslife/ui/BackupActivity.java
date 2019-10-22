package com.guc.babyslife.ui;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.SpManager;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.BackupBinding;
import com.guc.babyslife.utils.FileUtils;

import java.util.List;

/**
 * 备份操作
 */
public class BackupActivity extends BaseActivity implements BaseActivity.PermissionListener, View.OnClickListener {

    private BackupBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_backup);
        mBinding.setClick(this);
        requestRuntimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, this);
    }

    @Override
    public void onGranted() {
        mBinding.setBackupInfo(FileUtils.getBackupInfo());
    }

    @Override
    public void onDenied(List<String> deniedPermissions) {
        ToastUtils.toast("您已拒绝了必要权限");
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backup:
                saveBabies2File();
                break;
            case R.id.tv_backup_info:
                break;
        }
    }

    private void saveBabies2File() {
        String fileName = FileUtils.writeStr2File(SpManager.getInstance().getBabiesStr());
        ToastUtils.toast(fileName);
    }
}
