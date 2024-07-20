package com.hieubap.apptesta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class ScreenCaptureActivity extends Activity {

    private static final int REQUEST_CODE_ = 1000;
    private static final int REQUEST_CODE = 100;

    /****************************************** Activity Lifecycle methods ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // start projection
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startProjection();
            }
        });

        // stop projection
        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopProjection();
            }
        });
        Button appearTopButton = findViewById(R.id.appearTopButton);
        appearTopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Settings.canDrawOverlays(ScreenCaptureActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Intent intent = new Intent(ScreenCaptureActivity.this, OverlayService.class);
                    startService(intent);
                }
            }
        });

        Button triggerBtn = findViewById(R.id.receiverBtn);
        triggerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScreenCaptureActivity.this, MiddleSmsService.class);
                startService(intent);
                Toast.makeText(
                        getApplicationContext(), "Run",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        Button permissionBtn = findViewById(R.id.permissionBtn);
        permissionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//                startActivityForResult(intent);
                Toast.makeText(
                        getApplicationContext(), "Run",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startService(ScreenCaptureService.getStartIntent(this, resultCode, data));
            }
        }

        if (requestCode == REQUEST_CODE_) {
            if (Settings.canDrawOverlays(this)) {
                // Permission granted, start the overlay service
                Intent intent = new Intent(this, OverlayService.class);
                startService(intent);
            } else {
                // Permission not granted, show a message to the user
                Toast.makeText(this, "Overlay permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/
    private void startProjection() {
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    private void stopProjection() {
        startService(ScreenCaptureService.getStopIntent(this));
    }

}