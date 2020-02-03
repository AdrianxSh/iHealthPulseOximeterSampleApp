package com.example.ihealthpulseoximetersampleapp.presenter;

import android.content.Context;

import com.example.ihealthpulseoximetersampleapp.model.Device;

public interface DeviceInterface {
    interface View{
        void onGetDataSuccess(Device model);
        void onGetDataFailure(String message);
    }

    interface Presenter{
        void getData(Context context);
    }

    interface InteractorInterface{
        void initCall(Context context);

    }

    interface onGetDataListener{
        void onSuccess(Device model);
        void onFailure(String message);
    }
}
