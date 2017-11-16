package com.babase.lib.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.babase.lib.utils.ScreenUtil;

public class EditTextWithClear extends AppCompatEditText implements OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private OnFocusChangedCallback focusCallback;

    public EditTextWithClear(Context context) {
        this(context, null);
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithClear(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,2是获得右边的图片  顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawablesRelative()[2];
//        if (mClearDrawable == null) {
//            // throw new
//            // NullPointerException("You can add drawableRight attribute in XML");
//            mClearDrawable = ContextCompat.getDrawable(context, R.drawable.edt_clear);
//        }

        if (mClearDrawable != null) {
            //37是3.0版本之前的图片的宽高
            int clearBmpWidth = ScreenUtil.dip2px(context, 15);
            mClearDrawable.setBounds(0, 0, clearBmpWidth, clearBmpWidth);
        }
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 因为外部设置focus监听，那么这里的监听将被覆盖，导致删除按钮不会出现。所以需要回调返回
     *
     * @param focusCallback
     */
    public void setFocusCallback(OnFocusChangedCallback focusCallback) {
        this.focusCallback = focusCallback;
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawablesRelative()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (focusCallback != null) {
            focusCallback.focusChanged(hasFocus);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        if (right != null) {
            setCompoundDrawables(getCompoundDrawablesRelative()[0], getCompoundDrawablesRelative()[1], right, getCompoundDrawablesRelative()[3]);
        }
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnFocusChangedCallback {
        /**
         * 焦点改变
         *
         * @param hasFocus
         */
        void focusChanged(boolean hasFocus);
    }
}
