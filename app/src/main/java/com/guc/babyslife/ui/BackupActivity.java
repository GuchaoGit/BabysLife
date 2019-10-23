package com.guc.babyslife.ui;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Profile;
import com.guc.babyslife.app.SpManager;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.BackupBinding;
import com.guc.babyslife.utils.FileUtils;

import java.io.File;
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
        mBinding.setBackupInfo(FileUtils.getBackupInfo(Profile.FN_BABY));
        mBinding.setBackupInfo4Db(FileUtils.getBackupInfo(Profile.FN_DB));
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
                backupDB();
                onGranted();
                break;
        }
    }

    //备份数据库
    private void backupDB() {
        FileUtils.copyFile(new File(Profile.getInstance().getDatabasePath()), new File(Profile.getInstance().getBackupPath(), Profile.FN_DB));
    }

    private void saveBabies2File() {
        FileUtils.writeStr2File(SpManager.getInstance().getBabiesStr());
    }
}
