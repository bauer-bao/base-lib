package com.babase.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babase.lib.R;
import com.babase.lib.utils.GlideUtil;
import com.babase.lib.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * @author bauer on 2018/11/1.
 */
public class BaBtmMenuFragDialog extends BottomSheetDialogFragment {
    private BaBtmSheetDialog dialog;
    private RecyclerView recyclerView;
    private ConstraintLayout bbmdFragCl;
    private TextView titleTv;
    private View titleBgV;
    private ImageView iconIv;
    private View rootV;
    private View titleLineV;

    private Context mContext;
    /**
     * item内容
     */
    private ArrayList<Item> items = new ArrayList<>();
    private ItemAdapter itemAdapter;

    /**
     * 标题
     */
    private String titleStr;
    /**
     * 取消
     */
    private String cancelStr;

    /**
     * 是否可见
     */
    private int iconVisible = View.GONE;
    private ImageView.ScaleType iconScaleType = ImageView.ScaleType.FIT_CENTER;
    private int iconVerticalPadding = 2;
    private String iconUrl;

    /**
     * item的颜色
     */
    @ColorInt
    private int contentColor = Color.GRAY;
    /**
     * title的颜色
     */
    @ColorInt
    private int titleColor = Color.GRAY;
    /**
     * cancel的颜色
     */
    @ColorInt
    private int cancelColor = Color.GRAY;

    /**
     * 标题的图标
     */
    @DrawableRes
    private int titleDrawable;
    private OnBaBtmMenuFragClickListener listener;
    /**
     * 标题的对齐方式
     */
    private int titleGravity = Gravity.START | Gravity.CENTER_VERTICAL;

    /**
     * tag区分不同的dialog
     */
    private String tag;

    /**
     * 图片间距
     */
    private int drawablePadding;

    /**
     * 最大高度
     */
    private int maxHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog == null) {
            dialog = new BaBtmSheetDialog(mContext);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //如果dialogfragment有重建，需要关闭
            dismiss();
        }
        if (rootV == null) {
            rootV = inflater.inflate(R.layout.widget_ba_btm_menu_dia_frag, container);
            titleTv = rootV.findViewById(R.id.bbmd_frag_title_tv);
            titleBgV = rootV.findViewById(R.id.bbmd_frag_title_bg_v);
            iconIv = rootV.findViewById(R.id.bbmd_frag_icon_iv);
            recyclerView = rootV.findViewById(R.id.bbmd_frag_rv);
            titleLineV = rootV.findViewById(R.id.bbmd_frag_line_v);
            bbmdFragCl = rootV.findViewById(R.id.bbmd_frag_cl);
            bbmdFragCl.setOnClickListener(v -> dismiss());

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            itemAdapter = new ItemAdapter();
            recyclerView.setAdapter(itemAdapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //正常滚动，从1->2->0 基本是瞬间结束，但是此处的控件，在某些项目中（此demo不会）会延迟几秒，原因未知。导致延迟的几秒内，列表的点击事件全部被拦截掉
                    if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        recyclerView.stopScroll();
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
        updateView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果xml中设置了旋转不重建，则会进入此方法，需要关闭，否则dialog的高度不对
        dialog = null;
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        //修改成背景透明
        ((View) rootV.getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDismiss(tag);
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
        if (fragment == null || !fragment.isVisible()) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            updateView();
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
     * 更新view
     */
    private void updateView() {
        itemAdapter.notifyDataSetChanged();
        //设置title
        titleTv.setText(titleStr);
        titleTv.setGravity(titleGravity);
        titleTv.setTextColor(titleColor);
        if (titleDrawable != 0) {
            final Drawable drawable = ContextCompat.getDrawable(mContext, titleDrawable);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() * 2 / 3, drawable.getMinimumHeight() * 2 / 3);
            titleTv.setCompoundDrawablesRelative(drawable, null, null, null);
        } else {
            titleTv.setCompoundDrawablesRelative(null, null, null, null);
        }
        titleTv.setCompoundDrawablePadding(drawablePadding);
        if (TextUtils.isEmpty(titleStr)) {
            titleTv.setVisibility(View.GONE);
            titleLineV.setVisibility(View.GONE);
            titleBgV.setVisibility(View.GONE);
        } else {
            titleTv.setVisibility(View.VISIBLE);
            titleLineV.setVisibility(View.VISIBLE);
            titleBgV.setVisibility(View.VISIBLE);
        }
        //设置icon
        iconIv.setVisibility(iconVisible);
        if (iconVisible == View.VISIBLE) {
            iconIv.setScaleType(iconScaleType);
            iconIv.setPadding(0, iconVerticalPadding, 0, iconVerticalPadding);
            GlideUtil.load(mContext, iconUrl, iconIv);
        }
        if (maxHeight > 0) {
            bbmdFragCl.getLayoutParams().height = maxHeight;
        } else {
            bbmdFragCl.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    /**
     * 删除item
     *
     * @return
     */
    public BaBtmMenuFragDialog clearItem() {
        items.clear();
        return this;
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @return
     */
    public BaBtmMenuFragDialog addItem(String itemStr) {
        return addItem(itemStr, null);
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @return
     */
    public BaBtmMenuFragDialog addItem(String itemStr, String action) {
        if (itemStr == null) {
            return this;
        }
        Item item = new Item();
        item.setItemTextStr(itemStr);
        item.setItemTextColor(contentColor);
        item.setItemTextSize(16);
        if (TextUtils.isEmpty(action)) {
            item.setAction(items.size() + "");
        } else {
            item.setAction(action);
        }
        items.add(item);
        return this;
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @param itemColor
     * @return
     */
    public BaBtmMenuFragDialog addItem(String itemStr, @ColorInt int itemColor) {
        return addItem(itemStr, itemColor, null);
    }

    /**
     * 添加item
     *
     * @param itemStr
     * @param itemColor
     * @return
     */
    public BaBtmMenuFragDialog addItem(String itemStr, @ColorInt int itemColor, String action) {
        if (itemStr == null) {
            return this;
        }
        Item item = new Item();
        item.setItemTextStr(itemStr);
        item.setItemTextColor(itemColor);
        item.setItemTextSize(16);
        if (TextUtils.isEmpty(action)) {
            item.setAction(items.size() + "");
        } else {
            item.setAction(action);
        }
        items.add(item);
        return this;
    }

    /**
     * 设置title
     *
     * @param titleStr
     * @return
     */
    public BaBtmMenuFragDialog setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    /**
     * 设置cancel
     *
     * @param cancelStr
     * @return
     */
    public BaBtmMenuFragDialog setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
        return this;
    }

    /**
     * 设置取消颜色
     *
     * @param contentColor
     * @return
     */
    public BaBtmMenuFragDialog setContentColor(@ColorInt int contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    /**
     * 设置title颜色
     *
     * @param titleColor
     * @return
     */
    public BaBtmMenuFragDialog setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * 设置取消颜色
     *
     * @param cancelColor
     * @return
     */
    public BaBtmMenuFragDialog setCancelColor(@ColorInt int cancelColor) {
        this.cancelColor = cancelColor;
        return this;
    }

    /**
     * 设置title图标
     *
     * @param titleDrawable
     * @return
     */
    public BaBtmMenuFragDialog setTitleDrawable(@DrawableRes int titleDrawable) {
        this.titleDrawable = titleDrawable;
        return this;
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public BaBtmMenuFragDialog setListener(OnBaBtmMenuFragClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 设置id
     *
     * @param tag
     * @return
     */
    public BaBtmMenuFragDialog setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 设置title对齐方式
     *
     * @param titleGravity
     * @return
     */
    public BaBtmMenuFragDialog setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }

    /**
     * 设置最大高度
     *
     * @param maxHeight
     * @return
     */
    public BaBtmMenuFragDialog setMaxHeight(int maxHeight) {
        if (maxHeight > 0 && maxHeight < 300) {
            maxHeight = 300;
        }
        this.maxHeight = maxHeight;
        return this;
    }

    /**
     * 设置icon是否可见
     *
     * @param iconVisible
     * @return
     */
    public BaBtmMenuFragDialog setIconVisible(int iconVisible) {
        this.iconVisible = iconVisible;
        return this;
    }

    /**
     * 设置icon加载图片类型
     *
     * @param iconScaleType
     * @return
     */
    public BaBtmMenuFragDialog setIconScaleType(ImageView.ScaleType iconScaleType) {
        this.iconScaleType = iconScaleType;
        return this;
    }

    /**
     * 设置icon的间距
     *
     * @param iconVerticalPadding
     * @return
     */
    public BaBtmMenuFragDialog setIconVerticalPadding(int iconVerticalPadding) {
        this.iconVerticalPadding = iconVerticalPadding;
        return this;
    }

    /**
     * 设置icon的url
     *
     * @param iconUrl
     * @return
     */
    public BaBtmMenuFragDialog setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    /**
     * adapter
     */
    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.widget_rv_item_ba_btm_menu_dia, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            //title
            if (position == 0) {
                if (!TextUtils.isEmpty(titleStr)) {
                    //只是用来占位
                    holder.itemTitleTv.setVisibility(View.INVISIBLE);
                    holder.itemTitleLineV.setVisibility(View.INVISIBLE);
                    holder.itemTitleTv.setText(titleStr);
                } else {
                    holder.itemTitleTv.setVisibility(View.GONE);
                    holder.itemTitleLineV.setVisibility(View.GONE);
                }
            } else {
                holder.itemTitleTv.setVisibility(View.GONE);
                holder.itemTitleLineV.setVisibility(View.VISIBLE);
            }
            //cancel
            if (position == items.size() - 1 && !TextUtils.isEmpty(cancelStr)) {
                holder.itemCancelTv.setVisibility(View.VISIBLE);
                holder.itemCancelTv.setText(cancelStr);
                holder.itemCancelTv.setTextColor(cancelColor);
            } else {
                holder.itemCancelTv.setVisibility(View.GONE);
            }
            holder.itemCancelTv.setOnClickListener(v -> {
                dismiss();
                if (listener != null) {
                    listener.onCancelClick(tag);
                }
            });
            //content
            holder.itemContentTv.setText(items.get(position).getItemTextStr());
            holder.itemContentTv.setTextSize(items.get(position).getItemTextSize());
            holder.itemContentTv.setTextColor(items.get(position).getItemTextColor());
            holder.itemContentTv.setOnClickListener(v -> {
                dismiss();
                if (listener != null) {
                    listener.onContentClick(items.get(position).getAction(), tag);
                }
            });
            if (position == 0 && position == items.size() - 1) {
                //就1项
                if (TextUtils.isEmpty(titleStr)) {
                    //没有title
                    holder.itemContentTv.setBackgroundResource(R.drawable.bottom_menu_bg_white);
                } else {
                    holder.itemContentTv.setBackgroundResource(R.drawable.bottom_menu_bg_bottom_corner_white);
                }
            } else {
                //多项
                if (position == 0) {
                    //第一项
                    if (TextUtils.isEmpty(titleStr)) {
                        //没有title
                        holder.itemContentTv.setBackgroundResource(R.drawable.bottom_menu_bg_top_corner_white);
                    } else {
                        holder.itemContentTv.setBackgroundColor(Color.WHITE);
                    }
                } else if (position == items.size() - 1) {
                    //最后一项
                    holder.itemContentTv.setBackgroundResource(R.drawable.bottom_menu_bg_bottom_corner_white);
                } else {
                    //中间项
                    holder.itemContentTv.setBackgroundColor(Color.WHITE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView itemTitleTv;
            private View itemTitleLineV;
            private TextView itemCancelTv;
            private TextView itemContentTv;

            ItemViewHolder(View itemView) {
                super(itemView);
                itemTitleTv = itemView.findViewById(R.id.rv_item_bbmd_title_tv);
                itemCancelTv = itemView.findViewById(R.id.rv_item_bbmd_cancel_tv);
                itemContentTv = itemView.findViewById(R.id.rv_item_bbmd_content_tv);
                itemTitleLineV = itemView.findViewById(R.id.rv_item_bbmd_line_v);
            }
        }
    }

    /**
     * 点击回调
     */
    public interface OnBaBtmMenuFragClickListener {
        /**
         * item的点击
         */
        void onContentClick(String action, String tag);

        /**
         * 取消的点击
         */
        void onDismiss(String tag);

        /**
         * 取消点击
         */
        void onCancelClick(String tag);
    }

    /**
     * 实体类
     */
    public class Item {
        private String itemTextStr;
        private int itemTextColor;
        private int itemTextSize;
        private String action;

        public String getItemTextStr() {
            return itemTextStr;
        }

        public void setItemTextStr(String itemTextStr) {
            this.itemTextStr = itemTextStr;
        }

        public int getItemTextColor() {
            return itemTextColor;
        }

        public void setItemTextColor(int itemTextColor) {
            this.itemTextColor = itemTextColor;
        }

        public int getItemTextSize() {
            return itemTextSize;
        }

        public void setItemTextSize(int itemTextSize) {
            this.itemTextSize = itemTextSize;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    private class BaBtmSheetDialog extends BottomSheetDialog {
        private BottomSheetBehavior bottomSheetBehavior;

        public BaBtmSheetDialog(@NonNull Context context) {
            super(context);
        }

        public BaBtmSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //默认下对齐，以免造成和底部有一定的距离
            getWindow().setGravity(Gravity.BOTTOM);
            //解决弹出bsd之后，状态栏是黑色的问题。原理就是，设置window的布局，使得把状态栏顶出去
            int dialogHeight = ScreenUtil.getScreenHeight(getContext()) - ScreenUtil.getStatusBarHeight(getContext());
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }

        @Override
        protected void onStart() {
            super.onStart();
            //原则上需要对behavior进行判空，但是fragment进行了view的重用，所以此处的view每次展示都需要和behavior进行重新关联
            View view = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
            bottomSheetBehavior = BottomSheetBehavior.from(view);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        //解决弹出之后，如果用手势把对话框消失，则再次弹出的时候，只有阴影，对话框不会弹出的问题
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
            //默认展开，只能保证第一次是展开的状态。
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        public BottomSheetBehavior getBottomSheetBehavior() {
            return bottomSheetBehavior;
        }
    }
}
