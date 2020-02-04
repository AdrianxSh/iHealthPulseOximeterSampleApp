package com.example.ihealthpulseoximetersampleapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ihealthpulseoximetersampleapp.base.BaseApplication;
import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.example.ihealthpulseoximetersampleapp.view.MainActivity;
import com.ihealth.communication.manager.DiscoveryTypeEnum;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConnectPresenter implements ConnectInterface.Presenter, ConnectInterface.onGetDataListener{

    private ConnectInterface.View view;

    private static final int HANDLER_SCAN = 101;
    private static final int HANDLER_CONNECTED = 102;
    private static final int HANDLER_DISCONNECTED = 103;

    private boolean authenticated;
    private Context context;
    private ConnectInterface.onGetDataListener listener;

    public ConnectPresenter(ConnectInterface.View v, Context c){
        view = v;
        context = c;
        listener = this;
    }

    @Override
    public void initConnection() {
        int callbackId = iHealthDevicesManager.getInstance().registerClientCallback(miHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(callbackId, iHealthDevicesManager.TYPE_PO3);
        authenticated = authenticate();
    }

    @Override
    public void connect(String mac, String deviceName) {
        if (authenticated) {
            boolean req = iHealthDevicesManager.getInstance().connectDevice("", mac, deviceName);
            if (!req) {
                Toast.makeText(context, "Authentication failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void scan() {
        iHealthDevicesManager.getInstance().startDiscovery(DiscoveryTypeEnum.PO3);
    }

    @Override
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

    @Override
    public void onSuccess(Device model) {
        view.onGetDataSuccess(model);
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
                    listener.onSuccess(device);
                    break;
                case HANDLER_CONNECTED:
                    Bundle bundleC = msg.getData();
                    String macC = bundleC.getString("mac");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("mac", macC);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, "Succesfully connected to PO3 device", Toast.LENGTH_LONG).show();
                    break;
                case HANDLER_DISCONNECTED:
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
}
