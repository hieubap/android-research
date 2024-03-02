package com.hieubap.apptesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hieubap.apptesta.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding fragmentChatBinding;
    private MiddleSmsService middleSmsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager pk = getApplicationContext().getPackageManager();
//        boolean face = false;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            face = pk.hasSystemFeature(PackageManager.FEATURE_FACE);
//        }
//        boolean finger = pk.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

//        new BiometricPrompt.PromptInfo.Builder().setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG).build();
//        BiometricPrompt.Builder().
//        BiometricPrompt.Builder(getApplicationContext());
        System.out.println("TESTING+++++");
//        System.out.println(face + "" + finger);

        String str = "TK 440xxx8734 tai BIDV -120,000VND vao 15:32 12/11/2023. So du:1,232,545,654VND. ND: MB-TKThe :1001527646, tai SHB. ND  BNB0005";

        String s2 = str.substring(str.indexOf("tai BIDV ") + 9, str.indexOf("VND vao"));

        System.out.println(s2);
        System.out.println(Integer.parseInt(str.substring(str.length()-4)));
        System.out.println("s1 visible");

//        middleSmsService = new MiddleSmsService();
//        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
//        registerReceiver(middleSmsService, filter);

        // isCharging if true indicates charging is ongoing and vice-versa
//        Toast.makeText(applicationContext,"Not Charging", Toast.LENGTH_LONG).show()

        fragmentChatBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(fragmentChatBinding.getRoot());

        Button biometricLoginButton = fragmentChatBinding.btnBiometric;
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        Button btnStart = fragmentChatBinding.startServiceBtn;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onClick to Start service");
                Intent intent = new Intent(MainActivity.this, MiddleSmsService.class);
                startService(intent);

//                Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent2);
//                intent.putExtra("name","Data send from AuthActivity");

//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.finish();
//                MainActivity.this.onDestroy();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(intent);
//                }else{
//                    startService(intent);
//                }
            }
        });

        Button btnToast = fragmentChatBinding.showToast;
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("show toast");
                Toast.makeText(MainActivity.this.getApplicationContext(),"Show toast",Toast.LENGTH_LONG).show();
            }
        });


        System.out.println("INIT_SUCCESS+++++++++");

        Uri uri = Uri.parse("content://group.ivirse.sharing/share_data?key=123");

// Create a ContentResolver
        ContentResolver contentResolver = getContentResolver();

// Perform a query to retrieve data
        Cursor cursor = contentResolver.query(uri, null,null,null,null);

        System.out.println("CURSOR_SUCCESS+++++++++"+(cursor==null) +" ; " + uri);

// Process the data from the Cursor
        if (cursor != null) {
            System.out.println("CURSOR_NOT_NULL+++++++++");
            while (cursor.moveToNext()) {
                int i = cursor.getColumnIndex("data");
                System.out.println("i=" + i + ", ============");
                if(i >= 0){
                    String s = cursor.getString(i);
                    System.out.println("VALUE=" + s);
                }

                // Extract data from the cursor
                // ...
            }
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}