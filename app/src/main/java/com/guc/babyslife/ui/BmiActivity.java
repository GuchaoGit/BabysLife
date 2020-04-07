package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2020/4/7.
 * 描述：BMI 身高体重指数
 */
public class BmiActivity extends BaseActivity {
    @BindView(R.id.height_et)
    EditText mHeightEt;
    @BindView(R.id.weight_et)
    EditText mWeightEt;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.tv_suggest)
    TextView mTvSuggest;

    public static void jump(Context context) {
        Intent intent = new Intent(context, BmiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_calculate)
    public void onViewClicked() {
        calBmi();
    }

    /**
     * 计算bmi
     */
    private void calBmi() {
        String heigthStr = mHeightEt.getText().toString().trim();
        if (TextUtils.isEmpty(heigthStr)) {
            ToastUtils.toast("请输入身高");
            return;
        }
        String weightStr = mWeightEt.getText().toString().trim();
        if (TextUtils.isEmpty(weightStr)) {
            ToastUtils.toast("请输入体重");
            return;
        }
        double height = Double.parseDouble(heigthStr) / 100;
        double weight = Double.parseDouble(weightStr);
        double bmi = weight / (height * height);//体重 除以 身高的平方
        mTvResult.setText(String.format(getString(R.string.bmi_result), bmi));
        mTvSuggest.setText(getBmiSuggest(bmi));
    }

    /**
     * 获取提示信息
     *
     * @param bmi bmi 值
     * @return 建议 1：过重 0：标准 -1：
     */
    private String getBmiSuggest(double bmi) {
        if (bmi > 25) {
            return getString(R.string.advice_heavy);
        } else if (bmi < 20) {
            return getString(R.string.advice_light);
        } else {
            return getString(R.string.advice_average);
        }
    }
}
