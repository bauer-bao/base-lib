package com.babase.example;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.babase.lib.utils.AppManager;
import com.babase.lib.utils.GlideCircleTransform;
import com.babase.lib.utils.GlideUtil;
import com.babase.lib.utils.Logger;
import com.babase.lib.utils.NotificationUtil;
import com.babase.lib.utils.ScreenUtil;
import com.babase.lib.widget.dialog.BaBtmMenuFragDialog;
import com.babase.lib.widget.BaRvItemDecorationDivider;
import com.babase.lib.widget.BaToast;
import com.babase.lib.widget.dialog.BaFragDialog;
import com.babase.lib.widget.dialog.BaFragProgressDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaToast baToast;
    private BaBtmMenuFragDialog frag;
    private BaFragProgressDialog progressDialog;
    private BaFragDialog baFragDialog;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getInstance().addActivity(this);
        Logger.setDebug(true);

        ImageView imageView = findViewById(R.id.text_iv);
        recyclerView = findViewById(R.id.test_rv);

        GlideUtil.setPlaceholderDrawableId(R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        GlideCircleTransform glideCircleTransform = new GlideCircleTransform(this);
        glideCircleTransform.setText("HH");
        GlideUtil.loadCircle(this, "http://img05.tooopen.com/images/20140328/sy_57865838889.jpg", true, glideCircleTransform, imageView);
        baToast = new BaToast(this);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("toast");
        stringArrayList.add("dialog");
        stringArrayList.add("progress dialog");
        stringArrayList.add("bottom sheet dialog");
        stringArrayList.add("notification");

        Adapter adapter = new Adapter(stringArrayList);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            i++;
            if (position == 0) {
                //toast
                baToast.setTextAndShow("test");

            } else if (position == 1) {
                //dialog
                if (baFragDialog == null) {
                    baFragDialog = new BaFragDialog();
                }
                baFragDialog.setBaDialogId(11)
                        .setOnBaDialogClickListener((which, dialogId) -> Logger.d("click--->" + which))
                        .setBaDialogTitle(null)
                        .setBaDialogTitleColor(R.color.colorPrimary)
                        .setBaDialogMessage("messagemessage" + i)
                        .setBaDialogMessageColor(R.color.colorPrimary)
                        .setBaDialogContentCenter(true)
                        .setBaDialogPositiveButton("yesyes")
                        .setBaDialogNegativeButton(null)
                        .setBaDialogPositiveButtonColor(R.color.colorPrimary)
                        .setBaDialogNegativeButtonColor(R.color.colorPrimary)
                        .setBaDialogCancelable(true)
                        .show(getSupportFragmentManager(), "dialog");

            } else if (position == 2) {
                //progress dialog
                if (progressDialog == null) {
                    progressDialog = new BaFragProgressDialog();
                }
                progressDialog.setBaProgressDialogBgResId(R.drawable.solid_toast_bg)
                        .setBaProgressDialogMessage("test..." + i)
                        .setBaProgressDialogCancelable(true)
                        .setListener(new BaFragProgressDialog.OnBaFragProgressDialogListener() {
                            @Override
                            public void onCancel() {
                                Logger.d("cancel--->1111");
                            }

                            @Override
                            public void onDismiss() {
                                Logger.d("dismiss--->11111");
                            }
                        })
                        .show(getSupportFragmentManager(), "progressdialog");

            } else if (position == 3) {
                //bottom sheet dialog
                if (frag == null) {
                    frag = new BaBtmMenuFragDialog();
                    frag.setListener(new BaBtmMenuFragDialog.OnBaBtmMenuFragClickListener() {
                        @Override
                        public void onContentClick(String action, String tag) {
                            Logger.d(" content click--->" + action);
                            frag.show(getSupportFragmentManager(), "bottomsheetdialog");
                        }

                        @Override
                        public void onDismiss(String tag) {
                            Logger.d(" dismiss--->");

                        }

                        @Override
                        public void onCancelClick(String tag) {
                            Logger.d("cancel click--->");
                        }
                    });
                }
                if (i % 2 == 0) {
                    frag.clearItem()
                            .addItem("1")
                            .setMaxHeight(ScreenUtil.getScreenHeight(this) / 2)
                            .setContentColor(Color.BLUE)
                            .setTitleStr("title")
                            .addItem("2", Color.GREEN)
                            .addItem("3")
                            .addItem("4")
                            .addItem("5")
                            .addItem("6")
                            .addItem("7")
                            .addItem("8")
                            .setCancelStr(null)
                            .addItem("9")
                            .addItem("10")
                            .addItem("11")
                            .addItem("12")
                            .addItem("13")
                            .setIconVisible(View.GONE)
                            .show(getSupportFragmentManager(), "bottomsheetdialog");
                } else {
                    frag.clearItem()
                            .setTitleStr("title3323")
                            .setCancelStr("cancel")
                            .addItem("11", Color.RED)
                            .addItem("22")
                            .setIconVisible(View.VISIBLE)
                            .setIconVerticalPadding(6)
                            .setIconUrl("http://img05.tooopen.com/images/20140328/sy_57865838889.jpg")
                            .show(getSupportFragmentManager(), "bottomsheetdialog");
                }

            } else if (position == 4) {
                //notification
                NotificationUtil.showNotification(MainActivity.this, 0, R.drawable.ic_launcher_background, "test", "test", "test", null);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new BaRvItemDecorationDivider(0, 50, Color.YELLOW, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        baToast.cancelShow();
        super.onPause();
    }

    private class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public Adapter(@Nullable List<String> data) {
            super(R.layout.rv_item_test, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.item_test_tv, item);
        }
    }
}
