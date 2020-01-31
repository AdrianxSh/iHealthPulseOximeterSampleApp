package com.example.ihealthpulseoximetersampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.ihealthpulseoximetersampleapp.adapter.DeviceAdapter;
import com.example.ihealthpulseoximetersampleapp.base.BaseApplication;
import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.ihealth.communication.manager.DiscoveryTypeEnum;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_CODE = 1;
    @BindView(R.id.btnConnect)
    Button scan;
    @BindView(R.id.deviceRecycler)
    RecyclerView recyclerView;


    public static final int HANDLER_SCAN = 101;
    public static final int HANDLER_CONNECTED = 102;
    public static final int HANDLER_DISCONNECTED = 103;

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

        scan.setOnClickListener(v -> {
            requestLocationPermission();
            scan();
        });

    }

    private void initConnection() {
        callbackId = iHealthDevicesManager.getInstance().registerClientCallback(miHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(callbackId, iHealthDevicesManager.TYPE_PO3);
        authenticated = authenticate();
    }

    public void connect(String mac, String deviceName) {
        if (authenticated) {
            boolean req = iHealthDevicesManager.getInstance().connectDevice("", mac, deviceName);
            if (!req) {
                Toast.makeText(this, "Authentication failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(ConnectActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ConnectActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(getApplicationContext(), "Location permission denied!", Toast.LENGTH_SHORT).show();
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
            Bundle bundle = new Bundle();
            bundle.putString("mac", mac);
            bundle.putString("type", deviceType);
            msg.what = HANDLER_SCAN;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status, int errorID, Map manufactorData) {
            Message msg = new Message();

            if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTED) {
                Bundle bundle = new Bundle();
                bundle.putString("mac", mac);
                bundle.putString("type", deviceType);
                msg.what = HANDLER_CONNECTED;
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
            if (status == iHealthDevicesManager.DEVICE_STATE_DISCONNECTED) {
                msg.what = HANDLER_DISCONNECTED;
                myHandler.sendMessage(msg);
            }
        }

        @Override
        public void onScanFinish() {
            super.onScanFinish();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SCAN:
                    Bundle bundle = msg.getData();
                    String mac = bundle.getString("mac");
                    String type = bundle.getString("type");
                    Device device = new Device();
                    Log.d("testtest", "Device mac: " + mac);
                    device.setMac(mac);
                    device.setType(type);
                    deviceAdapter.addDevice(device);
                    break;
                case HANDLER_CONNECTED:
                    Bundle bundleC = msg.getData();
                    String macC = bundleC.getString("mac");
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    intent.putExtra("mac", macC);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Succesfully connected to PO3 device", Toast.LENGTH_LONG).show();
                    break;
                case HANDLER_DISCONNECTED:
                    Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
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
