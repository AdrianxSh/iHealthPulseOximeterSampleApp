package com.example.ihealthpulseoximetersampleapp.presenter;

import android.content.Context;

import com.example.ihealthpulseoximetersampleapp.interactor.ConnectInteractor;
import com.example.ihealthpulseoximetersampleapp.model.Device;

public class ConnectPresenter implements ConnectInterface.Presenter, ConnectInterface.onGetDataListener{

    private ConnectInterface.View view;
    private ConnectInteractor interactor;

    public ConnectPresenter(ConnectInterface.View v){
        view = v;
        interactor = new ConnectInteractor(this);
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
