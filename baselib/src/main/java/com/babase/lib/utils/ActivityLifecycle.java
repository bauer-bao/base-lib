package com.babase.lib.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author bauer on 2018/2/27.
 */

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private LifeCycleListener lifeCycleListener;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    private int created;

    public ActivityLifecycle(LifeCycleListener lifeCycleListener) {
        this.lifeCycleListener = lifeCycleListener;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        created++;
        lifeCycleListener.onCreate();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        started++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        resumed++;
        lifeCycleListener.onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused++;
        lifeCycleListener.onPause();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        stopped++;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 判断app是否是在前台，如果以pause和resume区分的话，因为页面切换的时候pause和resume有个时间差，在这时间差内结果不准确
     * resume和pause：页面是否有焦点
     * start和stop：页面是否可见
     * 此处，可见即可用来判断是否在前台。
     *
     * @return
     */
    public boolean isForeground() {
        return started > stopped;
    }

    public interface LifeCycleListener {
        void onCreate();

        void onPause();

        void onResume();
    }
}