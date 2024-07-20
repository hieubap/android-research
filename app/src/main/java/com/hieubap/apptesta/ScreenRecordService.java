package com.hieubap.apptesta;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.Arrays;

public class ScreenRecordService extends Service {
//    private int REQUEST_CODE = 1000;
//    private int REQUEST_PERMISSION = 1001;
//    MediaProjectionManager mediaProjectionManager;
//    MediaProjection mediaProjection;
//    VirtualDisplay virtualDisplay;
//    CaptureActivity.MediaProjectionCallback mediaProjectionCallback;
//
//    private int mScreenDensity = 0;
//    private int DISPLAY_WIDTH = 720;
//    private int DISPLAY_HEIGHT = 1280;
//
//    MediaRecorder mediaRecorder;
//    Button toggleButton;
//    VideoView videoView;
//
//    String videoUrl;
//
//    boolean isChecked = false;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mScreenDensity = metrics.densityDpi;
//        mediaRecorder = new MediaRecorder();
//        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//
//        videoView = findViewById(R.id.videoView);
//        toggleButton = findViewById(R.id.toggleBtn);
//
//        toggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(ContextCompat.checkSelfPermission(CaptureActivity.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(CaptureActivity.this,
//                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                ){
//                    isChecked = false;
//                    ActivityCompat.requestPermissions(
//                            CaptureActivity.this,
//                            Arrays.asList(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.RECORD_AUDIO).toArray(new String[2]),
//                            REQUEST_CODE
//                    );
//                }else {
//                    toggleScreen(toggleButton);
//                }
//            }
//        });
//    }
//
//
//    void toggleScreen(Button toggleButton){
//        if(!isChecked){
//            initRecorder();
//            recordScreen();
//            isChecked = true;
//            toggleButton.setText("STOP");
//        }else{
//            try {
//                mediaRecorder.stop();
//                mediaRecorder.reset();
//                stopRecorderScreen();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            videoView.setVisibility(View.VISIBLE);
//            videoView.setVideoURI(Uri.parse(videoUrl));
//            videoView.start();
//            isChecked = false;
//            toggleButton.setText("START");
//
//        }
//    }
//
//    private void stopRecorderScreen() {
//        if(virtualDisplay == null){
//            return;
//        }
//        virtualDisplay.release();
//        destroyMediaProjection();
//    }
//
//    private void destroyMediaProjection() {
//        if(mediaProjection != null){
//            mediaProjection.unregisterCallback(mediaProjectionCallback);
//            mediaProjection.stop();
//            mediaProjection = null;
//        }
//    }
//
//    private void recordScreen() {
//        if(mediaProjection == null){
//            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
//        }
//        virtualDisplay = createVirtualDisplay();
//        try {
//            mediaRecorder.start();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private VirtualDisplay createVirtualDisplay(){
//        if(mediaProjection != null)
//            return mediaProjection.createVirtualDisplay(
//                    "CaptureActivity",DISPLAY_WIDTH, DISPLAY_HEIGHT,
//                    mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                    mediaRecorder.getSurface(),null,null
//            );
//        return null;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode != REQUEST_CODE){
//            Toast.makeText(this, "Unk Error",Toast.LENGTH_LONG).show();
//            return;
//        }
//        System.out.println(requestCode);
//        System.out.println(resultCode);
//        System.out.println("REQUEST_PERMISSION: " + RESULT_OK);
//        if (resultCode != RESULT_OK){
//            Toast.makeText(this, "Permission denied",Toast.LENGTH_LONG).show();
//            isChecked = false;
//            return;
//        }
//
//        mediaProjectionCallback = new CaptureActivity.MediaProjectionCallback(mediaRecorder, mediaProjection);
//        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
//        mediaProjection.registerCallback(mediaProjectionCallback, null);
//
//        virtualDisplay = createVirtualDisplay();
//        try {
//            mediaRecorder.start();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void initRecorder() {
//        try{
//            String recordingFile = ("recordREC"+System.currentTimeMillis()+".mp4");
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//            File newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File folder = new File(newPath, "MyScreenREC/");
//            if(!folder.exists()){
//                folder.mkdirs();
//            }
//            File file1 = new File(folder,recordingFile);
//            videoUrl = file1.getAbsolutePath();
//
//            mediaRecorder.setOutputFile(videoUrl);
//            mediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mediaRecorder.setVideoEncodingBitRate(512*1000);
//            mediaRecorder.setVideoFrameRate(30);
//
////            int rotation = getWindowManager().getDefaultDisplay().getRotation();
////            int orientation = 90;
//            mediaRecorder.setOrientationHint(90);
//            mediaRecorder.prepare();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    class MediaProjectionCallback extends MediaProjection.Callback{
//        MediaRecorder mediaRecorder;
//        MediaProjection mediaProjection;
//
//        public MediaProjectionCallback(MediaRecorder mediaRecorder, MediaProjection mediaProjection) {
//            this.mediaRecorder = mediaRecorder;
//            this.mediaProjection = mediaProjection;
//        }
//
//        @Override
//        public void onStop() {
//            if(isChecked){
//                isChecked = false;
//                mediaRecorder.stop();
//                mediaRecorder.reset();
//            }
//            mediaProjection = null;
//            stopRecorderScreen();
//            super.onStop();
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
