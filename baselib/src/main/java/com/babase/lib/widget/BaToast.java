package com.babase.lib.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.ScreenUtil;

/**
 * 自定义Toast, 可以绕开通知权限
 * <p>
 * How To Use:
 * 1. PWToast pwToast = new PWToast(context);
 * pwToast.setTextAndShow(); 注意：可选择自己需要的方法
 * <p>
 * 2. PWToast.getInstance(context).setTextAndShow(); 注意：可选择自己需要的方法
 * 此方法无法cancel，如果需要cancel toast的话，请使用方式1
 */
public class BaToast implements Handler.Callback {

    private static final int DISMISS_TOAST = 111;
    private WindowManager manger;
    private WindowManager.LayoutParams params;
    /**
     * 如果要用timer，可能会造成内存泄露，就需要cancel timer。因此此处使用handler
     */
    private Handler handler;
    private static volatile BaToast baToast;
    private View contentView;
    private TextView textView;
    private Context context;
    private OnDismissListener listener;
    /**
     * 提示的时间
     */
    private int showTime = 1000;

    public static BaToast getInstance(Context context) {
        if (baToast == null) {
            synchronized (BaToast.class) {
                if (baToast == null) {
                    baToast = new BaToast(context);
                }
            }
        }
        return baToast;
    }

    public BaToast(Context context) {
        this.context = context;
        initView(context);
    }

    public void setListener(OnDismissListener listener) {
        this.listener = listener;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    private void initView(Context context) {
        manger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        handler = new Handler(this);

        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //由layout文件创建一个View对象
        contentView = inflater.inflate(R.layout.widget_ba_toast, null);
        textView = contentView.findViewById(R.id.toast_tv);

        // 设置toast位置
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = -1;
        params.setTitle("BToast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.FILL_HORIZONTAL | Gravity.BOTTOM;
        params.y = ScreenUtil.dip2px(context, 100);
    }

    /**
     * 设置为中间显示的模式
     */
    public void setCenterMode() {
        int padding = ScreenUtil.dip2px(context, 30);
        textView.setPadding(padding, padding, padding, padding);

        params.gravity = Gravity.NO_GRAVITY;
        params.y = ScreenUtil.dip2px(context, 0);
    }

    /**
     * 设置样式
     *
     * @param backgroundId
     * @param textColor
     * @param size
     */
    public void setTextViewStyle(@DrawableRes int backgroundId, @ColorRes int textColor, int size) {
        textView.setBackgroundResource(backgroundId);
        textView.setTextColor(ContextCompat.getColor(context, textColor));
        textView.setTextSize(14);
    }

    /**
     * 显示toast
     *
     * @param str
     * @param time
     */
    public void setTextAndShow(String str, int time) {
        textView.setText(str);
        setTimeAndShow(time);
    }

    /**
     * 显示toast
     *
     * @param stringId
     * @param time
     */
    public void setTextAndShow(@StringRes int stringId, int time) {
        textView.setText(stringId);
        setTimeAndShow(time);
    }

    /**
     * 显示toast, 默认LENGTH_SHORT时间
     *
     * @param msg
     */
    public void setTextAndShow(Object msg) {
        if (msg instanceof Integer) {
            setTextAndShow((int) msg);
        } else {
            setTextAndShow((String) msg);
        }
    }

    /**
     * 显示toast, 默认LENGTH_SHORT时间
     *
     * @param str
     */
    public void setTextAndShow(String str) {
        setTextAndShow(str, showTime);
    }

    /**
     * 显示toast, 默认LENGTH_SHORT时间
     *
     * @param stringId
     */
    public void setTextAndShow(@StringRes int stringId) {
        setTextAndShow(stringId, showTime);
    }

    /**
     * 设置时间
     *
     * @param time
     */
    private void setTimeAndShow(int time) {
        //不为空，并且没有父控件
        if (contentView != null && contentView.getParent() == null) {
            if (time < 1000) {
                //至少1000ms以上
                time = 1000;
            }
            show();
            handler.sendEmptyMessageDelayed(DISMISS_TOAST, time);
        }
    }

    /**
     * show
     */
    private void show() {
        manger.addView(contentView, params);
    }

    /**
     * 取消显示toast
     */
    public void cancelShow() {
        try {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            manger.removeView(contentView);
            if (listener != null) {
                listener.toastDismiss();
            }
        } catch (IllegalArgumentException e) {
            //这边由于上下文被销毁后removeView可能会抛出IllegalArgumentException
            //暂时这么处理，因为EToast2是轻量级的，不想和Context上下文的生命周期绑定在一块儿
            //其实如果真的想这么做，可以参考博文2的第一种实现方式，添加一个空的fragment来做生命周期绑定
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        cancelShow();
        return false;
    }

    /**
     * 消失回调
     */
    public interface OnDismissListener {
        void toastDismiss();
    }
}
