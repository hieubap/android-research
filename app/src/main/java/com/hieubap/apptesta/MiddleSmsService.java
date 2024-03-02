package com.hieubap.apptesta;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MiddleSmsService extends Service {
    private static final String ACTION_STOP = "STOP";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    SmsBroadCaster smsReceiver;

    private PendingIntent getPendingIntent_StopService() {
        Intent intent = new Intent(instance, MiddleSmsService.class);
        intent.setAction(ACTION_STOP);

        return PendingIntent.getService(instance, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification getNotification() {
        Notification notification  = (Build.VERSION.SDK_INT >= 26)
                ? (new Notification.Builder(/* context= */ instance, /* channelId= */ getPackageName())).build()
                :  new Notification()
                ;

        notification.when          = System.currentTimeMillis();
        notification.flags         = 0;
        notification.flags        |= Notification.FLAG_ONGOING_EVENT;
        notification.flags        |= Notification.FLAG_NO_CLEAR;
        notification.icon          = R.drawable.ic_launcher_background;
        notification.tickerText    = "something";
        notification.contentIntent = getPendingIntent_StopService();
        notification.deleteIntent  = getPendingIntent_StopService();

        if (Build.VERSION.SDK_INT >= 16) {
            notification.priority    = Notification.PRIORITY_HIGH;
        }
        else {
            notification.flags      |= Notification.FLAG_HIGH_PRIORITY;
        }

        if (Build.VERSION.SDK_INT >= 21) {
            notification.visibility  = Notification.VISIBILITY_PUBLIC;
        }

        RemoteViews contentView    = new RemoteViews(getPackageName(), R.layout.service_notification);
        contentView.setImageViewResource(R.id.notification_icon, R.drawable.ic_launcher_background);
        contentView.setTextViewText(R.id.notification_text_line1, "BIDV checking");
        contentView.setTextViewText(R.id.notification_text_line2, "My service checking transfer BIDV banking");
//        contentView.setTextViewText(R.id.notification_text_line3, "string3");
        notification.contentView   = contentView;

        return notification;
    }
    public static int getInteger(Context context, int id) {
        return context.getResources().getInteger(id);
    }

    private int getNotificationId() {
        return 1;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            System.out.println("Build.VERSION.SDK_INT >= 26");
            String channelId       = getPackageName();
            NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel NC = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);

            NC.setDescription(channelId);
            NC.setSound(null, null);
            NM.createNotificationChannel(NC);
        }
    }

    private void showNotification() {
        Notification notification = getNotification();
        int NOTIFICATION_ID = getNotificationId();

        if (Build.VERSION.SDK_INT >= 5) {
            System.out.println("Build.VERSION.SDK_INT >= 5");
            createNotificationChannel();
            startForeground(NOTIFICATION_ID, notification);
        }
        else {
            NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NM.notify(NOTIFICATION_ID, notification);
        }
    }

    private void processIntent(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();

        if ((action != null) && action.equals(ACTION_STOP))
            instance.stopSelf();
    }

    public static MiddleSmsService instance;

    @Override
    public void onCreate() {
        System.out.println("Create Service");
        smsReceiver = new SmsBroadCaster();
//        registerReceiver(smsReceiver, new IntentFilter(Intent.ACTION_SHOW_APP_INFO));
        registerReceiver(smsReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.ACTION_PASTE));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.ACTION_PROVIDER_CHANGED));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.EXTRA_EMAIL));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.CATEGORY_APP_EMAIL));
//        registerReceiver(smsReceiver, new IntentFilter(Intent.CATEGORY_APP_MESSAGING));
        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
//        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION));
//        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_DELIVER_ACTION));
//        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_CB_RECEIVED_ACTION));
//        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT));


        super.onCreate();
        instance = MiddleSmsService.this;
        WakeLockMgr.acquire(/* context= */ instance);
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        Toast.makeText(
                getApplicationContext(), "Service has started running in the background",
                Toast.LENGTH_SHORT
        ).show();

        processIntent(intent);
//        System.out.println("Started Service");
//        if (name != null) {
//            Log.d("Service Name",name);
//        }
//        Log.d("Service Status","Starting Service");
//        for (int i = 1;i<11;i++){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        stopSelf();
        return START_STICKY;
    }

    private void hideNotification() {
        if (Build.VERSION.SDK_INT >= 5) {
            stopForeground(true);
        }
        else {
            NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            int NOTIFICATION_ID    = getNotificationId();
            NM.cancel(NOTIFICATION_ID);
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(
                getApplicationContext(), "Service execution completed",
                Toast.LENGTH_SHORT
        ).show();

        super.onDestroy();
        instance = null;
        WakeLockMgr.release();

        hideNotification();
        Toast.makeText(getApplicationContext(), "Dừng service", Toast.LENGTH_SHORT).show();

        unregisterReceiver(smsReceiver);
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public boolean stopService(Intent name) {
//        return super.stopService(name);
//    }


//    private class SmsReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            System.out.println("received sms");
//            String telnr = "", nachricht = "";
//
//            Bundle extras = intent.getExtras();
//
//            if (extras != null) {
//                Object[] pdus = (Object[]) extras.get("pdus");
//                if (pdus != null) {
//
//                    for (Object pdu : pdus) {
//                        SmsMessage smsMessage = getIncomingMessage(pdu, extras);
//                        telnr = smsMessage.getDisplayOriginatingAddress();
//                        nachricht += smsMessage.getDisplayMessageBody();
//                    }
//
//                    System.out.println("received");
//                    System.out.println(nachricht);
//
//                    // Here the message content is processed within MainAct
////                    MainActivity.instance()
////                            .processSMS(telnr.replace("+49", "0")
////                                    .replace(" ", ""), nachricht);
//                }
//            }
//        }
//
//        private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
//            SmsMessage smsMessage;
//
//            String format = bundle.getString("format");
//            smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
//
//            return smsMessage;
//        }
//    }
}