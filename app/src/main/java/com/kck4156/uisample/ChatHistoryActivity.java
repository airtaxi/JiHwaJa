package com.kck4156.uisample;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.kck4156.uisample.viewdata.ChatLog;

import java.util.List;
import java.util.Random;

public class ChatHistoryActivity extends AppCompatActivity {

    public static List<ChatLog> logs;
    public static int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        fontSize = pref.getInt("fontSize", 24);
        for(ChatLog log : logs) {;
            if(log.isMine()) {
                fragmentTransaction.add(R.id.layout_history, ChatMeFragment.newInstance(log.getMessage()));
            } else {
                fragmentTransaction.add(R.id.layout_history, ChatOtherFragment.newInstance(log.getMessage()));
            }
        }
        fragmentTransaction.commit();

    }
}
