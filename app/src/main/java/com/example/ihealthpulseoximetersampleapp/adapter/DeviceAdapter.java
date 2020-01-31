package com.example.ihealthpulseoximetersampleapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihealthpulseoximetersampleapp.ConnectActivity;
import com.example.ihealthpulseoximetersampleapp.R;
import com.example.ihealthpulseoximetersampleapp.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<Device> devices = new ArrayList<>();
    private ConnectActivity connectActivity;

    public DeviceAdapter(ConnectActivity ca) {
        connectActivity = ca;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listDevices = layoutInflater.inflate(R.layout.device_item, parent, false);
        return new ViewHolder(listDevices);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillView(devices.get(position).getMac());
        holder.deviceId.setOnClickListener(v -> {
            Device currentDevice = devices.get(position);
            connectActivity.connect(currentDevice.getMac(), currentDevice.getType());
        });
    }

    public void addDevice(Device device) {
        if (!devices.contains(device)) {
            devices.add(device);
            notifyItemInserted(devices.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button deviceId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceId = itemView.findViewById(R.id.deviceID);
        }

        void fillView(String s) {
            deviceId.setText(s);
        }
    }
}
