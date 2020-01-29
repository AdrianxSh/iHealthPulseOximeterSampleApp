package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ihealthpulseoximetersampleapp.base.BaseApplication;
import com.ihealth.communication.manager.iHealthDevicesManager;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticateActivity extends AppCompatActivity {

    @BindView(R.id.btnAuthenticate)Button btnAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        ButterKnife.bind(this);

        btnAuthenticate.setOnClickListener(v -> onButtonClicked());
    }

    public void onButtonClicked(){
        try {
            InputStream is  = BaseApplication.instance().getAssets().open("com_example_ihealthpulseoximetersampleapp_android.pem");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            boolean isPass = iHealthDevicesManager.getInstance().sdkAuthWithLicense(buffer);
            if(isPass){
                Intent intent = new Intent(this, ConnectActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Authentication failed!", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
