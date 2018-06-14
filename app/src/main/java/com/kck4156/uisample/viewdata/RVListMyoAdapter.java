package com.kck4156.uisample.viewdata;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kck4156.uisample.FindMyo;
import com.kck4156.uisample.MainActivity;
import com.kck4156.uisample.R;

import java.util.ArrayList;

import eu.darken.myolib.BaseMyo;
import eu.darken.myolib.Myo;
import eu.darken.myolib.MyoCmds;

public class RVListMyoAdapter extends RecyclerView.Adapter<RVListMyoAdapter.ViewHolder> implements BaseMyo.ConnectionListener  {
    private  ArrayList<RVListMyo> mItems;

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
                FindMyo.mMyo = mItems.get(position).getMyo();
                FindMyo.mMyo.addConnectionListener(RVListMyoAdapter.this);
                FindMyo.mMyo.connect();
                FindMyo.mMyo.setConnectionSpeed(BaseMyo.ConnectionSpeed.HIGH);
                FindMyo.mMyo.writeSleepMode(MyoCmds.SleepMode.NEVER, null);
                FindMyo.mMyo.writeMode(MyoCmds.EmgMode.FILTERED, MyoCmds.ImuMode.RAW, MyoCmds.ClassifierMode.DISABLED, null);
                FindMyo.mMyo.writeUnlock(MyoCmds.UnlockType.HOLD, null);
                Toast.makeText(FindMyo.mActivity.getApplicationContext(), "MYO 연결됨", Toast.LENGTH_LONG).show();
                FindMyo.mActivity.finish();
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

    @Override
    public void onConnectionStateChanged(BaseMyo myo, BaseMyo.ConnectionState state) {
        if(state == BaseMyo.ConnectionState.DISCONNECTED) {
            Log.d(MainActivity.TAG, "Disconnected!");
            FindMyo.mMyo = null;
        }
    }

    static class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mUdid;
        ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tvMyoName);
            mUdid = itemView.findViewById(R.id.tvUdid);
        }
    }
}
