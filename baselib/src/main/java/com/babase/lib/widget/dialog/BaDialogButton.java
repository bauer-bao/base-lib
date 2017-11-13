package com.babase.lib.widget.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.babase.lib.R;

/**
 * 自定义对话框的确认按钮和取消按钮封装类
 *
 * @author bauer_bao
 */
public class BaDialogButton extends FrameLayout {
    private TextView btnNegative;
    private TextView btnPositive;
    private View view;
    private Context context;
    private OnDialogButtonClickCallBack onDialogButtonClickCallBack;

    public BaDialogButton(Context context) {
        super(context);
    }

    public BaDialogButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View.inflate(getContext(), R.layout.widget_ba_dialog_btn, this);
        btnNegative = findViewById(R.id.puc_dialog_negative_tv);
        btnNegative.setOnClickListener(arg0 -> {
            if (onDialogButtonClickCallBack != null) {
                onDialogButtonClickCallBack.onNegativeButtonClicked();
            }
        });

        btnPositive = findViewById(R.id.puc_dialog_positive_tv);
        btnPositive.setOnClickListener(arg0 -> {
            if (onDialogButtonClickCallBack != null) {
                onDialogButtonClickCallBack.onPositiveButtonClicked();
            }
        });

        view = findViewById(R.id.puc_dialog_middle_line);
    }

    /**
     * 设置监听
     *
     * @param onDialogButtonClickCallBack
     */
    public void setOnClickListener(OnDialogButtonClickCallBack onDialogButtonClickCallBack) {
        this.onDialogButtonClickCallBack = onDialogButtonClickCallBack;
    }

    /**
     * 设置按钮的内容，如果内容为null，则不显示按钮
     *
     * @param positive
     * @param negative
     */
    public void setButtonText(String positive, String negative) {
        if (positive == null && negative == null) {
            setVisibility(View.GONE);
            return;
        }

        //设置取消按钮
        if (negative == null) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setVisibility(View.VISIBLE);
            btnNegative.setText(negative);
        }

        //设置确定按钮
        if (positive == null) {
            btnPositive.setVisibility(View.GONE);
        } else {
            if (negative == null) {
                //一个按钮的对话框
                view.setVisibility(View.GONE);
//                btnPositive.setBackgroundResource(R.drawable.pucdialog_bg_blue_bottom);
            } else {
                //两个按钮
                view.setVisibility(View.VISIBLE);
//                btnPositive.setBackgroundResource(R.drawable.pucdialog_bg_blue_bottom_right);
            }
            btnPositive.setVisibility(View.VISIBLE);
            btnPositive.setText(positive);
        }
    }

    /**
     * 设置按钮的颜色
     *
     * @param positiveColorId
     * @param negativeColorId
     */
    public void setButtonTextColor(int positiveColorId, int negativeColorId) {
        //设置取消按钮颜色
        if (negativeColorId != 0 && btnNegative.isShown()) {
            btnNegative.setTextColor(ContextCompat.getColor(context, negativeColorId));
        }

        //设置确定按钮颜色
        if (positiveColorId != 0 && btnPositive.isShown()) {
            btnPositive.setTextColor(ContextCompat.getColor(context, positiveColorId));
        }
    }

    /**
     * 设置divider颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        if (dividerColor != 0) {
            view.setBackgroundColor(ContextCompat.getColor(context, dividerColor));
        }
    }

    /**
     * 按钮回调
     */
    public interface OnDialogButtonClickCallBack {
        /**
         * 取消按钮
         */
        void onNegativeButtonClicked();

        /**
         * 确定按钮
         */
        void onPositiveButtonClicked();
    }
}