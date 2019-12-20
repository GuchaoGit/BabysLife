package com.guc.babyslife.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.guc.babyslife.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/20.
 * 描述：计时提醒
 */
public class ViewCounterTips extends FrameLayout {
    private static final String TAG = "ViewCounterTips";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_hundredOfDay)
    TextView mTvHundredOfDay;
    @BindView(R.id.tv_tensOfDay)
    TextView mTvTensOfDay;
    @BindView(R.id.tv_unitsOfDay)
    TextView mTvUnitsOfDay;
    @BindView(R.id.tv_tensOfHour)
    TextView mTvTensOfHour;
    @BindView(R.id.tv_unitsOfHour)
    TextView mTvUnitsOfHour;
    @BindView(R.id.tv_hour)
    TextView mTvHour;
    // 月为1-12
    private int endYear = 2019, endMonth = 12, endDay = 15, endHours = 12, endMinute = 0;
    private Calendar mEndCalendar;
    private int mLeftDay, mLeftHour;//剩余天数 ， 小时
    private long mEndTimeMillis, mCurrentTimeMillis;
    private boolean isShowHour = false;//是否显示小时


    public ViewCounterTips(@NonNull Context context) {
        this(context, null);
    }

    public ViewCounterTips(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewCounterTips(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_counter, this);
        ButterKnife.bind(this);
        initView();
        initEndDate();
        calculateLeftTime();
    }

    private void initView() {
        mTvHour.setVisibility(isShowHour ? View.VISIBLE : View.GONE);
        mTvTensOfHour.setVisibility(isShowHour ? View.VISIBLE : View.GONE);
        mTvUnitsOfHour.setVisibility(isShowHour ? View.VISIBLE : View.GONE);
    }

    /**
     * 更新时间
     */
    public void updateTime() {
        calculateLeftTime();
    }

    /**
     * 设置截止日期
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     */
    public void setEndDate(int year, int month, int day) {
        this.endYear = year;
        this.endMonth = month;
        this.endDay = day;
        initEndDate();
        updateTime();
    }

    /**
     * 设置截止日期
     *
     * @param year   年
     * @param month  月 1-12
     * @param day    日
     * @param hour   时
     * @param minute 分
     */
    public void setEndDateTime(int year, int month, int day, int hour, int minute) {
        this.endHours = hour;
        this.endMinute = minute;
        setEndDate(year, month, day);
    }

    private void initEndDate() {
        mEndCalendar = Calendar.getInstance();
        mEndCalendar.set(endYear, endMonth - 1, endDay, endHours, endMinute);
        mEndTimeMillis = mEndCalendar.getTimeInMillis();
        mTvEndTime.setText(String.format("下次生日：%d-%02d-%02d", endYear, endMonth, endDay));
    }

    /**
     * 计算剩余时间
     */
    private void calculateLeftTime() {
        mCurrentTimeMillis = System.currentTimeMillis();
        if (mCurrentTimeMillis >= mEndTimeMillis) {
            mLeftDay = 0;
            mLeftHour = 0;
        } else {
            long timeMillisLeft = mEndTimeMillis - mCurrentTimeMillis;
            mLeftDay = (int) (timeMillisLeft / 24 / 60 / 60 / 1000);
            mLeftHour = (int) (timeMillisLeft % (24 * 60 * 60 * 1000) / 60 / 60 / 1000);
        }
        loadLeftTime();
    }

    private void loadLeftTime() {
        mTvUnitsOfDay.setText(mLeftDay > 9 ? String.valueOf(mLeftDay % 10) : String.valueOf(mLeftDay));
        mTvTensOfHour.setText(mLeftHour > 9 ? String.valueOf(mLeftHour / 10) : "0");
        mTvUnitsOfHour.setText(mLeftHour > 9 ? String.valueOf(mLeftHour % 10) : String.valueOf(mLeftHour));
        if (mLeftDay > 99) {
            int tens = mLeftDay % 100 / 10;
            mTvTensOfDay.setText(String.valueOf(tens));
            mTvHundredOfDay.setText(String.valueOf(mLeftDay / 100));
        } else {
            mTvTensOfDay.setText(mLeftDay > 9 ? String.valueOf(mLeftDay / 10) : "0");
        }
    }
}
