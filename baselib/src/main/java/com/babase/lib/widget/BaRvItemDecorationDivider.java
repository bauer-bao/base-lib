package com.babase.lib.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author bauer on 2017/8/23.
 */

public class BaRvItemDecorationDivider extends RecyclerView.ItemDecoration {
    /**
     * 左右边距
     */
    private int paddingLeftRight;
    /**
     * 分割线高度
     */
    private int dividerHeight;
    /**
     * 控件上下是否也要divider
     */
    private boolean paddingMode;
    private Paint mPaint;

    public BaRvItemDecorationDivider(int paddingLeftRight, int dividerHeight, int dividerColor, boolean paddingMode) {
        this.paddingLeftRight = paddingLeftRight;
        this.dividerHeight = dividerHeight;
        this.paddingMode = paddingMode;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        //如果不是paddingMode，因为是上边距，因此从1开始，第一行不做处理
        for (int i = paddingMode ? 0 : 1; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() + params.topMargin;
            final int bottom = top + dividerHeight;
            if (mPaint != null) {
                c.drawRect(left + paddingLeftRight, top, right - paddingLeftRight, bottom, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //获取当前的view的position
        if (paddingMode) {
            if (parent.getChildLayoutPosition(view) == 0) {
                //第一行，paddingMode，上下都要设置
                outRect.top = dividerHeight;
            }
            outRect.bottom = dividerHeight;
        } else {
            if (parent.getChildLayoutPosition(view) == 0) {
                //第一行，则不用设置top
                outRect.top = 0;
            } else {
                outRect.top = dividerHeight;
            }
        }
    }
}