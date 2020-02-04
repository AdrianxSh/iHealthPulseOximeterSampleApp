package com.example.ihealthpulseoximetersampleapp.presenter;


import com.example.ihealthpulseoximetersampleapp.model.Device;

public interface ConnectInterface {
    interface View{
        void onGetDataSuccess(Device model);
    }

    interface Presenter{
        void initConnection();
        void connect(String mac, String deviceName);
        void scan();
        boolean authenticate();
    }

    interface onGetDataListener{
        void onSuccess(Device model);
    }
}
