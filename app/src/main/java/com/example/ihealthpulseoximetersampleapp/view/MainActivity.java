package com.example.ihealthpulseoximetersampleapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ihealthpulseoximetersampleapp.R;
import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.example.ihealthpulseoximetersampleapp.model.DeviceMeasurement;
import com.example.ihealthpulseoximetersampleapp.presenter.DeviceInterface;
import com.example.ihealthpulseoximetersampleapp.presenter.DevicePresenter;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.ihealth.communication.control.Po3Control;
import com.ihealth.communication.control.PoProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DeviceInterface.View {

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

    private DevicePresenter devicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String deviceMac = intent.getStringExtra("mac");

        devicePresenter = new DevicePresenter(this, getApplicationContext(), deviceMac);
        devicePresenter.initController();

        showBattery();
        measureBtn.setOnClickListener(v -> startMeasuring());

        Timer timer = new Timer();
        TimerTask batteryTask = new TimerTask() {
            @Override
            public void run() {
                showBattery();
            }
        };

        timer.schedule(batteryTask, 1, 1000 * 60);
    }

    private void startMeasuring() {
        devicePresenter.startMeasuring();
    }

    private void showBattery() {
        devicePresenter.showBattery();
    }

    @Override
    protected void onDestroy() {
        devicePresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetDataSuccess(DeviceMeasurement model) {
        oxygenRate.setProgress(model.getOxygen());
        heartBeat.setText(String.valueOf(model.getPulseRate()));
        measureBtn.setEnabled(false);
    }

    @Override
    public void onGetDataSuccessImage(DeviceMeasurement model) {
        devicePresenter.setImage(batteryImage, model.getBattery());
        batteryPercentage.setText(model.getBatteryPercentage());
    }
}
