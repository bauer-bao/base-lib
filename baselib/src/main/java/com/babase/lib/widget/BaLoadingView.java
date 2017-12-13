package com.babase.lib.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babase.lib.R;

/**
 * @author bauer_bao on 17/12/13.
 */

public class BaLoadingView extends LinearLayout {
    private TextView widgetLoadingTv;
    private ProgressBar widgetLoadingProgressBar;
    private LinearLayout widgetLoadingLinearLayout;

    private Context context;

    public BaLoadingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

    public BaLoadingView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.widget_ba_loading_view, this);
        widgetLoadingLinearLayout = findViewById(R.id.widget_loading_view);
        widgetLoadingProgressBar = findViewById(R.id.widget_loading_progressbar);
        widgetLoadingTv = findViewById(R.id.widget_loading_tv);
    }

    /**
     * 设置数据
     *
     * @param messageId
     */
    public void initData(@StringRes int messageId) {
        widgetLoadingTv.setText(messageId);
    }

    /**
     * 设置数据
     *
     * @param messageStr
     */
    public void initData(String messageStr) {
        widgetLoadingTv.setText(messageStr);
    }

    /**
     * 设置text文字的size
     *
     * @param size
     */
    public void setTextSize(int size) {
        widgetLoadingTv.setTextSize(size);
    }

    /**
     * 设置颜色
     *
     * @param backgroundColor
     * @param textColor
     */
    public void setColorStyle(@ColorRes int backgroundColor, @ColorRes int textColor) {
        widgetLoadingLinearLayout.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        widgetLoadingTv.setTextColor(ContextCompat.getColor(context, textColor));
    }
}
