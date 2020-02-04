package com.example.ihealthpulseoximetersampleapp.presenter;

import android.widget.ImageView;

import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.example.ihealthpulseoximetersampleapp.model.DeviceMeasurement;
import com.ihealth.communication.control.Po3Control;

public interface DeviceInterface {
    interface View{
        void onGetDataSuccess(DeviceMeasurement model);
        void onGetDataSuccessImage(DeviceMeasurement model);
    }

    interface Presenter{
        void initController();
        void startMeasuring();
        void showBattery();
        void destroy();
        void setImage(final ImageView imageView, final int value);
    }

    interface onGetDataListener{
        void onSuccess(DeviceMeasurement model);
        void onSuccessSetImage(DeviceMeasurement model);
    }
}
