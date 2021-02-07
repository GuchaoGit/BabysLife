package com.guc.babyslife.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.guc.babyslife.R;

/**
 * Created by guc on 2021/2/6.
 * Description：加载动画
 */
public class LoadingDialog extends Dialog {
    private TextView tvTip;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.LoadingDialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.view_loading_dialog);
        tvTip = findViewById(R.id.tvTips);
    }

    public void setTips(String tip) {
        tvTip.setText(tip);
    }

}
