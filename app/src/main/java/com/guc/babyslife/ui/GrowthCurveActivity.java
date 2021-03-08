package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Constants;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.model.StdData;
import com.guc.babyslife.utils.AssersUtil;
import com.guc.babyslife.widget.GCLineChart;
import com.guc.babyslife.widget.ToolBar;
import com.guc.babyslife.widget.chart.MyMarkerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guc on 2021/2/6.
 * Description：成长曲线
 */
public class GrowthCurveActivity extends BaseActivity {
    private static final int COLOR_STD = Color.parseColor("#1A8C0D");
    private static final int COLOR_BOY = Color.parseColor("#1886f4");
    private static final int COLOR_GIRL = Color.parseColor("#FDADBC");
    @BindView(R.id.toolbar)
    ToolBar mToolBar;
    @BindView(R.id.lineChartHeight)
    GCLineChart mLineChartHeight;
    @BindView(R.id.lineChartWeight)
    GCLineChart mLineChartWeight;
    private List<StdData> mStdDataHeight;//身高标准数据
    private List<StdData> mStdDataWeight;//体重标准数据
    private Baby mBaby;
    private ArrayList<GrowData> mGrowData;

    public static void jump(Context context, Baby baby, ArrayList<GrowData> growData) {
        Intent intent = new Intent(context, GrowthCurveActivity.class);
        intent.putExtra("baby", baby);
        intent.putExtra("data", growData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_curve);
        ButterKnife.bind(this);
        mBaby = getIntent().getParcelableExtra("baby");
        mGrowData = getIntent().getParcelableArrayListExtra("data");
        if (mGrowData != null) {
            Collections.sort(mGrowData);
        }
        initData();
        initView();
    }

    private void initView() {
        mToolBar.setTitle(mBaby.name + "的成长曲线");
        loadChartHeight();
        loadChartWeight();
    }

    private void initData() {
        mStdDataHeight = Constants.GENDER_BOY.equals(mBaby.sex) ? AssersUtil.getStdDatas(this, Constants.STD_BOY_HEIGHT) : AssersUtil.getStdDatas(this, Constants.STD_GIRL_HEIGHT);
        mStdDataWeight = Constants.GENDER_BOY.equals(mBaby.sex) ? AssersUtil.getStdDatas(this, Constants.STD_BOY_WEIGHT) : AssersUtil.getStdDatas(this, Constants.STD_GIRL_WEIGHT);
    }

    /**
     * 加载体重曲线
     */
    private void loadChartWeight() {
        loadChartData(mLineChartWeight, mStdDataWeight, mGrowData, false);
    }

    /**
     * 加载身高曲线
     */
    private void loadChartHeight() {
        loadChartData(mLineChartHeight, mStdDataHeight, mGrowData, true);
    }

    /**
     * 加载表格数据
     *
     * @param chart    表
     * @param stdData  标准数据
     * @param growData 成长记录数据
     * @param isHeight 是否是身高
     */
    private void loadChartData(GCLineChart chart, List<StdData> stdData, List<GrowData> growData, boolean isHeight) {
        MyMarkerView mv = new MyMarkerView(this).setData(mBaby, stdData, growData, isHeight);
        mv.setChartView(chart);
        chart.setMarker(mv);
        chart.setScaleYEnabled(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(30);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int age = (int) value;
                return getXValue(age);
            }
        });
        YAxis yAxisLeft = chart.getAxisLeft();
        YAxis yAxisRight = chart.getAxisRight();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
        yAxisRight.setDrawGridLines(false);

        ArrayList<Entry> yStd = new ArrayList<>();
        for (StdData data : stdData) {
            if (data.age == -1) continue;
            yStd.add(new Entry(data.age, Float.parseFloat(data.average)));
        }

        Entry entry;
        ArrayList<Entry> yData = new ArrayList<>();
        for (int i = 0; i < growData.size(); i++) {
            GrowData data = growData.get(i);
            if (isHeight) {
                entry = new Entry(data.getAge(), data.getHeight());
            } else {
                entry = new Entry(data.getAge(), data.getWeight());
            }
            entry.setData(true);
            yData.add(entry);
        }

        LineDataSet setStd, setData;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            setStd = (LineDataSet) chart.getData().getDataSetByIndex(0);
            setData = (LineDataSet) chart.getData().getDataSetByIndex(1);
            setStd.setValues(yStd);
            setData.setValues(yData);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            setStd = new LineDataSet(yStd, isHeight ? "标准身高" : "标准体重");
            //数据对应的是左边还是右边的Y值
            setStd.setAxisDependency(AxisDependency.LEFT);
            //线条的颜色
            setStd.setColor(COLOR_STD);
            //绘制圆点
            setStd.setCircleColor(COLOR_STD);
            setStd.setDrawCircles(false);
            setStd.setDrawCircleHole(false);
            //不绘制value
            setStd.setDrawValues(false);
            //填充颜色
            setStd.setHighLightColor(Color.rgb(244, 117, 117));

            setData = new LineDataSet(yData, isHeight ? mBaby.name + "的身高记录" : mBaby.name + "的体重记录");
            //数据对应的是左边还是右边的Y值
            setData.setAxisDependency(AxisDependency.LEFT);
            //线条的颜色
            setData.setColor(Constants.GENDER_BOY.equals(mBaby.sex) ? COLOR_BOY : COLOR_GIRL);
            //绘制圆点
            setData.setCircleColor(Constants.GENDER_BOY.equals(mBaby.sex) ? COLOR_BOY : COLOR_GIRL);
            setData.setDrawCircles(true);
            setData.setDrawCircleHole(false);
            //不绘制value
            setData.setDrawValues(true);
            //填充颜色
            setData.setHighLightColor(Color.rgb(244, 117, 117));
        }
        LineData data = new LineData(setStd, setData);
        chart.setData(data);
        chart.setVisibleXRangeMinimum(30 * 12); //设置最小可见为1年 需放到设置数据之后

    }

    private String getXValue(int age) {
        StringBuilder sb = new StringBuilder();
        int year = age / (12 * 30);
        int month = (age % (12 * 30)) / 30;
        if (year == 0) {
            sb.append(month);
            sb.append("月");
        } else {
            sb.append(year);
            sb.append("岁");
            if (month != 0) {
                sb.append(month);
                sb.append("月");
            }
        }
        return sb.toString();
    }

}