package com.kck4156.uisample;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kck4156.uisample.viewdata.ChatLog;
import com.kck4156.uisample.viewdata.ChatLogs;
import com.kck4156.uisample.viewdata.RVListHistory;
import com.kck4156.uisample.viewdata.RVListHistoryAdapter;
import com.kck4156.uisample.viewdata.RVListMyo;
import com.kck4156.uisample.viewdata.RVListMyoAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

        SharedPreferences pref = getSharedPreferences("logs2", MODE_PRIVATE);
        Gson gson = new Gson();
        String text =pref.getString("data", "");
        List<ChatLogs> totalLog;
        if(text.isEmpty())
            totalLog = new ArrayList<>();
        else
            totalLog = gson.fromJson(text,  new TypeToken<List<ChatLogs>>(){}.getType());

        for (ChatLogs logs : totalLog) {
            long time = logs.getEpochTime();
            Date date = new Date(time);
            mItems.add(new RVListHistory((date.getYear()+1900)+"년 "+(date.getMonth() + 1)+"월 "+date.getDate()+"일의 대화 내용", logs.getLogs()));
        }

        adapter.notifyDataSetChanged();
    }
}
