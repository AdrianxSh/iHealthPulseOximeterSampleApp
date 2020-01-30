package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ihealthpulseoximetersampleapp.base.BaseApplication;
import com.ihealth.communication.manager.iHealthDevicesManager;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity {

    @BindView(R.id.btnConnect)
    Button connect;
    @BindView(R.id.pair)
    TextView pairDevice;

    private String deviceName;
    private String deviceMac;
    private boolean authenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);

        deviceName = "PO3";
        deviceMac = "907065F47324";

        authenticated = authenticate();

        pairDevice.setOnClickListener(v->{
            Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
            intent.putExtra("mac", deviceMac);
            startActivity(intent);
        });

        connect.setOnClickListener(v -> connect());
    }

    private void connect() {
        if (authenticated) {
            boolean req = iHealthDevicesManager.getInstance().connectDevice("", deviceMac, deviceName);
            if(req) {
                Toast.makeText(getApplicationContext(), "Succesfully connected to PO3 device", Toast.LENGTH_SHORT).show();
                pairDevice.setText("Connect to device");
            }
        } else {
            Toast.makeText(this, "Authentication failed!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean authenticate() {
        try {
            InputStream is = BaseApplication.instance().getAssets().open("com_example_ihealthpulseoximetersampleapp_android.pem");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            boolean isPass = iHealthDevicesManager.getInstance().sdkAuthWithLicense(buffer);
            if (isPass) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
