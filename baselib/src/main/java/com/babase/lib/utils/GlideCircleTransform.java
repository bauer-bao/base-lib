package com.babase.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.text.TextUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 圆形文字变化工具
 *
 * @author bauer on 2017/11/14.
 */
public class GlideCircleTransform extends BitmapTransformation {
    private @ColorInt int textColor = Color.BLACK;
    private int textSize = 36;
    private Typeface typeface;
    private String text;

    public GlideCircleTransform(Context context) {
        super();
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        if (!TextUtils.isEmpty(text)) {
            //有文字
            Paint mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            //设置颜色
            mTextPaint.setColor(textColor);
            //设置字体大小
            mTextPaint.setTextSize(textSize);
            if (typeface != null) {
                mTextPaint.setTypeface(typeface);
            }
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(text, r - mTextPaint.measureText(text) / 2, r - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}