package com.kck4156.uisample.viewdata;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kck4156.uisample.GestureManageActivity;
import com.kck4156.uisample.R;

import java.util.ArrayList;;

public class RVListGestureAdapter extends RecyclerView.Adapter<RVListGestureAdapter.ViewHolder> {
    private ArrayList<RVListGesture> mItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mJamoum;
        private TextView mGyro;
        public ViewHolder(View itemView) {
            super(itemView);
            mJamoum = itemView.findViewById(R.id.tvJamoum);
            mGyro = itemView.findViewById(R.id.tvGyroscope);
        }
    }

    public RVListGestureAdapter(ArrayList<RVListGesture> items) {
        mItems = items;
    }

    @Override
    public RVListGestureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gesture, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = GestureManageActivity.recyclerView.getChildLayoutPosition(v);
                GestureManageActivity.mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GestureManageActivity.mActivity);
                        builder.setMessage("정말로 이 제스쳐를 삭제하시겠습니까?")
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        GestureManageActivity.mItems.remove(position);
                                        GestureManageActivity.mThis.saveList(GestureManageActivity.mItems);
                                        GestureManageActivity.adapter.notifyDataSetChanged();
                                    }
                                });
                        builder.show();
                    }
                });
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mJamoum.setText("등록된 자모음 : " + mItems.get(position).name);
        holder.mGyro.setText("자이로스코프 사용여부 : " + (mItems.get(position).isOrig ? "사용" : "사용 안함"));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
