package com.babase.lib.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;

import com.babase.lib.utils.ScreenUtil;

/**
 * @author bauer on 2017/8/18.
 */

public class BaBottomSheetDialog extends BottomSheetDialog {

    public BaBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public BaBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int dialogHeight = ScreenUtil.getScreenHeight(getContext()) - ScreenUtil.getStatusBarHeight(getContext());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }
}
