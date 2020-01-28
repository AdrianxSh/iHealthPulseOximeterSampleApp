package com.example.ihealthpulseoximetersampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ic_battery)ImageButton battery;
    @BindView(R.id.arc_SpO2)ArcProgress oxygenRate;
    @BindView(R.id.btnStart)Button start;
    @BindView(R.id.btnStop)Button stop;
    @BindView(R.id.txt_heartBeat)TextView heartBeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }
}
