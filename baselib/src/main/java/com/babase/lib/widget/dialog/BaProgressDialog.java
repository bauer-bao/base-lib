package com.babase.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.ScreenUtil;

/**
 * 自定义的ProgressDialog
 * <p>
 * 使用方法和PWDialog类似，具体请点击右侧link
 * <p>
 * 具体使用，已写入baseActivity，并且重载多重常用的显示方法
 * <p>
 * 注意点，show和dismiss必须要在main线程中执行
 * <p>
 * 加载动画，建议使用gif，然后使用glide加载。但是项目中的图片制作出来的gif边缘模糊，因此依旧使用帧动画实现
 *
 * @author bauer_bao
 */
public class BaProgressDialog extends Dialog {
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

    public BaProgressDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public BaProgressDialog(Context context) {
        super(context, R.style.BaDialog);
        mContext = context;
    }

    /**
     * 设置message
     *
     * @param messageStr
     * @return
     */
    public BaProgressDialog setBaProgressDialogMessage(String messageStr) {
        this.messageStr = messageStr;
        return this;
    }

    /**
     * 设置message
     *
     * @param messageId
     * @return
     */
    public BaProgressDialog setBaProgressDialogMessage(int messageId) {
        this.messageStr = mContext.getString(messageId);
        return this;
    }

    /**
     * 设置点击对话框外部是否取消，默认不可取消
     *
     * @param cancelable
     * @return
     */
    public BaProgressDialog setBaProgressDialogCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 创建dialog
     */
    public BaProgressDialog baProgressDialogCreate() {
        View view = View.inflate(mContext, R.layout.widget_ba_progressdialog, null);
        messageTV = view.findViewById(R.id.pd_loading_tv);

        setContentView(view);
        //设置布局
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().getAttributes().width = ScreenUtil.getScreenWidth(mContext);
        getWindow().getAttributes().height = ScreenUtil.getScreenHeight(mContext);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉浸式到标题栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return this;
    }

    /**
     * 显示dialog
     */
    public void baProgressDialogShow() {
        checkContent();
        if (!isShowing()) {
            try {
                show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * dismiss dialog
     */
    public void baProgressDialogDismiss() {
        if (isShowing()) {
            dismiss();
        }
    }

    /**
     * 是否正在显示中
     *
     * @return
     */
    public boolean isBaProgressDialogShowing() {
        return isShowing();
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
//        setCancelable(cancelable);//不可取消
        //外部不可取消，但是返回键可以取消
        setCanceledOnTouchOutside(cancelable);
    }
}