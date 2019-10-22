package com.guc.babyslife.app;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.guc.babyslife.system.BaseSystem;
import com.guc.babyslife.system.SystemManager;
import com.guc.babyslife.system.SystemPages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guc on 2019/10/15.
 * 描述：base Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SETTING = 2;
    private static final String DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用";
    private static final String DEF_RATIONAL_BTN_TEXT = "去设置";
    private static final int REQUEST_CODE = 1;
    protected Context mContext;
    private PermissionListener mListener;
    private String[] mPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getSystem(SystemPages.class).addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSystem(SystemPages.class).removeActivity(this);
    }

    protected <T extends BaseSystem> T getSystem(Class<T> system) {
        return SystemManager.getInstance().getSystem(system);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTING:
                requestRuntimePermissions(mPermissions, mListener);
                break;
        }
    }

    public void requestRuntimePermissions(String[] permissions, PermissionListener listener) {
        mPermissions = permissions;
        mListener = listener;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mListener.onGranted();
            return;
        }
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            startRequestPermissions(permissionList);
        } else {
            mListener.onGranted();
        }
    }

    private void startRequestPermissions(List<String> mDeniedPermissions) {
        boolean rationale = false;
        //如果有拒绝则提示申请理由提示框，否则直接向系统请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        for (String permission : mDeniedPermissions) {
            rationale = rationale || shouldShowRequestPermissionRationale(permission);
        }
        String[] permissions = mDeniedPermissions.toArray(new String[mDeniedPermissions.size()]);
//        if (rationale) {
        requestPermissions(permissions, REQUEST_CODE);
//        } else {
//            showRationalDialog(permissions);
//        }
    }

    private synchronized void showRationalDialog(final String[] permissions) {
        new AlertDialog.Builder(this)
                .setMessage(DEF_RATIONAL_MESSAGE)
                .setCancelable(false)
                .setPositiveButton(DEF_RATIONAL_BTN_TEXT, (DialogInterface dialog, int which) ->
                        startSetting()
                ).show();
    }

    /**
     * 跳转到设置界面
     */
    private void startSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_SETTING);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_SETTING);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public interface PermissionListener {
        void onGranted();

        void onDenied(List<String> deniedPermissions);
    }

}
