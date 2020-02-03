package com.example.ihealthpulseoximetersampleapp.interactor;

import android.content.Context;

import com.example.ihealthpulseoximetersampleapp.presenter.ConnectInterface;
import com.example.ihealthpulseoximetersampleapp.presenter.DeviceInterface;

public class ConnectInteractor implements DeviceInterface.InteractorInterface {

    private ConnectInterface.onGetDataListener listener;

    public ConnectInteractor(ConnectInterface.onGetDataListener onGetDataListener){
        listener = onGetDataListener;
    }

    @Override
    public void initCall(Context context) {

    }
}
