package com.mally.ch02.waitHater.ui.BusInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mally.ch02.waitHater.R;
import com.mally.ch02.waitHater.ui.utils.ListItem;

import java.util.ArrayList;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.ViewHolder>{

    private final ArrayList<ListItem> lists;
    private final OnItemClickListener onItemClickListener;

    RoutesListAdapter(ArrayList<ListItem> routes_list, OnItemClickListener itemClick){
        lists = routes_list;
        onItemClickListener = itemClick;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView bus_num, bus_dir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_num = (TextView) itemView.findViewById(R.id.tv_busItem_num);
            bus_dir = (TextView) itemView.findViewById(R.id.tv_busItem_dir);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesListAdapter.ViewHolder holder, int position) {
        String num = lists.get(position).getNum();
        String dir = lists.get(position).getName();

        holder.bus_num.setText(num);
        holder.bus_dir.setText(dir);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
