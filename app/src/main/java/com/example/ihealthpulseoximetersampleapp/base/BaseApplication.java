package com.example.ihealthpulseoximetersampleapp.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ihealth.communication.manager.iHealthDevicesManager;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        applicationContext = this;
        init();
    }

    private void init() {
        iHealthDevicesManager.getInstance().init(this,  Log.VERBOSE, Log.VERBOSE);
    }

    public static BaseApplication instance() {
        return mInstance;
    }
}
