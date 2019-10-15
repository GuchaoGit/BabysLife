package com.guc.babyslife.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.guc.babyslife.system.BaseSystem;
import com.guc.babyslife.system.SystemManager;
import com.guc.babyslife.system.SystemPages;

/**
 * Created by guc on 2019/10/15.
 * 描述：base Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSystem(SystemPages.class).addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSystem(SystemPages.class).removeActivity(this);
    }

    protected <T extends BaseSystem> T getSystem(Class<T> system) {
        return SystemManager.getInstance().getSystem(system);
    }
}
