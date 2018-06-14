package com.kck4156.uisample.viewdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kck4156.uisample.ChatHistoryActivity;
import com.kck4156.uisample.ChatListActivity;
import com.kck4156.uisample.R;

import java.util.ArrayList;
import java.util.List;

public class RVListHistoryAdapter extends RecyclerView.Adapter<RVListHistoryAdapter.ViewHolder> {
    ArrayList<RVListHistory> mItems;

    public  RVListHistoryAdapter(ArrayList<RVListHistory> items) {
        mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatlist, parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ChatListActivity.recyclerView.getChildLayoutPosition(v);
                Toast.makeText(ChatListActivity.recyclerView.getContext(), "position = "+String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mName.setText(mItems.get(position).getTitle());
        holder.mTimeStamp.setText(String.valueOf(mItems.get(position).getLog().size())+"개의 대화가 있습니다.");
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChatLog> logs = mItems.get(position).getLog();
                ChatHistoryActivity.logs = logs;
                Intent intent = new Intent(holder.cv.getContext(), ChatHistoryActivity.class);
                holder.cv.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mTimeStamp;
        private CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tvTitle);
            mTimeStamp = itemView.findViewById(R.id.tvDesc);
            cv = itemView.findViewById(R.id.cardView);
        }
    }
}
