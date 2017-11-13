package com.babase.lib.utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

/**
 * notification处理
 *
 * @author bauer on 2017/11/2.
 */

public class NotificationUtil {
    private static NotificationManager nm;

    public static boolean showNotification(Context context, int notificationId, @DrawableRes int smallIcon, String contentTitle, String contentText, String ticker) {
        if (LibUtil.isNotificationEnabled(context)) {
            //有权限
            if (nm == null) {
                nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //8.0需要特别设置chanel，不然没法显示
                NotificationChannel channel = new NotificationChannel("channelId", "default", NotificationManager.IMPORTANCE_HIGH);
                //设置呼吸灯的颜色
                channel.setLightColor(Color.GREEN);
                //是否在锁屏页面显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                nm.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(context, "channelId")
                    .setSmallIcon(smallIcon)
                    //8.0的话，不能设置，设置了没法悬浮显示，8.0以下的，设置不设置都没关系
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.splash_logo))
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    //如果需要悬浮显示，以下两条件，缺一不可。同时，必须保证设置中打开了悬浮显示的开关
                    .setFullScreenIntent(null, true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(ticker)
                    .build();
            nm.cancel(notificationId);
            nm.notify(notificationId, notification);
            return true;
        } else {
            //没有权限
            if (context instanceof Application) {
                //目前不支持application的toast，因此此处没有处理
                return true;
            }
            return false;
        }
    }
}
