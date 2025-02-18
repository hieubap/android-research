package com.hieubap.apptesta;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String deviceAddress = "XX:XX:XX:XX:XX:XX"; // Replace with Device B's Bluetooth MAC address
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard UUID for SPP

    public void startPing() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();

            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // Send Ping and measure time
            long startTime = System.currentTimeMillis();
            String pingMessage = "ping";
            outputStream.write(pingMessage.getBytes());

            // Wait for response
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            long endTime = System.currentTimeMillis();

            String response = new String(buffer, 0, bytesRead);
            if ("pong".equals(response)) {
                long pingTime = endTime - startTime;
                Log.d("BluetoothPingSender", "Ping time: " + pingTime + " ms");
            }

        } catch (IOException e) {
//            Log.e("BluetoothPingSender", "Error", e);
        } finally {
            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
