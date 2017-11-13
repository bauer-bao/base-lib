package com.babase.lib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.Iterator;
import java.util.Stack;

/**
 * Activity 管理类
 *
 * @author bauer_bao
 */
public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }

        if (!mActivityStack.contains(activity)) {
            mActivityStack.add(activity);
        }
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 获取在栈中的activity的总数量
     *
     * @return
     */
    public int getActivityCount() {
        return mActivityStack.size();
    }

    /**
     * 检查是否有对应的Activity
     *
     * @param cls
     * @return
     */
    public boolean checkActivity(Class<?> cls) {
        for (Activity act : mActivityStack) {
            if (act.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            if (!activity.isFinishing()) {
                //如果正在finish的话，就不需要再finish
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 结束除了specialActivity之外的其他的activity
     *
     * @param specialActivity 指定的activity
     */
    public void killOtherActivity(Class<?> specialActivity) {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(specialActivity)) {
                activity.finish();
                iterator.remove();
            }
        }
    }

    /**
     * 退出应用程序，并且杀死进程
     */
    public void appExit(Context context) {
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出应用程序，logout的时候，不需要将进程杀死，否则会有一段时间的黑屏
     */
    public void appLogout() {
        killAllActivity();
    }
}
