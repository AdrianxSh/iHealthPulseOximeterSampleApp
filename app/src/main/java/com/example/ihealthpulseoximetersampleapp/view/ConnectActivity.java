package com.example.ihealthpulseoximetersampleapp.view;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ihealthpulseoximetersampleapp.R;
import com.example.ihealthpulseoximetersampleapp.adapter.DeviceAdapter;
import com.example.ihealthpulseoximetersampleapp.model.Device;
import com.example.ihealthpulseoximetersampleapp.presenter.ConnectInterface;
import com.example.ihealthpulseoximetersampleapp.presenter.ConnectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity implements ConnectInterface.View, DeviceAdapter.DeviceAdapterCallback {

    @BindView(R.id.btnConnect)
    Button scan;
    @BindView(R.id.deviceRecycler)
    RecyclerView recyclerView;

    private static final int LOCATION_PERMISSION_CODE = 1;

    private ConnectPresenter connectPresenter;
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);

        connectPresenter = new ConnectPresenter(this, getApplicationContext());

        bindAdapter();

        connectPresenter.initConnection();

        scan.setOnClickListener(v -> {
            turnOnBluetooth();
            turnOnLocation();
            requestLocationPermission();
            connectPresenter.scan();
        });

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(ConnectActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ConnectActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    private void turnOnBluetooth() {
        final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bAdapter.isEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Bluetooth enable")
                    .setMessage("This app requires to turn bluetooth on!")
                    .setPositiveButton("Turn on", (dialog, which) -> {
                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ConnectActivity.this.startActivityForResult(i, 1);
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.ic_bluetooth_foreground)
                    .show();
        }
    }

    private void turnOnLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location enable")
                    .setMessage("This app requires to turn location on!")
                    .setPositiveButton("Turn on", (dialog, which) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.ic_location_foreground)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(getApplicationContext(), "Location permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindAdapter() {
        deviceAdapter = new DeviceAdapter(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);
    }

    @Override
    public void onGetDataSuccess(Device model) {
        deviceAdapter.addDevice(model);
    }

    @Override
    public void onDeviceSelected(Device device) {
        connectPresenter.connect(device.getMac(), device.getType());
    }
}
