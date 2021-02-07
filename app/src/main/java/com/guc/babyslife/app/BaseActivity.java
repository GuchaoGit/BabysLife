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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.guc.babyslife.system.BaseSystem;
import com.guc.babyslife.system.SystemManager;
import com.guc.babyslife.system.SystemPages;
import com.guc.babyslife.widget.dialog.CoffeeLoadingDialog;
import com.guc.babyslife.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guc on 2019/10/15.
 * 描述：base Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SETTING = 2;
    private static final String DEF_DENIED_MESSAGE = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权";
    private static final String DEF_DENIED_CLOSE_BTN_TEXT = "关闭";
    private static final String DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用";
    private static final String DEF_RATIONAL_BTN_TEXT = "去设置";
    private static final int REQUEST_CODE = 1;
    protected Context mContext;
    private PermissionListener mListener;
    private String[] mPermissions;
    private boolean isForce;//是否强制
    private LoadingDialog loadingDialog;
    private CoffeeLoadingDialog coffeeLoadingDialog;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                LinkedList<String> grantedPermissions = new LinkedList<>();
                LinkedList<String> deniedPermissions = new LinkedList<>();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        grantedPermissions.add(permission);
                    else deniedPermissions.add(permission);
                }
                //全部允许才回调 onGranted 否则只要有一个拒绝都回调 onDenied
                if (!grantedPermissions.isEmpty() && deniedPermissions.isEmpty()) {
                    if (mListener != null)
                        mListener.onGranted();
                } else if (!deniedPermissions.isEmpty()) {
                    showDeniedDialog(deniedPermissions);
                }
                break;
        }
    }

    public void requestRuntimePermissions(String[] permissions, PermissionListener listener) {
        requestRuntimePermissions(permissions, listener, true);
    }

    public void requestRuntimePermissions(String[] permissions, PermissionListener listener, boolean isForce) {
        this.isForce = isForce;
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
        if (rationale) {
            if (isForce)
                showRationalDialog(permissions);
        } else {
            requestPermissions(permissions, REQUEST_CODE);
        }
    }

    private synchronized void showRationalDialog(final String[] permissions) {
        new AlertDialog.Builder(this)
                .setMessage(DEF_RATIONAL_MESSAGE)
                .setCancelable(false)
                .setNegativeButton(DEF_DENIED_CLOSE_BTN_TEXT, (DialogInterface dialog, int which) ->
                        this.finish())
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

    /**
     * 拒绝权限提示框
     *
     * @param permissions
     */
    private synchronized void showDeniedDialog(final List<String> permissions) {
        new AlertDialog.Builder(this)
                .setMessage(DEF_DENIED_MESSAGE)
                .setCancelable(false)
                .setNegativeButton(DEF_DENIED_CLOSE_BTN_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onDenied(permissions);
                        }
                    }
                })
                .setPositiveButton(DEF_RATIONAL_BTN_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSetting();
                    }
                }).show();
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏显示框
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 显示加载框
     */
    public void showCoffeeLoading() {
        if (coffeeLoadingDialog == null) {
            coffeeLoadingDialog = new CoffeeLoadingDialog(this);
        }
        if (!coffeeLoadingDialog.isShowing()) {
            coffeeLoadingDialog.show();
        }
    }

    /**
     * 隐藏显示框
     */
    public void hideCoffeeLoading() {
        if (coffeeLoadingDialog != null && coffeeLoadingDialog.isShowing()) {
            coffeeLoadingDialog.dismiss();
        }
    }

    public interface PermissionListener {
        void onGranted();

        void onDenied(List<String> deniedPermissions);
    }

}
