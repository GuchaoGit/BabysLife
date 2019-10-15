package com.guc.babyslife.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by guc on 2019/10/15.
 * 描述：系统管理类
 */
public class SystemManager {
    private static SystemManager mInstance;
    private HashMap<String, BaseSystem> mSystemPool;

    private SystemManager() {
        mSystemPool = new HashMap<>();
    }
    public static SystemManager getInstance() {
        if (mInstance == null) {
            synchronized (SystemManager.class) {
                mInstance = new SystemManager();
            }
        }
        return mInstance;
    }
    public <T> void destroySystem(Class<T> className) {
        BaseSystem baseSystem;
        if ((baseSystem = mSystemPool.get(className.getName())) != null) {
            baseSystem.destroySystem();
            mSystemPool.remove(baseSystem);
        }
    }

    public void destroyAllSystem() {
        Iterator<Map.Entry<String, BaseSystem>> iterator = mSystemPool.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BaseSystem> next = iterator.next();
            next.getValue().destroySystem();
        }
        mSystemPool.clear();
    }

    public <T extends BaseSystem> T getSystem(Class<T> className) {
        if (className == null) {
            return null;
        }
        T instance = (T) mSystemPool.get(className.getName());
        if (instance == null) {
            try {
                instance = className.newInstance();
                instance.createSystem();
                mSystemPool.put(className.getName(), instance);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return instance;
    }
}
