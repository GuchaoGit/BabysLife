package com.guc.babyslife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.guc.babyslife.R;
import com.guc.babyslife.utils.ImageUtils;

/**
 * Created by guc on 2019/12/13.
 * 描述：圆角ImageView
 */
public class CornerImageView extends AppCompatImageView {
    private static final String TAG = "CornerImageView";
    private float width, height;
    private int defaultRadius = 0;
    private boolean isCircle;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public CornerImageView(Context context) {
        this(context, null);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView);
        isCircle = array.getBoolean(R.styleable.CornerImageView_is_circle, false);
        radius = array.getDimensionPixelOffset(R.styleable.CornerImageView_radius, defaultRadius);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.CornerImageView_left_top_radius, defaultRadius);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.CornerImageView_right_top_radius, defaultRadius);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.CornerImageView_left_bottom_radius, defaultRadius);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.CornerImageView_right_bottom_radius, defaultRadius);
        array.recycle();
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        Log.e(TAG, "onLayout: " + width + "height：" + height);
        if (isCircle) {
            int maxRadius = (int) (Math.min(width, height) / 2);
            radius = radius == 0 || radius > maxRadius ? maxRadius : radius;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        if (isCircle) {
            path.addCircle(width / 2, height / 2, radius, Path.Direction.CCW);
        } else {
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);
        }
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    public void setCornerRadius(int dp) {
        this.radius = ImageUtils.dp2px(getContext(), dp);
        this.leftTopRadius = this.leftBottomRadius = this.rightBottomRadius = this.rightTopRadius = this.radius;
        invalidate();
    }
}
