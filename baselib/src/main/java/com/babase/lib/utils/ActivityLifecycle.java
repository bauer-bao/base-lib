package com.babase.lib.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author bauer on 2018/2/27.
 */

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    /**
     * 应用是否处于前端
     */
    private boolean isForeground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isForeground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isForeground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isForeground() {
        return isForeground;
    }
}