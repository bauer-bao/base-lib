package com.babase.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.ScreenUtil;

/**
 * @author bauer on 2018/11/15.
 */
public class BaFragDialog extends AppCompatDialogFragment {
    private Dialog dialog;
    private Context mContext;
    /**
     * dialog对应的默认view
     */
    private View contentView, rootV;
    private TextView messageTV, titleTV;
    private RelativeLayout contentRL;
    private FrameLayout contentViewParent;
    private BaDialogButton btnGroup;
    private View buttonLine;

    private String titleStr;
    private String messageStr;
    private String positiveButtonStr;
    private String negativeButtonStr;

    /**
     * 文案是否居中，默认居中，false左对齐，暂时只支持两种排列模式
     */
    private boolean textCenter = true;

    /**
     * 点击dialog外部是否可以取消，默认不可以取消
     */
    private boolean cancelable = false;

    /**
     * 自定义view是否已经初始化
     */
    private boolean customerInited = false;

    /**
     * dialog的ID
     */
    private int dialogId;

    /**
     * title字体颜色
     */
    @ColorRes
    private int titleColorId;

    /**
     * message字体颜色
     */
    @ColorRes
    private int messageColorId;

    /**
     * 确定按钮字体颜色
     */
    @ColorRes
    private int positiveButtonColorId;

    /**
     * 取消按钮字体颜色
     */
    @ColorRes
    private int negativeButtonColorId;

    /**
     * 背景颜色
     */
    @ColorRes
    private int backgroundColor = 0;

    private OnDialogButtonListener dialogButtonListener;
    private OnCustomerViewCallBack customerViewCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.BaDialog);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootV == null) {
            rootV = inflater.inflate(R.layout.widget_ba_dialog, container);
            titleTV = rootV.findViewById(R.id.dialog_title_tv);
            contentRL = rootV.findViewById(R.id.dialog_content_rl);
            messageTV = rootV.findViewById(R.id.dialog_message_tv);
            contentViewParent = rootV.findViewById(R.id.dialog_content_fl);
            btnGroup = rootV.findViewById(R.id.dialog_pdbtn);
            buttonLine = rootV.findViewById(R.id.dialog_middle_line_view);
            btnGroup.setOnClickListener(new BaDialogButton.OnDialogButtonClickCallBack() {
                @Override
                public void onPositiveButtonClicked() {
                    if (dialogButtonListener != null) {
                        dialogButtonListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }

                @Override
                public void onNegativeButtonClicked() {
                    if (dialogButtonListener != null) {
                        dialogButtonListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
        } else {
            ((ViewGroup) rootV.getParent()).removeView(rootV);
        }
        return rootV;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(cancelable);
        autoFitScreen();
        checkContent();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        autoFitScreen();
    }

    /**
     * 显示dialog，重写，不然会报Can not perform this action after onSaveInstanceState 错误
     *
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 和show对应
     */
    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    /**
     * 设置dialogId
     *
     * @param dialogId
     * @return
     */
    public BaFragDialog setBaDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }

    /**
     * 设置按钮监听
     *
     * @param onBaDialogClickListener
     * @return
     */
    public BaFragDialog setOnBaDialogClickListener(OnBaDialogClickListener onBaDialogClickListener) {
        dialogButtonListener = new OnDialogButtonListener(onBaDialogClickListener);
        return this;
    }

    /**
     * 设置title，null则不显示title
     *
     * @param titleStr
     * @return
     */
    public BaFragDialog setBaDialogTitle(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    /**
     * 设置title字体颜色
     *
     * @param titleColorId
     * @return
     */
    public BaFragDialog setBaDialogTitleColor(@ColorRes int titleColorId) {
        this.titleColorId = titleColorId;
        return this;
    }

    /**
     * 设置message
     *
     * @param messageStr
     * @return
     */
    public BaFragDialog setBaDialogMessage(String messageStr) {
        this.messageStr = messageStr;
        return this;
    }

    /**
     * 设置message字体颜色
     *
     * @param messageColorId
     * @return
     */
    public BaFragDialog setBaDialogMessageColor(@ColorRes int messageColorId) {
        this.messageColorId = messageColorId;
        return this;
    }

    /**
     * 设置文字居中属性， 默认居中
     *
     * @param gravity
     * @return
     */
    public BaFragDialog setBaDialogContentCenter(boolean gravity) {
        textCenter = gravity;
        return this;
    }

    /**
     * 设置确认按钮，null则不显示确认按钮
     *
     * @param positiveButtonStr
     * @return
     */
    public BaFragDialog setBaDialogPositiveButton(String positiveButtonStr) {
        this.positiveButtonStr = positiveButtonStr;
        return this;
    }

    /**
     * 设置确定按钮颜色
     *
     * @param positiveButtonColorId
     * @return
     */
    public BaFragDialog setBaDialogPositiveButtonColor(@ColorRes int positiveButtonColorId) {
        this.positiveButtonColorId = positiveButtonColorId;
        return this;
    }

    /**
     * 设置取消按钮
     *
     * @param negativeButtonStr
     * @return
     */
    public BaFragDialog setBaDialogNegativeButton(String negativeButtonStr) {
        this.negativeButtonStr = negativeButtonStr;
        return this;
    }

    /**
     * 设置取消按钮颜色
     *
     * @param negativeButtonColorId
     * @return
     */
    public BaFragDialog setBaDialogNegativeButtonColor(@ColorRes int negativeButtonColorId) {
        this.negativeButtonColorId = negativeButtonColorId;
        return this;
    }

    /**
     * 设置点击对话框外部是否取消，默认不可取消
     *
     * @param cancelable
     * @return
     */
    public BaFragDialog setBaDialogCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public BaFragDialog setBaDialogBackgroundColor(@ColorRes int colorId) {
        this.backgroundColor = colorId;
        return this;
    }

    /**
     * 设置自定义布局
     *
     * @param contentView          自定义布局View
     * @param customerViewCallBack 自定义布局接口回调
     * @return
     */
    public BaFragDialog setBaDialogContentView(View contentView, OnCustomerViewCallBack customerViewCallBack) {
        this.contentView = contentView;
        this.customerViewCallBack = customerViewCallBack;
        return this;
    }

    /**
     * 设置新的布局
     *
     * @param layoutResId          自定义布局View
     * @param customerViewCallBack 自定义布局接口回调
     * @return
     */
    public BaFragDialog setBaDialogContentView(int layoutResId, OnCustomerViewCallBack customerViewCallBack) {
        this.contentView = View.inflate(mContext, layoutResId, null);
        this.customerViewCallBack = customerViewCallBack;
        return this;
    }

    /**
     * 自动适应屏幕，只有切换屏幕方向的时候用到
     */
    public void autoFitScreen() {
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.72);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(params);
        }
    }

    /**
     * 检查各个组件
     */
    private void checkContent() {
        //检查title
        if (TextUtils.isEmpty(titleStr)) {
            titleTV.setVisibility(View.GONE);
            contentRL.setBackgroundResource(R.drawable.dialog_bg_white_content_top_corner);
        } else {
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(titleStr);
            if (titleColorId != 0) {
                titleTV.setTextColor(ContextCompat.getColor(mContext, titleColorId));
            }
            contentRL.setBackgroundColor(Color.WHITE);
        }

        if (TextUtils.isEmpty(positiveButtonStr) && TextUtils.isEmpty(negativeButtonStr)) {
            contentRL.setBackgroundResource(R.drawable.dialog_bg_white_content_bottom_corner);
            buttonLine.setVisibility(View.GONE);
        }

        if (backgroundColor != 0) {
            contentRL.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));
        }

        //检查内容
        if (contentView != null) {
            //检查自定义布局
            //因为自定义布局，所以整体的布局背景，在此处设置
            if (TextUtils.isEmpty(positiveButtonStr) && TextUtils.isEmpty(negativeButtonStr)) {
                //无buttons
                contentRL.setBackgroundResource(R.drawable.dialog_bg_white_corners);
            } else {
                contentRL.setBackgroundResource(R.drawable.dialog_bg_white_content_top_corner);
            }

            if (!customerInited) {
                //没有初始化，则需要初始化
                customerInited = true;
                contentViewParent.removeAllViews();
                contentViewParent.addView(this.contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                customerViewCallBack.initCustomerView(contentRL, contentView, dialogId);
            }

        } else {//检查message
            if (!TextUtils.isEmpty(messageStr)) {
                messageTV.setText(messageStr);
                messageTV.setGravity(textCenter ? Gravity.CENTER : Gravity.START | Gravity.CENTER_VERTICAL);
            }
            if (messageColorId != 0) {
                messageTV.setTextColor(ContextCompat.getColor(mContext, messageColorId));
            }
        }

        btnGroup.setButtonText(positiveButtonStr, negativeButtonStr);
        btnGroup.setButtonTextColor(positiveButtonColorId, negativeButtonColorId);
    }

    /**
     * 按钮监听
     */
    private class OnDialogButtonListener implements DialogInterface.OnClickListener {
        final OnBaDialogClickListener onBaDialogClickListener;

        public OnDialogButtonListener(OnBaDialogClickListener onBaDialogClickListener) {
            this.onBaDialogClickListener = onBaDialogClickListener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
            onBaDialogClickListener.onBaDialogButtonClicked(which, dialogId);
        }
    }

    public interface OnBaDialogClickListener {
        /**
         * 按钮点击
         *
         * @param which
         * @param dialogId
         */
        void onBaDialogButtonClicked(int which, int dialogId);
    }

    public interface OnCustomerViewCallBack {
        /**
         * 自定义view的初始化
         *
         * @param customViewFl
         * @param view
         * @param dialogId
         */
        void initCustomerView(RelativeLayout customViewFl, View view, int dialogId);
    }
}
