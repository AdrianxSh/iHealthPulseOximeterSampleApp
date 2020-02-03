package com.example.ihealthpulseoximetersampleapp.presenter;

import android.content.Context;

import com.example.ihealthpulseoximetersampleapp.interactor.DeviceInteractor;
import com.example.ihealthpulseoximetersampleapp.model.Device;

public class DevicePresenter implements DeviceInterface.Presenter, DeviceInterface.onGetDataListener {

    private DeviceInterface.View view;
    private DeviceInteractor interactor;

    public DevicePresenter(DeviceInterface.View v){
        view = v;
        interactor = new DeviceInteractor(this);
    }

    @Override
    public void getData(Context context) {

    }

    @Override
    public void onSuccess(Device model) {
        view.onGetDataSuccess(model);
    }

    @Override
    public void onFailure(String message) {
        view.onGetDataFailure(message);
    }
}
