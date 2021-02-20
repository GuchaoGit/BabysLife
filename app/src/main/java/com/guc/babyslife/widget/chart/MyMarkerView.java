package com.guc.babyslife.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.guc.babyslife.R;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.model.StdData;

import java.util.List;

/**
 * Created by guc on 2021/2/19.
 * Description：图表 MarkerView
 */
public class MyMarkerView extends MarkerView {
    private final int DEFAULT_INDICATOR_COLOR = 0xffFD9138;//指示器默认的颜色
    private final int ARROW_HEIGHT = dp2px(5); // 箭头的高度
    private final int ARROW_WIDTH = dp2px(10); // 箭头的宽度
    private final float ARROW_OFFSET = dp2px(2);//箭头偏移量
    private final float BG_CORNER = dp2px(2);//背景圆角
    private TextView tvContent;
    private boolean isHeight = false; //是否是身高
    private List<StdData> mStdData;
    private List<GrowData> mGrowData;
    private Baby mBaby;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
    }

    public MyMarkerView(Context context) {
        this(context, R.layout.custom_marker_view);
    }

    public MyMarkerView setData(Baby baby, List<StdData> stdData, List<GrowData> growData, boolean isHeight) {
        this.mBaby = baby;
        this.mStdData = stdData;
        this.mGrowData = growData;
        this.isHeight = isHeight;
        return this;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        StringBuilder sbContent = new StringBuilder();
        int age = 0;
        boolean isRecord = e.getData() != null;
        String ageDesc = "";
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            age = (int) (ce.getX());
            sbContent.append(ce.getHigh() + " ");
        } else {
            age = (int) (e.getX());
            sbContent.append(e.getY() + " ");
        }
        ageDesc = getAgeDesc(age, isRecord);
        sbContent.append(isHeight ? "cm" : "kg");
        if (!TextUtils.isEmpty(ageDesc)) {
            sbContent.append("\n");
            sbContent.append(ageDesc);
        }
        tvContent.setText(sbContent.toString());
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Chart chart = getChartView();
        if (chart == null) {
            super.draw(canvas, posX, posY);
            return;
        }

        //指示器背景画笔
        Paint bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(DEFAULT_INDICATOR_COLOR);
        //剪头画笔
        Paint arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(DEFAULT_INDICATOR_COLOR);

        float width = getWidth();
        float height = getHeight();

        int saveId = canvas.save();
        //移动画布到点并绘制点
        canvas.translate(posX, posY);
        //画指示器
        Path path = new Path();
        RectF bRectF;
        if (posY < height + ARROW_HEIGHT + ARROW_OFFSET) {//处理超过上边界
            //移动画布并绘制三角形和背景
            canvas.translate(0, height + ARROW_HEIGHT + ARROW_OFFSET);
            path.moveTo(0, -(height + ARROW_HEIGHT));
            path.lineTo(ARROW_WIDTH / 2f, -(height - BG_CORNER));
            path.lineTo(-ARROW_WIDTH / 2f, -(height - BG_CORNER));
            path.lineTo(0, -(height + ARROW_HEIGHT));

            bRectF = new RectF(-width / 2, -height, width / 2, 0);

            canvas.drawPath(path, arrowPaint);
            canvas.drawRoundRect(bRectF, BG_CORNER, BG_CORNER, bgPaint);
            canvas.translate(-width / 2f, -height);
        } else {//没有超过上边界
            //移动画布并绘制三角形和背景
            canvas.translate(0, -height - ARROW_HEIGHT - ARROW_OFFSET);
            path.moveTo(0, height + ARROW_HEIGHT);
            path.lineTo(ARROW_WIDTH / 2f, height - BG_CORNER);
            path.lineTo(-ARROW_WIDTH / 2f, height - BG_CORNER);
            path.lineTo(0, height + ARROW_HEIGHT);

            bRectF = new RectF(-width / 2, 0, width / 2, height);

            canvas.drawPath(path, arrowPaint);
            canvas.drawRoundRect(bRectF, BG_CORNER, BG_CORNER, bgPaint);
            canvas.translate(-width / 2, 0);
        }
        draw(canvas);
        canvas.restoreToCount(saveId);
    }

    private int dp2px(int dpValues) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValues,
                getResources().getDisplayMetrics());
    }

    /**
     * 获取年龄描述
     *
     * @param age      年龄（天）
     * @param isRecord 是否是自己记录的
     * @return 返回相应的年龄描述
     */
    private String getAgeDesc(int age, boolean isRecord) {
        String desc = "";
        if (isRecord) {
            for (GrowData data : mGrowData) {
                if (data.getAge() == age) {
                    return data.getAgeDesc();
                }
            }
        } else {
            for (StdData data : mStdData) {
                if (age == data.age) {
                    return data.ageDesc;
                }
            }
        }
        return desc;
    }
}
