package com.babase.lib.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.babase.lib.utils.Logger;
import com.babase.lib.utils.ScreenUtil;

/**
 * @author bauer on 2017/8/18.
 */

public class BaBottomSheetDialog extends BottomSheetDialog {
    private BottomSheetBehavior bottomSheetBehavior;
    /**
     * 是否在滑动中
     */
    protected boolean isSliding = false;

    public BaBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public BaBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认下对齐，一面造成和底部有一定的距离
        getWindow().setGravity(Gravity.BOTTOM);
        //解决弹出bsd之后，状态栏是黑色的问题。原理就是，设置window的布局，使得把状态栏顶出去
        int dialogHeight = ScreenUtil.getScreenHeight(getContext()) - ScreenUtil.getStatusBarHeight(getContext());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bottomSheetBehavior == null) {
            //解决弹出之后，如果用手势把对话框消失，则再次弹出的时候，只有阴影，对话框不会弹出的问题
            View view = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
            bottomSheetBehavior = BottomSheetBehavior.from(view);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    Logger.d("state change--->" + newState);
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED && !isSliding) {
                        //测试发现，如果内容多，可能再次展开的时候会默认折叠状态（其他地方有再次设置为打开状态，被自动改为了折叠状态），所以此处处理一下。同时需要处理滑动的情况
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    isSliding = true;
                }
            });
        }
        //默认展开，只能保证第一次是展开的状态。
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public BottomSheetBehavior getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }
}