package com.hieubap.apptesta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SmsBroadCaster extends BroadcastReceiver {

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        System.out.println("ngon lành cành đào");
//        Toast.makeText(context, "Ngon lành cành đào", Toast.LENGTH_SHORT).show();
//    }
public String digestSha256(String s) {
    try {
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++){
            String current = "00" + Integer.toHexString(0xFF & messageDigest[i]);
            hexString.append(current.substring(current.length()-2));
        }
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
}

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            System.out.println("received_sms");
            StringBuilder str = new StringBuilder();
            for(SmsMessage s : smsMessages) {
                if(!s.getDisplayOriginatingAddress().equals("BIDV")){
                    Toast.makeText(context, "Service trigger", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(s.getDisplayMessageBody());//Mnop
                System.out.println(s.getDisplayOriginatingAddress());//+84961745160
                System.out.println(s.getEmailBody());//null
                System.out.println(s.getEmailFrom());//null
                System.out.println(s.getMessageBody());//Mnop
                System.out.println(s.getOriginatingAddress());//+84961745160
                System.out.println(s.getPseudoSubject());//+84980200615
                System.out.println(s.getServiceCenterAddress());//Mnop

                str.append(s.getDisplayMessageBody());
            }

            System.out.println(str);//Mnop
            System.out.println(str.toString());
            if(str.indexOf("BNB") == -1) return;

            String balanceStr = str.substring(str.indexOf("tai BIDV ") + 9, str.indexOf("VND vao"));
            Integer billIdStr = Integer.parseInt(str.substring(str.indexOf("BNB") + 3, str.indexOf("BNB") + 7));


            System.out.println(balanceStr);

            OkHttpClient client = new OkHttpClient();
            String sha256Str = digestSha256(billIdStr + "SECRET_KEY_TO_UPDATE_TRANSACTION");
            System.out.println(sha256Str);
            String jsonString = "{\"balance\":\""+
                    balanceStr +
                    "\",\"validate\":\""+
                    sha256Str+"\"}";
            System.out.println(jsonString);
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"),jsonString);
            Request request = new Request.Builder()
                    .url("https://api-gala.bapber.online/market/transfer-success/" + billIdStr)
                    .addHeader("Content-Type", "application/json")
                    .put(jsonData)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}

                @Override
                public void onResponse(Call call, Response response) throws IOException {}
            });;

            Toast.makeText(context, jsonString, Toast.LENGTH_SHORT).show();
        }else{
//            String str = "TK 440xxx8734 tai BIDV +10,000VND vao 19:13 12/11/2023. So du:46,608,887VND. ND: TKThe :3201537, tai ACB. BNB0007-121123-19:13:00 000012";
//            String balanceStr = str.substring(str.indexOf("tai BIDV ") + 9, str.indexOf("VND vao"));
//            Integer billIdStr = Integer.parseInt(str.substring(str.indexOf("BNB") + 3, str.indexOf("BNB") + 7));
//            System.out.println(balanceStr);
//
//            OkHttpClient client = new OkHttpClient();
//            String sha256Str = digestSha256(billIdStr + "SECRET_KEY_TO_UPDATE_TRANSACTION");
//            System.out.println(sha256Str);
//            String jsonString = "{\"balance\":\""+
//                    balanceStr +
//                    "\",\"validate\":\""+
//                    sha256Str+"\"}";
//            System.out.println(jsonString);
//            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"),jsonString);
//            Request request = new Request.Builder()
//                    .url("https://api-gala.bapber.online/market/transfer-success/" + billIdStr)
//                    .addHeader("Content-Type", "application/json")
//                    .put(jsonData)
//                    .build();
//
//            client.newCall(request);
            Toast.makeText(context, "Service trigger", Toast.LENGTH_SHORT).show();
        }


//            String telnr = "", nachricht = "";
//
//            Bundle extras = intent.getExtras();

//            if (extras != null) {
//                Object[] pdus = (Object[]) extras.get("pdus");
//                if (pdus != null) {
//
//                    for (Object pdu : pdus) {
//                        SmsMessage smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(pdu, extras);
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

//        private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
//            SmsMessage smsMessage;
//
//            String format = bundle.getString("format");
//            smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
//
//            return smsMessage;
//        }
    }

//    SmsReceiver smsReceiver;

//    @Override
//    public void onCreate() {
//        smsReceiver = new SmsReceiver();
//        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//        System.out.println("Create Service");
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String name = intent.getStringExtra("name");
//        Toast.makeText(
//                getApplicationContext(), "Service has started running in the background",
//                Toast.LENGTH_SHORT
//        ).show();
//        System.out.println("Start Service");
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
////        stopSelf();
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        Toast.makeText(
//                getApplicationContext(), "Service execution completed",
//                Toast.LENGTH_SHORT
//        ).show();
//        super.onDestroy();
//        unregisterReceiver(smsReceiver);
//    }
//
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
