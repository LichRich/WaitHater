package com.mally.ch02.waitHater.ui.BusStopInfo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.utils.ListItem;

import java.util.ArrayList;

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.ViewHolder> {

    private final ArrayList<ListItem> stations;
    private final StationListAdapter.OnItemClickListener onItemClickListener;

    public StationListAdapter(ArrayList<ListItem> routes_list, StationListAdapter.OnItemClickListener itemClick){
        stations = routes_list;
        onItemClickListener = itemClick;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_stop, parent, false);
        return new StationListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String station_name = stations.get(position).getName();
        holder.tv_stationName.setText(station_name);

        if(stations.get(position).isBusIsHere()) {
            holder.tv_stationName.setBackgroundColor(Color.rgb(178, 204, 255));
        } else if(!stations.get(position).isBusIsHere()) {
            holder.tv_stationName.setBackgroundColor(Color.rgb(255, 238, 212));
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_stationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_stationName = itemView.findViewById(R.id.tv_stationName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
