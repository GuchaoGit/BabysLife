package com.guc.babyslife.system;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.guc.babyslife.app.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guc on 2019/10/15.
 * 描述：Activity 管理类
 */
public class SystemPages extends BaseSystem {
    private static final String TAG = "SystemPages";
    private List<Activity> mList;
    private Activity mCurrActivity;
    @Override
    protected void init() {
        mList = new ArrayList<>();
    }

    @Override
    protected void destroy() {
        finishAllActivity();
        mCurrActivity = null;
        mList = null;
    }
    public void addActivity(Activity activity) {
        if (activity != null) {
            Logger.d(TAG, "addActivity name=" + activity.getClass().getSimpleName());
            mList.add(activity);
            mCurrActivity = activity;
        }
    }
    public void removeActivity(Activity activity) {
        if (activity != null) {
            Logger.d(TAG, "finishActivity name=" + activity.getClass().getSimpleName());
            mList.remove(activity);
            if (mList != null && mList.size() > 0) {
                mCurrActivity = mList.get(mList.size() - 1);//定位到最后一个存活的act
            } else {
                mCurrActivity = null;
            }
        }
    }
    //获取当前Activity
    public Activity getCurrActivity() {
        return mCurrActivity;
    }
    //获取当前Activity  并转为FragmentActivity
    public FragmentActivity getCurrFragmentActivity() {
        return (FragmentActivity) mCurrActivity;
    }
    public void finishAllActivity() {
        if (mList == null || mList.size() < 1) return;
        for (Activity activity : mList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        mList.clear();
    }
}
