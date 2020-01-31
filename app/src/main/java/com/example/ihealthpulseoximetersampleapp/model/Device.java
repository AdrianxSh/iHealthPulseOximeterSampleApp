package com.example.ihealthpulseoximetersampleapp.model;

import androidx.annotation.Nullable;

public class Device {

    private String type;
    private String mac;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Device && ((Device)obj).getMac().equals(mac);
    }
}
