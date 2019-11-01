package com.guc.babyslife.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.guc.babyslife.R;
import com.guc.babyslife.app.SpManager;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.ui.adapter.AdapterBabies;
import com.guc.babyslife.utils.AgeCalculateUtils;
import com.guc.babyslife.widget.ToolBar;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2019/10/15.
 * 描述：添加 婴儿
 */
public class AddNewBabyActivity extends AppCompatActivity {
    private static final String TAG = "AddNewBabyActivity";
    private static final int REQUEST_CODE = 0X39;
    @BindView(R.id.name_et)
    EditText mNameEt;
    @BindView(R.id.rb_prince)
    RadioButton mRbPrince;
    @BindView(R.id.rb_princess)
    RadioButton mRbPrincess;
    @BindView(R.id.sex_rg)
    RadioGroup mSexRg;
    @BindView(R.id.sel_birth_tv)
    TextView mSelBirthTv;
    @BindView(R.id.sel_age_tv)
    TextView mSelAgeTv;
    @BindView(R.id.add_btn)
    Button mAddBtn;
    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.rcv_baby)
    RecyclerView mRcvBaby;
    private DatePickerDialog mDatePickerDialog;
    private int mBirthYear, mBirthMonth, mBirthDay;
    private int mNowYear, mNowMonth, mNowDay;
    private String mBirth;
    private int mAge = 0;//天数
    private String mAgeDesc;//年龄描述
    private AdapterBabies mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_baby);
        ButterKnife.bind(this);
        initView();
        initRcv();
    }

    private void initRcv() {
        RecyclerView recyclerView = findViewById(R.id.rcv_baby);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new AdapterBabies(this, SpManager.getInstance().getBabies());
        recyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        Calendar calendar = Calendar.getInstance();
        mNowYear = mBirthYear = calendar.get(Calendar.YEAR);
        mNowMonth = mBirthMonth = calendar.get(Calendar.MONTH);
        mNowDay = mBirthDay = calendar.get(Calendar.DAY_OF_MONTH);
        mToolbar.setOnRightClickedListener(() -> startActivityForResult(new Intent(this, BackupActivity.class), REQUEST_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            boolean needUpdate = data.getBooleanExtra("needUpdate", false);
            if (needUpdate) update();
        }
    }

    @OnClick({R.id.sel_birth_tv, R.id.add_btn, R.id.sel_age_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sel_birth_tv:
            case R.id.sel_age_tv:
                showSelBirthDialog();
                break;
            case R.id.add_btn:
                add();
                break;
        }
    }

    private void add() {
        if (mNameEt.getText().toString().trim().isEmpty()) {
            ToastUtils.toast("姓名不能为空");
        } else {
            String name = mNameEt.getText().toString().trim();
            String sex = mSexRg.getCheckedRadioButtonId() == R.id.rb_prince ? "1" : "2";
            Baby baby = new Baby();
            baby.name = name;
            baby.birthday = mBirth;
            baby.birthYear = mBirthYear;
            baby.birthMonth = mBirthMonth;
            baby.birthDay = mBirthDay;
            baby.sex = sex;
            baby.age = mAge;
            baby.ageDesc = mAgeDesc;
            SpManager.getInstance().saveBaby(baby);
            mNameEt.setText("");
            update();
            ToastUtils.toast("添加成功");
        }
    }

    //更新列表
    private void update() {
        mAdapter.update(SpManager.getInstance().getBabies());
    }

    //计算年龄
    private void caculateAge() {
        mAge = 0;
        StringBuilder ageDescSb = new StringBuilder();
        Calendar calendarBirth = Calendar.getInstance();
        calendarBirth.set(Calendar.YEAR, mBirthYear);
        calendarBirth.set(Calendar.MONTH, mBirthMonth);
        calendarBirth.set(Calendar.DAY_OF_MONTH, mBirthDay);

        int year = mNowYear - mBirthYear;
        int month = mNowMonth - mBirthMonth;
        int day = mNowDay - mBirthDay;
        if (mNowMonth < mBirthMonth) {
            year -= 1;
            month += 12;
        }
        if (mNowDay < mBirthDay) {
            month -= 1;
            day += AgeCalculateUtils.getMaxDayOfMonth(mBirthYear, mBirthMonth);
        }
        ageDescSb.append(year == 0 ? "" : year + "岁");
        ageDescSb.append(month != 0 ? month + "月" : year == 0 ? "" : "零");
        ageDescSb.append(day != 0 ? day + "天" : year == 0 && month == 0 ? "出生" : "");
        mAgeDesc = ageDescSb.toString();
        mSelAgeTv.setText(mAgeDesc);
        if (isNow(calendarBirth)) {
            mAge = 0;
        } else {
            while (!isNow(calendarBirth)) {
                mAge++;
                calendarBirth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        Log.e(TAG, "caculateAge: " + mAge + "天");
    }

    private boolean isNow(Calendar calendar) {
        return calendar.get(Calendar.YEAR) == mNowYear && calendar.get(Calendar.MONTH) == mNowMonth && calendar.get(Calendar.DAY_OF_MONTH) == mNowDay;
    }

    private void showSelBirthDialog() {
        mDatePickerDialog = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
            if (mNowYear < year || (mNowYear == year && mNowMonth < month) || (mNowYear == year && mNowMonth == month && mNowDay < dayOfMonth)) {
                ToastUtils.toast("不能选择未来的时间");
                return;
            }
            mBirthYear = year;
            mBirthMonth = month;
            mBirthDay = dayOfMonth;
            mBirth = mBirthYear + "-" + (mBirthMonth + 1) + "-" + mBirthDay;
            mSelBirthTv.setText(mBirth);
            caculateAge();
        }, mBirthYear, mBirthMonth, mBirthDay);
        mDatePickerDialog.show();
    }
}
