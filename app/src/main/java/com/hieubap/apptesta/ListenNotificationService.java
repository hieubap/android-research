package com.hieubap.apptesta;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class ListenNotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("TRIGGER_NOTIFICAION");
        // Your code to run when a notification is received
        String notificationTitle = sbn.getNotification().extras.getString("android.title");
        String notificationText = sbn.getNotification().extras.getString("android.text");

        // Check if the notification matches your criteria
        if (notificationTitle.contains("specific text")) {
            // Run your code
            // For example, start an Activity, send a broadcast, or start a background service
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Code for when the notification is removed, if needed
    }
}
