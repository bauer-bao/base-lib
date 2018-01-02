package com.babase.lib.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetBehavior;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.LibUtil;
import com.babase.lib.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * 底部菜单 采用BottomSheetDialog
 *
 * @author bauer on 2018/1/1.
 */

public class BaBottomMenuDialog extends BaBottomSheetDialog {
    /**
     * item内容
     */
    private ArrayList<Item> items = new ArrayList<>();
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
    private int titleGravity = Gravity.START | Gravity.CENTER_VERTICAL;

    /**
     * 显示标题
     */
    private boolean showTitle = true;

    private View rootView;
    private LinearLayout linearLayout;
    private TextView titleTv;
    private TextView cancelTv;
    private View titleLine;
    private View contentBgV;
    private Context mContext;

    private int padding;

    protected BottomSheetBehavior mBehavior;

    public BaBottomMenuDialog(Context context) {
        super(context, R.style.BottomSheetDialogStyle);
        mContext = context;
    }

    /**
     * 删除item
     *
     * @return
     */
    public BaBottomMenuDialog clearItem() {
        items.clear();
        return this;
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @return
     */
    public BaBottomMenuDialog addItem(String itemStr) {
        Item item1 = new Item();
        item1.setItemStr(itemStr);
        item1.setItemColor(contentColor);
        item1.setItemSize(16);
        items.add(item1);
        return this;
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @param itemColor
     * @return
     */
    public BaBottomMenuDialog addItem(String itemStr, @ColorInt int itemColor) {
        Item item1 = new Item();
        item1.setItemStr(itemStr);
        item1.setItemColor(itemColor);
        item1.setItemSize(16);
        items.add(item1);
        return this;
    }

    /**
     * 设置title
     *
     * @param titleStr
     * @return
     */
    public BaBottomMenuDialog setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    /**
     * 设置cancel
     *
     * @param cancelStr
     * @return
     */
    public BaBottomMenuDialog setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
        return this;
    }

    /**
     * 设置取消颜色
     *
     * @param contentColor
     * @return
     */
    public BaBottomMenuDialog setContentColor(@ColorInt int contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    /**
     * 设置title颜色
     *
     * @param titleColor
     * @return
     */
    public BaBottomMenuDialog setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * 设置取消颜色
     *
     * @param cancelColor
     * @return
     */
    public BaBottomMenuDialog setCancelColor(@ColorInt int cancelColor) {
        this.cancelColor = cancelColor;
        return this;
    }

    /**
     * 设置title图标
     *
     * @param titleDrawable
     * @return
     */
    public BaBottomMenuDialog setTitleDrawable(@DrawableRes int titleDrawable) {
        this.titleDrawable = titleDrawable;
        return this;
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public BaBottomMenuDialog setListener(OnBaBottomMenuClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 设置背景
     *
     * @param contentBgDrawable
     * @return
     */
    public BaBottomMenuDialog setBgDrawable(@DrawableRes int contentBgDrawable) {
        this.contentBgDrawable = contentBgDrawable;
        return this;
    }

    /**
     * 设置title对齐方式
     *
     * @param titleGravity
     * @return
     */
    public BaBottomMenuDialog setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }

    /**
     * 设置是否显示title
     *
     * @param showTitle
     * @return
     */
    public BaBottomMenuDialog setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

    /**
     * 创建dialog
     *
     * @return
     */
    public BaBottomMenuDialog onCreateDialog() {
        if (rootView == null) {
            //缓存下来的View 当为空时才需要初始化 并缓存
            initView();
        }
        setContentView(rootView);
        setOnDismissListener(dialogInterface -> {
            if (listener != null) {
                listener.onDismiss();
            }
        });
        mBehavior = getBottomSheetBehavior();
        return this;
    }

    /**
     * 初始化view
     */
    private void initView() {
        rootView = View.inflate(mContext, R.layout.widget_bottom_menu_dialog, null);
        linearLayout = rootView.findViewById(R.id.bsd_content_ll);
        titleTv = rootView.findViewById(R.id.bsd_title_tv);
        cancelTv = rootView.findViewById(R.id.bsd_cancel_tv);
        titleLine = rootView.findViewById(R.id.bsd_title_line_v);
        contentBgV = rootView.findViewById(R.id.bsd_content_bg_v);
        cancelTv.setOnClickListener(view -> dismiss());
        padding = ScreenUtil.dip2px(mContext, 15);
    }

    /**
     * 更新设置各个控件
     */
    public void updateInfo() {
        titleTv.setText(titleStr);
        titleTv.setGravity(titleGravity);
        titleTv.setTextColor(titleColor);
        if (titleDrawable != 0) {
            titleTv.setCompoundDrawablesRelative(LibUtil.tintDrawable(mContext, titleDrawable, R.color.lib_ba_black, false), null, null, null);
        }
        if (!showTitle) {
            titleTv.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);
        }
        cancelTv.setText(cancelStr);
        cancelTv.setTextColor(cancelColor);
        if (contentBgDrawable != 0) {
            contentBgV.setBackgroundResource(contentBgDrawable);
        }

        linearLayout.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            final int position = i;
            TextView childView = new TextView(mContext);
            childView.setText(item.getItemStr());
            childView.setTextColor(item.getItemColor());
            childView.setTextSize(item.getItemSize());
            childView.setGravity(Gravity.CENTER);
            childView.setPadding(padding, padding, padding, padding);
            childView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onContentClick(position);
                    dismiss();
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(childView, params);
        }
    }

    public class Item {
        private String itemStr;
        private int itemColor;
        private int itemSize;

        public String getItemStr() {
            return itemStr;
        }

        public void setItemStr(String itemStr) {
            this.itemStr = itemStr;
        }

        public int getItemColor() {
            return itemColor;
        }

        public void setItemColor(int itemColor) {
            this.itemColor = itemColor;
        }

        public int getItemSize() {
            return itemSize;
        }

        public void setItemSize(int itemSize) {
            this.itemSize = itemSize;
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
        void onDismiss();
    }
}