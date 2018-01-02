package com.babase.lib;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.babase.lib.utils.AppManager;
import com.babase.lib.utils.GlideCircleTransform;
import com.babase.lib.utils.GlideUtil;
import com.babase.lib.utils.Logger;
import com.babase.lib.widget.BaBottomMenuDialog;
import com.babase.lib.widget.BaToast;
import com.babase.lib.widget.dialog.BaProgressDialog;

public class MainActivity extends AppCompatActivity {
    private BaBottomMenuDialog baBottomMenuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getInstance().addActivity(this);
        Logger.setDebug(true);

        ImageView imageView = findViewById(R.id.text_iv);

        GlideUtil.setPlaceholderDrawableId(R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        GlideCircleTransform glideCircleTransform = new GlideCircleTransform(this);
        glideCircleTransform.setText("HH");
        GlideUtil.loadCircle(this, "http://img05.tooopen.com/images/20140328/sy_57865838889.jpg", true, glideCircleTransform, imageView);
        BaToast baToast = new BaToast(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAcowoProgressDialog();
//                baToast.setTextAndShow("test");
//                AppManager.getInstance().killActivity("com.babase.lib.MainActivity");
                if (baBottomMenuDialog == null) {
                    baBottomMenuDialog = new BaBottomMenuDialog(MainActivity.this)
                            .setListener(new BaBottomMenuDialog.OnBaBottomMenuClickListener() {
                                @Override
                                public void onContentClick(int position) {
                                    Logger.d("position--->" + position);
                                }

                                @Override
                                public void onDismiss() {

                                }
                            })
                            .onCreateDialog();
                }
                i++;
                baBottomMenuDialog.clearItem()
                        .addItem("111", Color.GREEN)
                        .addItem("222")
                        .addItem("333")
                        .addItem("444")
                        .addItem("555")
                        .addItem("" + i)
                        .updateInfo();
                baBottomMenuDialog.show();
            }
        });
    }

    private int i = 0;

    /**
     * show pwProgressDialog
     */
    protected void showAcowoProgressDialog() {
        BaProgressDialog acowoProgressDialog = new BaProgressDialog(this)
                .setBaProgressDialogBgResId(R.drawable.solid_toast_bg)
//                    .setBaProgressDialogSize(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this))
                .baProgressDialogCreate();
        if (null != acowoProgressDialog) {
            //更新message
            acowoProgressDialog.setBaProgressDialogMessage("loading llhlj");
            acowoProgressDialog.baProgressDialogShow();
        }
    }
}
