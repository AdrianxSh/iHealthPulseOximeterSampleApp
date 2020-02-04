package com.example.ihealthpulseoximetersampleapp.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.example.ihealthpulseoximetersampleapp.R;
import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.example.ihealthpulseoximetersampleapp.model.DeviceMeasurement;
import com.ihealth.communication.control.Po3Control;
import com.ihealth.communication.control.PoProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class DevicePresenter implements DeviceInterface.Presenter, DeviceInterface.onGetDataListener {

    private final String deviceMac;
    private DeviceInterface.View view;
    private Context context;
    private DeviceInterface.onGetDataListener listener;
    private Po3Control mPo3Control;


    public DevicePresenter(DeviceInterface.View v, Context c, String mac){
        view = v;
        context = c;
        deviceMac = mac;
        listener = this;
    }

    @Override
    public void initController() {
        int mClientCallbackId = iHealthDevicesManager.getInstance().registerClientCallback(miHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(mClientCallbackId, iHealthDevicesManager.TYPE_PO3);
        mPo3Control = iHealthDevicesManager.getInstance().getPo3Control(deviceMac);
    }

    @Override
    public void startMeasuring() {
        mPo3Control.startMeasure();
    }

    @Override
    public void showBattery() {
        mPo3Control.getBattery();
    }

    @Override
    public void destroy() {
        if (mPo3Control != null) {
            mPo3Control.disconnect();
        }
    }

    @Override
    public void setImage(ImageView imageView, int value) {
        runOnUiThread(() -> {
            if (value == 100)
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_foreground));
            else if ((value < 100) && (value >= 90))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_90));
            else if ((value < 90) && (value >= 80))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_80));
            else if ((value < 80) && (value >= 60))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_60));
            else if ((value < 60) && (value >= 50))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_50));
            else if ((value < 50) && (value >= 30))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_30));
            else if ((value < 30) && (value >= 20))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_20));
            else if ((value < 20) && (value >= 0))
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_low));
            else
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_battery_unknown));
        });
    }

    @Override
    public void onSuccess(DeviceMeasurement model) {
        view.onGetDataSuccess(model);
    }

    @Override
    public void onSuccessSetImage(DeviceMeasurement model) {
        view.onGetDataSuccessImage(model);
    }

    private iHealthDevicesCallback miHealthDevicesCallback = new iHealthDevicesCallback() {
        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            JSONTokener jsonTokener = new JSONTokener(message);
            switch (action) {
                case PoProfile.ACTION_LIVEDA_PO:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        int oxygen = jsonObject.getInt(PoProfile.BLOOD_OXYGEN_PO);
                        int pulseRate = jsonObject.getInt(PoProfile.PULSE_RATE_PO);
                        DeviceMeasurement deviceMeasurement = new DeviceMeasurement();
                        deviceMeasurement.setOxygen(oxygen);
                        deviceMeasurement.setPulseRate(pulseRate);
                        listener.onSuccess(deviceMeasurement);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case PoProfile.ACTION_RESULTDATA_PO:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        String dataId = jsonObject.getString(PoProfile.DATAID);
                        int oxygen = jsonObject.getInt(PoProfile.BLOOD_OXYGEN_PO);
                        int pulseRate = jsonObject.getInt(PoProfile.PULSE_RATE_PO);
                        DeviceMeasurement deviceMeasurement = new DeviceMeasurement();
                        deviceMeasurement.setOxygen(oxygen);
                        deviceMeasurement.setPulseRate(pulseRate);
                        listener.onSuccess(deviceMeasurement);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case PoProfile.ACTION_BATTERY_PO:
                    JSONObject jsonobject;
                    try {
                        jsonobject = (JSONObject) jsonTokener.nextValue();
                        int battery = jsonobject.getInt(PoProfile.BATTERY_PO);
                        String batteryPerc = battery + "%";
                        DeviceMeasurement deviceMeasurement = new DeviceMeasurement();
                        deviceMeasurement.setBattery(battery);
                        deviceMeasurement.setBatteryPercentage(batteryPerc);
                        listener.onSuccessSetImage(deviceMeasurement);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
}
