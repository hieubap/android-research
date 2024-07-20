package com.hieubap.apptesta;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;


public class NotificationUtils {

    public static final int NOTIFICATION_ID = 1337;
    private static final String NOTIFICATION_CHANNEL_ID = "com.mtsahakis.mediaprojectiondemo.app";
    private static final String NOTIFICATION_CHANNEL_NAME = "com.mtsahakis.mediaprojectiondemo.app";

    public static Pair<Integer, Notification> getNotification(@NonNull Context context) {
        createNotificationChannel(context);
        Notification notification = createNotification(context);
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        return new Pair<>(NOTIFICATION_ID, notification);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    private static Notification createNotification(@NonNull Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        RemoteViews contentView = new RemoteViews("com.hieubap.apptesta", R.layout.service_notification_2);
        builder.setContent(contentView);
        builder.setSmallIcon(R.drawable.ic_camera);
        builder.setContentTitle("  ");
        builder.setContentText("  ");
        builder.setOngoing(true);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setPriority(-100);
        builder.setShowWhen(false);
        return builder.build();
//        Notification notification  =
//                new Notification();
////                (Build.VERSION.SDK_INT >= 26)
////                ? (new Notification.Builder(/* context= */ instance, /* channelId= */ getPackageName())).build()
////                :  new Notification()
////                ;
//
//        notification.when          = System.currentTimeMillis();
//        notification.flags         = 0;
//        notification.flags        |= Notification.FLAG_ONGOING_EVENT;
//        notification.flags        |= Notification.FLAG_NO_CLEAR;
//        notification.icon          = R.drawable.ic_launcher_background;
//        notification.tickerText    = "something";
////        notification.contentIntent = getPendingIntent_StopService();
////        notification.deleteIntent  = getPendingIntent_StopService();
//
//        if (Build.VERSION.SDK_INT >= 16) {
//            notification.priority    = Notification.PRIORITY_HIGH;
//        }
//        else {
//            notification.flags      |= Notification.FLAG_HIGH_PRIORITY;
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            notification.visibility  = Notification.VISIBILITY_PUBLIC;
//        }
//
//        RemoteViews contentView    = new RemoteViews("com.hieubap.apptesta", R.layout.service_notification_2);
////        contentView.setImageViewResource(R.id.notification_icon, R.drawable.ic_launcher_background);
////        contentView.setTextViewText(R.id.notification_text_line1, "BIDV checking");
////        contentView.setTextViewText(R.id.notification_text_line2, "My service checking transfer BIDV banking");
////        contentView.setTextViewText(R.id.notification_text_line3, "string3");
//        notification.contentView   = contentView;
//
//        return notification;
    }

}
