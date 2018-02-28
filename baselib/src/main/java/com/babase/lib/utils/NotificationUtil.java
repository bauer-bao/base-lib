package com.babase.lib.utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    /**
     * 显示notification
     *
     * @param context
     * @param notificationId
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @param ticker
     * @return
     */
    public static boolean showNotification(Context context, int notificationId, @DrawableRes int smallIcon, String contentTitle, String contentText, String ticker, PendingIntent pendingIntent) {
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

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId")
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
                    .setTicker(ticker);
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent);
            }
            //移除之前的通知
            nm.cancel(notificationId);
            //显示最新的通知
            nm.notify(notificationId, builder.build());
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

    /**
     * 获取pendingintent activity
     *
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent getPendingActivityIntent(Context context, Intent intent) {
        //0为requestCode， FLAG_UPDATE_CURRENT表示会更新同一个notificationId的其他pendingIntent
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 获取pendingintent broadcast
     *
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent getPendingBroadcastIntent(Context context, Intent intent) {
        //0为requestCode， FLAG_UPDATE_CURRENT表示会更新同一个notificationId的其他pendingIntent
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 获取pendingintent service
     *
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent getPendingServiceIntent(Context context, Intent intent) {
        //0为requestCode， FLAG_UPDATE_CURRENT表示会更新同一个notificationId的其他pendingIntent
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
