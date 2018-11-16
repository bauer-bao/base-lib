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
    private boolean withHeaderAndFooter;
    private Paint mPaint;

    public BaRvItemDecorationDivider(int paddingLeftRight, int dividerHeight, int dividerColor, boolean withHeaderAndFooter) {
        this.paddingLeftRight = paddingLeftRight;
        this.dividerHeight = dividerHeight;
        this.withHeaderAndFooter = withHeaderAndFooter;
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
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            if (mPaint != null) {
                if (withHeaderAndFooter) {
                    //包括上下边距
                    //画下边距的divider
                    c.drawRect(left + paddingLeftRight, bottom, right - paddingLeftRight, bottom + dividerHeight, mPaint);
                    if (i == 0) {
                        //画上边距的divider
                        c.drawRect(left + paddingLeftRight, top - dividerHeight, right - paddingLeftRight, top, mPaint);
                    }
                } else {
                    //不包括上下边距
                    //画上边距的divider
                    c.drawRect(left + paddingLeftRight, top - dividerHeight, right - paddingLeftRight, top, mPaint);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //获取当前的view的position
        if (withHeaderAndFooter) {
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