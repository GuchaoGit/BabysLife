package com.guc.babyslife.ui;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Logger;
import com.guc.babyslife.app.Profile;
import com.guc.babyslife.app.SpManager;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.BackupBinding;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.BackupInfo;
import com.guc.babyslife.utils.FileUtils;
import com.guc.babyslife.widget.ToolBar;

import java.io.File;
import java.util.List;

/**
 * 备份操作
 */
public class BackupActivity extends BaseActivity implements BaseActivity.PermissionListener, View.OnClickListener, ToolBar.OnLeftClickedListener {
    private static final String TAG = "BackupActivity";
    private BackupBinding mBinding;
    private BackupInfo mBakBaby, mBakDb;
    private boolean needUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_backup);
        mBinding.setClick(this);
        mBinding.setLeftClick(this);
        requestRuntimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, this);
    }

    @Override
    public void onGranted() {
        mBakBaby = FileUtils.getBackupInfo(Profile.FN_BABY);
        mBakDb = FileUtils.getBackupInfo(Profile.FN_DB);
        mBinding.setBackupInfo(mBakBaby);
        mBinding.setBackupInfo4Db(mBakDb);
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
                if (backupDB()) {
                    ToastUtils.toast("备份成功");
                } else {
                    ToastUtils.toast("备份失败");
                }
                onGranted();
                break;
            case R.id.btn_restore:
                new AlertDialog.Builder(this).setTitle(getString(R.string.warning))
                        .setMessage(getString(R.string.warning_restore_data))
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) ->
                                dialog.dismiss())
                        .setPositiveButton(getString(R.string.sure), (dialog, which) -> {
                            restoreData();
                            dialog.dismiss();
                        }).create().show();

                break;
        }
    }

    //备份数据库
    private boolean backupDB() {
        return FileUtils.copyFile(new File(Profile.getInstance().getDatabasePath()), new File(Profile.getInstance().getBackupPath(), Profile.FN_DB));
    }

    private void saveBabies2File() {
        FileUtils.writeStr2File(SpManager.getInstance().getBabiesStr());
    }

    /**
     * 恢复备份数据
     */
    private void restoreData() {
        String spBaby = FileUtils.readFile2Str(new File(mBakBaby.path, mBakBaby.fileName));
        Logger.e(TAG, spBaby);
        try {
            List<Baby> babies = new Gson().fromJson(spBaby, new TypeToken<List<Baby>>() {
            }.getType());
            boolean success = SpManager.getInstance().restoreBabies(babies);
            if (success) {
                FileUtils.copyFile(new File(mBakDb.path, mBakDb.fileName), new File(Profile.getInstance().getDatabasePath()));
                ToastUtils.toast("恢复成功");
                needUpdate = true;
            } else {
                ToastUtils.toast("恢复0条数据");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            ToastUtils.toast("恢复失败");
        }
    }

    @Override
    public void onLeftClicked() {
        Intent intent = new Intent();
        intent.putExtra("needUpdate", needUpdate);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        onLeftClicked();
    }
}
