package com.kck4156.uisample;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.kck4156.uisample.viewdata.ChatLog;
import com.kck4156.uisample.viewdata.RVListHistory;
import com.kck4156.uisample.viewdata.RVListHistoryAdapter;
import com.kck4156.uisample.viewdata.RVListMyo;
import com.kck4156.uisample.viewdata.RVListMyoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatListActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerView;
    private ArrayList<RVListHistory> mItems;
    private RVListHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView = findViewById(R.id.list);
        mItems = new ArrayList<>();
        adapter = new RVListHistoryAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            List<ChatLog> logs = new ArrayList<>();
            int min = (int) (rand.nextFloat()*15.0f);
            int max = 15 + (int) (rand.nextFloat()*15.0f);
            for (int j = 0; j < max-min; j++) {
                float var = rand.nextFloat() * 2.0f;
                StringBuilder builder = new StringBuilder();
                String baseStr = "소불고기 먹고싶다. ";
                for(int k = 0; k <= j; k++) {
                    builder.append(baseStr);
                }
                ChatLog log = new ChatLog(builder.toString(), var < 1.0f);
                logs.add(log);
            }
            mItems.add(new RVListHistory("2018년 5월 25일의 대화 내용", logs));
        }

        adapter.notifyDataSetChanged();
    }
}
