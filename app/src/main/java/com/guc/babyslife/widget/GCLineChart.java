package com.guc.babyslife.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

/**
 * Created by guc on 2021/2/6.
 * Description：
 */
public class GCLineChart extends LineChart {
    public GCLineChart(Context context) {
        this(context, null);
    }

    public GCLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GCLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initChart();
    }

    private void initChart() {
        setNoDataText("暂无数据");
        animateY(1000, Easing.EaseInOutQuad);//设置加载动画
        setDrawBorders(false); // 不绘制边框线
        setTouchEnabled(true); //可触摸
        setDescription(null);//不显示描述信息
        setDoubleTapToZoomEnabled(false);//禁止双击放大
        Legend legend = getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
        legend.setDrawInside(false); //表头的说明是否绘制在表内部

        setScaleEnabled(true); //可缩放
        setDragEnabled(true); //可拖拽
        setPinchZoom(false); //false代表缩放时X与Y轴分开缩放,true代表同时缩放
        //X轴设置
        XAxis xAxis = getXAxis();
        xAxis.setLabelCount(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);//设置轴的值之间的最小间隔。这可以用来避免价值复制当放大到一个地步，小数设置轴不再数允许区分两轴线之间的值。
        xAxis.setGranularity(1f);
        YAxis yAxisLeft = getAxisLeft();
        yAxisLeft.setAxisMinimum(0f); //最小值
        yAxisLeft.setDrawGridLines(true); //是否绘制Y轴网格线

        YAxis yAxisRight = getAxisRight();
        yAxisRight.setDrawAxisLine(false); //不绘制
        yAxisRight.setEnabled(false);
    }
}
