package com.example.ihealthpulseoximetersampleapp.interactor;

import android.content.Context;

import com.example.ihealthpulseoximetersampleapp.presenter.DeviceInterface;

public class DeviceInteractor implements DeviceInterface.InteractorInterface {

    private DeviceInterface.onGetDataListener mOnGetDataListener;

    public DeviceInteractor(DeviceInterface.onGetDataListener listener){
        mOnGetDataListener = listener;
    }

    @Override
    public void initCall(Context context) {

    }
}
