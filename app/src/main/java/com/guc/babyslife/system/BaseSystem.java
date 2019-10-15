package com.guc.babyslife.system;

/**
 * Created by guc on 2019/10/15.
 * 描述：系统基类
 */
public abstract class BaseSystem implements ISystem {
    @Override
    public void createSystem() {
        init();
    }

    @Override
    public void destroySystem() {
        destroy();
    }

    protected abstract void init();

    protected abstract void destroy();

    protected <T extends BaseSystem> T getSystem(Class<T> className) {
        return SystemManager.getInstance().getSystem(className);
    }
}
