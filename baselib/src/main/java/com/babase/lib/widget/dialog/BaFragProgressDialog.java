package com.babase.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.babase.lib.R;

/**
 * 自定义的ProgressDialog
 * <p>
 * 具体使用，已写入baseActivity，并且重载多重常用的显示方法
 * <p>
 * show的时候tag需要多加注意
 *
 * @author bauer_bao
 */

public class BaFragProgressDialog extends AppCompatDialogFragment {
    private Dialog dialog;

    private Context mContext;
    /**
     * dialog对应的默认view
     */
    private TextView messageTV;

    /**
     * 显示的文字
     */
    private String messageStr;

    /**
     * 点击dialog外部不可取消，但是点击返回按钮可以取消，默认可以取消
     */
    private boolean cancelable = false;

    private View rootV;

    /**
     * 进度条宽高
     */
    private int width = -1;
    private int height = -1;

    /**
     * 背景资源id
     */
    private int bgResId = -1;

    private OnBaFragProgressDialogListener listener;

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
            rootV = inflater.inflate(R.layout.widget_ba_progressdialog, container);
            messageTV = rootV.findViewById(R.id.pd_loading_tv);
            //设置布局
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            if (width != -1) {
                dialog.getWindow().getAttributes().width = width;
            }
            if (height != -1) {
                dialog.getWindow().getAttributes().height = height;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //沉浸式到标题栏
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            ((ViewGroup) rootV.getParent()).removeView(rootV);
        }
        return rootV;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkContent();
    }

    /**
     * 检查各个参数
     */
    private void checkContent() {
        if (!TextUtils.isEmpty(messageStr)) {
            messageTV.setText(messageStr);
            messageTV.setVisibility(View.VISIBLE);
        } else {
            messageTV.setVisibility(View.GONE);
        }
        if (bgResId != -1) {
            rootV.setBackgroundResource(bgResId);
        }
        //外部不可取消，但是返回键可以取消
        dialog.setCanceledOnTouchOutside(cancelable);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (listener != null) {
            listener.onCancel();
        }
    }

    /**
     * 显示dialog，重写，不然会报Can not perform this action after onSaveInstanceState 错误
     *
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 和show对应
     */
    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    /**
     * 设置message
     *
     * @param messageStr
     * @return
     */
    public BaFragProgressDialog setBaProgressDialogMessage(String messageStr) {
        this.messageStr = messageStr;
        return this;
    }

    /**
     * 设置点击对话框外部是否取消，默认不可取消
     *
     * @param cancelable
     * @return
     */
    public BaFragProgressDialog setBaProgressDialogCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 设置宽高
     *
     * @param width
     * @param height
     */
    public BaFragProgressDialog setBaProgressDialogSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置背景resid
     *
     * @param bgId
     */
    public BaFragProgressDialog setBaProgressDialogBgResId(@DrawableRes int bgId) {
        this.bgResId = bgId;
        return this;
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public BaFragProgressDialog setListener(OnBaFragProgressDialogListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 点击回调
     */
    public interface OnBaFragProgressDialogListener {
        /**
         * 取消的点击
         */
        void onDismiss();

        /**
         * 取消点击
         */
        void onCancel();
    }
}