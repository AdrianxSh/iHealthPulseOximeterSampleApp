package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.ihealth.communication.control.Po3Control;
import com.ihealth.communication.control.PoProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ic_battery)
    ImageView batteryImage;
    @BindView(R.id.arc_SpO2)
    ArcProgress oxygenRate;
    @BindView(R.id.btnStart)
    Button measureBtn;
    @BindView(R.id.txt_heartBeat)
    TextView heartBeat;
    @BindView(R.id.txt_battery)
    TextView batteryPercentage;

    private int mClientCallbackId;
    private Po3Control mPo3Control;

    private String deviceMac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        Intent intent = getIntent();
        deviceMac = intent.getStringExtra("mac");

        mClientCallbackId = iHealthDevicesManager.getInstance().registerClientCallback(miHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(mClientCallbackId, iHealthDevicesManager.TYPE_PO3);
        mPo3Control = iHealthDevicesManager.getInstance().getPo3Control(deviceMac);

        showBattery();
        measureBtn.setOnClickListener(v -> startMeasuring());

        Timer timer=new Timer();
        TimerTask batteryTask=new TimerTask() {
            @Override
            public void run() {
                showBattery();
            }
        };

        timer.schedule(batteryTask,01,1000*60);
    }

    private void startMeasuring() {
        mPo3Control.startMeasure();
    }

    private void showBattery() {
        mPo3Control.getBattery();
    }

    private iHealthDevicesCallback miHealthDevicesCallback = new iHealthDevicesCallback() {
        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            JSONTokener jsonTokener = new JSONTokener(message);
            switch (action) {
                case PoProfile.ACTION_LIVEDA_PO:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        int oxygen = jsonObject.getInt(PoProfile.BLOOD_OXYGEN_PO);
                        int pulseRate = jsonObject.getInt(PoProfile.PULSE_RATE_PO);
                        oxygenRate.setProgress(oxygen);
                        heartBeat.setText(String.valueOf(pulseRate));
                        measureBtn.setEnabled(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case PoProfile.ACTION_RESULTDATA_PO:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        String dataId = jsonObject.getString(PoProfile.DATAID);
                        int oxygen = jsonObject.getInt(PoProfile.BLOOD_OXYGEN_PO);
                        int pulseRate = jsonObject.getInt(PoProfile.PULSE_RATE_PO);
                        oxygenRate.setProgress(oxygen);
                        heartBeat.setText(String.valueOf(pulseRate));
                        measureBtn.setEnabled(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case PoProfile.ACTION_BATTERY_PO:
                    JSONObject jsonobject;
                    try {
                        jsonobject = (JSONObject) jsonTokener.nextValue();
                        int battery = jsonobject.getInt(PoProfile.BATTERY_PO);
                        batteryPercentage.setText(battery+"%");
                        setImage(batteryImage,battery);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    protected void onDestroy() {
        if(mPo3Control != null){
            mPo3Control.disconnect();
        }
        iHealthDevicesManager.getInstance().unRegisterClientCallback(mClientCallbackId);
        super.onDestroy();
    }

    private void setImage(final ImageView imageView, final int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(value==100)
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_foreground));
                else if((value<100)&&(value>=90))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_90));
                else if((value<90)&&(value>=80))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_80));
                else if((value<80)&&(value>=60))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_60));
                else if((value<60)&&(value>=50))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_50));
                else if((value<50)&&(value>=30))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_30));
                else if((value<30)&&(value>=20))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_20));
                else if((value<20)&&(value>=00))
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_low));
                else
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_unknown));
            }
        });
    }
}
