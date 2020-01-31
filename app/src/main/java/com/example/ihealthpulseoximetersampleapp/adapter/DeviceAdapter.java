package com.example.ihealthpulseoximetersampleapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihealthpulseoximetersampleapp.ConnectActivity;
import com.example.ihealthpulseoximetersampleapp.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<String> devices = new ArrayList<>();
    ConnectActivity connectActivity;

    public DeviceAdapter(ConnectActivity ca){
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
        holder.fillView(devices.get(position));
        holder.deviceId.setOnClickListener(v -> connectActivity.connect());
    }

    public void addDevice(String o){
        devices.add(o);
        notifyItemInserted(devices.size()-1);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceId = itemView.findViewById(R.id.deviceID);
        }

        void fillView(String s) {
            deviceId.setText(s);
        }
    }
}
