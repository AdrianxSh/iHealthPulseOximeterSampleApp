package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);

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
