package com.example.ihealthpulseoximetersampleapp.model;

public class DeviceMeasurement {

    private int oxygen;
    private int pulseRate;
    private int battery;

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(String batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    private String batteryPercentage;

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }
}
