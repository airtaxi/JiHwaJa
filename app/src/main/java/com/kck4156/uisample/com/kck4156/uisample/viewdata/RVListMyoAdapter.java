package com.kck4156.uisample.com.kck4156.uisample.viewdata;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kck4156.uisample.FindMyo;
import com.kck4156.uisample.R;

import java.util.ArrayList;

public class RVListMyoAdapter extends RecyclerView.Adapter<RVListMyoAdapter.ViewHolder> {
    ArrayList<RVListMyo> mItems;

    public  RVListMyoAdapter(ArrayList<RVListMyo> items) {
        mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myolist, parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = FindMyo.recyclerView.getChildLayoutPosition(v);
                Toast.makeText(FindMyo.recyclerView.getContext(), "position = "+String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mItems.get(position).getName());
        holder.mUdid.setText(mItems.get(position).getUdid());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mUdid;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tvMyoName);
            mUdid = itemView.findViewById(R.id.tvUdid);
        }
    }
}
