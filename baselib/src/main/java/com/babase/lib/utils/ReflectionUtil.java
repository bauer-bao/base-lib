package com.babase.lib.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;

/**
 * @author bauer_bao on 15/12/10.
 */
public class ReflectionUtil {
    /**
     * 获取layout Id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @LayoutRes
    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout", paramContext.getPackageName());
    }

    /**
     * 获取String Id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getStringId(Context paramContext, String paramString) {
        if (paramContext == null) {
            return 401;
        }
        return paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
    }

    /**
     * 获取图片资源ID
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @DrawableRes
    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "drawable", paramContext.getPackageName());
    }

    /**
     * 获取style id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @StyleRes
    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "style", paramContext.getPackageName());
    }

    /**
     * 获取ID
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @IdRes
    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "id", paramContext.getPackageName());
    }

    /**
     * 获取颜色ID
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @ColorRes
    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "color", paramContext.getPackageName());
    }

    /**
     * 获取array ID
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    @ArrayRes
    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "array", paramContext.getPackageName());
    }
}
