package com.guc.babyslife.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.guc.babyslife.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2019/7/15.
 * 描述：标题栏
 */
public class ToolBar extends FrameLayout {
    private static final int FUNCTION_NONE = 0;
    private static final int FUNCTION_FINISH = 1;
    private static final int FUNCTION_FUN = 2;
    private static final int FUNCTION_ICON = 1;
    private static final int FUNCTION_STRING = 2;

    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tv_common_header_title)
    TextView mTvTitle;
    @BindView(R.id.iv_function_right)
    ImageView mIvFunctionRight;
    @BindView(R.id.rl_function_right)
    RelativeLayout mRlFunctionRight;
    @BindView(R.id.rl_function_left)
    RelativeLayout mRlFunctionLeft;
    @BindView(R.id.tv_right_function)
    TextView mTvRightFunction;
    private Context mCxt;

    private int mLeftFunType;
    private int mLeftIconId;
    private int mRightFunType;
    private int mRightIconId;
    private int mTitleTextColor;
    private int mTitleTextSize;
    private CharSequence mTitle;
    private CharSequence mRightFuncitonString;
    private OnLeftClickedListener mOnLeftClickedListener;
    private OnRightClickedListener mOnRightClickedListener;

    public ToolBar(@NonNull Context context) {
        this(context, null);
    }

    public ToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCxt = context;
        View.inflate(context, R.layout.layout_toolbar, this);
        ButterKnife.bind(this);
        initAttrs(context, attrs);
        initView();
    }

    @SuppressWarnings("unused")
    public void setOnLeftClickedListener(OnLeftClickedListener onLeftClickedListener){
        this.mOnLeftClickedListener = onLeftClickedListener;
    }
    @SuppressWarnings("unused")
    public void setOnRightClickedListener(OnRightClickedListener onRightClickedListener){
        this.mOnRightClickedListener = onRightClickedListener;
    }
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
        mLeftFunType = a.getInt(R.styleable.ToolBar_leftType, 0);
        mLeftIconId = a.getResourceId(R.styleable.ToolBar_leftIcon,R.drawable.selector_btn_back);
        mTitle = a.getText(R.styleable.ToolBar_title);
        mTitleTextColor = a.getColor(R.styleable.ToolBar_titleTextColor,getResources().getColor(R.color.colorWhite));
        mTitleTextSize = a.getDimensionPixelSize(R.styleable.ToolBar_titleTextSize,sp2px(14));
        mTitleTextSize = px2sp(mTitleTextSize);
        mRightFunType = a.getInt(R.styleable.ToolBar_rightType, 0);
        mRightIconId = a.getResourceId(R.styleable.ToolBar_rightIcon,R.drawable.selector_btn_more);
        mRightFuncitonString = a.getText(R.styleable.ToolBar_rightString);
        a.recycle();
    }
    private void initView(){
        switch (mLeftFunType){
            case FUNCTION_NONE:
                mRlFunctionLeft.setVisibility(View.GONE);
                break;
            case FUNCTION_FINISH:
                mRlFunctionLeft.setVisibility(View.VISIBLE);
                break;
            case FUNCTION_FUN:
                mRlFunctionLeft.setVisibility(View.VISIBLE);
                break;
        }
        mIvFunctionLeft.setBackgroundResource(mLeftIconId);
        mTitle = mTitle == null?"暂无":mTitle;
        mRightFuncitonString = mRightFuncitonString == null ? "新增" : mRightFuncitonString;
        mTvTitle.setText(mTitle);
        mTvRightFunction.setText(mRightFuncitonString);
        mTvTitle.setTextColor(mTitleTextColor);
        mTvTitle.setTextSize(mTitleTextSize);
        switch (mRightFunType){
            case FUNCTION_NONE:
                mRlFunctionRight.setVisibility(View.GONE);
                mTvRightFunction.setVisibility(View.GONE);
                break;
            case FUNCTION_ICON:
                mRlFunctionRight.setVisibility(View.VISIBLE);
                mTvRightFunction.setVisibility(View.GONE);
                mIvFunctionRight.setBackgroundResource(mRightIconId);
                break;
            case FUNCTION_STRING:
                mRlFunctionRight.setVisibility(View.GONE);
                mTvRightFunction.setVisibility(View.VISIBLE);
                break;
        }
    }
    @OnClick({R.id.rl_function_left, R.id.rl_function_right, R.id.tv_right_function})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_function_left:
                onLeftClicked();
                break;
            case R.id.rl_function_right:
            case R.id.tv_right_function:
                onRightClicked();
                break;
        }
    }

    /**
     *处理右侧功能点击事件
     */
    private void onRightClicked() {
        if (mRightFunType !=FUNCTION_NONE && mOnRightClickedListener!=null){
            mOnRightClickedListener.onRightClicked();
        }
    }

    /**
     * 处理左侧点击事件
     */
    private void onLeftClicked() {
        if(mLeftFunType == FUNCTION_FINISH){
            Activity mCurAty = (Activity) mCxt;
            mCurAty.finish();
        }else if (mLeftFunType ==FUNCTION_FUN){
            if (mOnLeftClickedListener!=null){
                mOnLeftClickedListener.onLeftClicked();
            }
        }
    }
    @SuppressWarnings("SameParameterValue")
    private int sp2px( float spValue) {
        float fontScale = mCxt.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    private int px2sp(float pxValue) {
        float fontScale = mCxt.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public interface OnLeftClickedListener {
        void onLeftClicked();
    }

    public interface OnRightClickedListener {
        void onRightClicked();
    }

    public void setTitle(String title) {
        if (title.isEmpty()) return;
        mTitle = title;
        mTvTitle.setText(mTitle);
    }

    @SuppressWarnings("unused")
    public void setTitle(int resid) {
        setTitle(mCxt.getResources().getString(resid));
    }
}
