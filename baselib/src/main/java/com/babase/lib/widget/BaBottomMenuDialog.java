package com.babase.lib.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.Logger;
import com.babase.lib.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * 底部菜单 采用BottomSheetDialog
 *
 * @author bauer on 2018/1/1.
 */

public class BaBottomMenuDialog extends BottomSheetDialogFragment {
    /**
     * item内容
     */
    private ArrayList<String> items;
    /**
     * 标题
     */
    private String titleStr;
    /**
     * 取消
     */
    private String cancelStr;
    /**
     * item的颜色
     */
    @ColorInt
    private int contentColor;

    /**
     * title的颜色
     */
    @ColorInt
    private int titleColor = Color.GRAY;
    /**
     * cancel的颜色
     */
    @ColorInt
    private int cancelColor = Color.GRAY;

    /**
     * 标题的图标
     */
    @DrawableRes
    private int titleDrawable;
    private OnBaBottomMenuClickListener listener;
    /**
     * 背景颜色
     */
    @DrawableRes
    private int contentBgDrawable;
    /**
     * 标题的对齐方式
     */
    private int titleGravity;

    private View rootView;
    protected BaBottomSheetDialog dialog;
    private Context mContext;

    protected BottomSheetBehavior mBehavior;

    public BaBottomMenuDialog() {
    }

    @SuppressLint("ValidFragment")
    public BaBottomMenuDialog(BaBottomMenuBuilder builder) {
        items = builder.items;
        titleStr = builder.titleStr;
        cancelStr = builder.cancelStr;
        contentColor = builder.contentColor;
        titleColor = builder.titleColor;
        cancelColor = builder.cancelColor;
        titleDrawable = builder.titleDrawable;
        listener = builder.listener;
        contentBgDrawable = builder.contentBgDrawable;
        titleGravity = builder.titleGravity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new BaBottomSheetDialog(mContext, R.style.BottomSheetDialogStyle);
        if (rootView == null) {
            //缓存下来的View 当为空时才需要初始化 并缓存
            initView();
        }
        dialog.setContentView(rootView);
        mBehavior = dialog.getBottomSheetBehavior();
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除缓存View和当前ViewGroup的关联
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }

    /**
     * 初始化view
     */
    private void initView() {
        rootView = View.inflate(mContext, R.layout.widget_bottom_menu_dialog, null);
        LinearLayout linearLayout = rootView.findViewById(R.id.bsd_content_ll);
        TextView titleTv = rootView.findViewById(R.id.bsd_title_tv);
        TextView cancelTv = rootView.findViewById(R.id.bsd_cancel_tv);
        View contentBgV = rootView.findViewById(R.id.bsd_content_bg_v);
        cancelTv.setOnClickListener(view -> {
            if (listener != null) {
                listener.onCancelClick();
                dismiss();
            }
        });
        titleTv.setText(titleStr);
        titleTv.setGravity(titleGravity);
        titleTv.setTextColor(titleColor);
        if (titleDrawable != 0) {
            titleTv.setCompoundDrawablesRelative(ContextCompat.getDrawable(mContext, titleDrawable), null, null, null);
        }
        cancelTv.setText(cancelStr);
        cancelTv.setTextColor(cancelColor);
        if (contentBgDrawable != 0) {
            contentBgV.setBackgroundResource(contentBgDrawable);
        }

        int padding = ScreenUtil.dip2px(mContext, 15);
        for (int i = 0; i < items.size(); i++) {
            final int position = i;
            TextView childView = new TextView(mContext);
            childView.setText(items.get(i));
            childView.setGravity(Gravity.CENTER);
            childView.setPadding(padding, padding, padding, padding);
            childView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onContentClick(position);
                    dismiss();
                }
            });
            childView.setTextColor(contentColor);
            childView.setTextSize(16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(childView, params);
        }
    }

    /**
     * 显示dialog
     *
     * @param manager
     */
    public void show(FragmentManager manager) {
        if (!this.isAdded()) {
            show(manager, "baBottomMenuDialog");
        }
    }

    /**
     * 构建build
     */
    public static class BaBottomMenuBuilder {
        /**
         * item内容
         */
        private ArrayList<String> items;
        /**
         * 标题
         */
        private String titleStr;

        /**
         * 取消
         */
        private String cancelStr;
        /**
         * item的颜色
         */
        @ColorInt
        private int contentColor = Color.GRAY;
        /**
         * title的颜色
         */
        @ColorInt
        private int titleColor = Color.GRAY;
        /**
         * cancel的颜色
         */
        @ColorInt
        private int cancelColor = Color.GRAY;
        /**
         * 标题的图标
         */
        @DrawableRes
        private int titleDrawable;
        private OnBaBottomMenuClickListener listener;
        /**
         * 背景颜色
         */
        @DrawableRes
        private int contentBgDrawable;
        /**
         * 标题的对齐方式
         */
        private int titleGravity = Gravity.LEFT;

        public BaBottomMenuBuilder() {
            items = new ArrayList<>();
        }

        public BaBottomMenuBuilder addItem(String title) {
            items.add(title);
            return this;
        }

        public BaBottomMenuBuilder setTitleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public BaBottomMenuBuilder setCancelStr(String cancelStr) {
            this.cancelStr = cancelStr;
            return this;
        }

        public BaBottomMenuBuilder setContentColor(@ColorInt int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public BaBottomMenuBuilder setTitleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public BaBottomMenuBuilder setCancelColor(@ColorInt int cancelColor) {
            this.cancelColor = cancelColor;
            return this;
        }

        public BaBottomMenuBuilder setTitleDrawable(@DrawableRes int titleDrawable) {
            this.titleDrawable = titleDrawable;
            return this;
        }

        public BaBottomMenuBuilder setListener(OnBaBottomMenuClickListener listener) {
            this.listener = listener;
            return this;
        }

        public BaBottomMenuBuilder setBgDrawable(@DrawableRes int contentBgDrawable) {
            this.contentBgDrawable = contentBgDrawable;
            return this;
        }

        public BaBottomMenuBuilder setTitleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return this;
        }

        public BaBottomMenuDialog build() {
            if (items == null || items.isEmpty()) {
                Logger.d("can not empty items");
            }
            return new BaBottomMenuDialog(this);
        }
    }

    public interface OnBaBottomMenuClickListener {
        /**
         * item的点击
         */
        void onContentClick(int position);

        /**
         * 取消的点击
         */
        void onCancelClick();
    }
}