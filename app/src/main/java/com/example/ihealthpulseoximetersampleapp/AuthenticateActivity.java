package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;

public class AuthenticateActivity extends AppCompatActivity {

    @BindView(R.id.btnAuthenticate)Button authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AuthenticateActivity.this, ConnectActivity.class);
                AuthenticateActivity.this.startActivity(i);
            }
        });
    }
}
