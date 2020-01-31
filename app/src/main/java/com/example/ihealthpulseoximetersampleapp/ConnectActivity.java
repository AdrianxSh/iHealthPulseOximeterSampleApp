package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.easylibrary.utils.ToastUtils;
import com.example.ihealthpulseoximetersampleapp.adapter.DeviceAdapter;
import com.example.ihealthpulseoximetersampleapp.base.BaseApplication;
import com.ihealth.communication.control.HsProfile;
import com.ihealth.communication.manager.DiscoveryTypeEnum;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity {

    @BindView(R.id.btnConnect)
    Button scan;
    @BindView(R.id.deviceRecycler)
    RecyclerView recyclerView;


    public static final int HANDLER_SCAN = 101;
    public static final int HANDLER_CONNECTED = 102;

    private String deviceName;
    private String deviceMac;
    private int callbackId;
    private boolean authenticated;

    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);

        bindAdapter();

        initConnection();

        scan.setOnClickListener(v -> scan());

    }

    private void initConnection() {

        callbackId = iHealthDevicesManager.getInstance().registerClientCallback(miHealthDevicesCallback);

        authenticated = authenticate();
    }

    public void connect() {
        if (authenticated) {
            boolean req = iHealthDevicesManager.getInstance().connectDevice("", deviceMac, deviceName);
            if (!req) {
                Toast.makeText(this, "Authentication failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean authenticate() {
        try {
            InputStream is = BaseApplication.instance().getAssets().open("com_example_ihealthpulseoximetersampleapp_android.pem");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            boolean isPass = iHealthDevicesManager.getInstance().sdkAuthWithLicense(buffer);
            if (isPass) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private iHealthDevicesCallback miHealthDevicesCallback = new iHealthDevicesCallback() {

        @Override
        public void onScanDevice(String mac, String deviceType, int rssi, Map<String, Object> manufactorData) {
            Message msg = new Message();
            deviceMac = mac;
            deviceName = deviceType;
            msg.what = HANDLER_SCAN;
            myHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status, int errorID, Map manufactorData) {
            Message msg = new Message();

            if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTED) {
                msg.what = HANDLER_CONNECTED;
                myHandler.sendMessage(msg);
            } else {
                Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onScanFinish() {
            super.onScanFinish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SCAN:
                    deviceAdapter.addDevice(deviceMac);
                    break;
                case HANDLER_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Succesfully connected to PO3 device", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    intent.putExtra("mac", deviceMac);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Succesfully connected to PO3 device", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void scan() {
        iHealthDevicesManager.getInstance().startDiscovery(DiscoveryTypeEnum.PO3);
    }

    private void bindAdapter() {
        deviceAdapter = new DeviceAdapter(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);
    }

}
