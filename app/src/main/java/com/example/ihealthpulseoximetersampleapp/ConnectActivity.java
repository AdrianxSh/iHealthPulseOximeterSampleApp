package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity {

    @BindView(R.id.btnConnect)Button connect;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device not supported!",Toast.LENGTH_SHORT).show();
        }

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                ConnectActivity.this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Succesfully connected to PO3 device",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
