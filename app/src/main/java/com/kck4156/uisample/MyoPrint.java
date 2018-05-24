package com.kck4156.uisample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyoPrint extends AppCompatActivity {
    boolean isBack = false;
    Timer backTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myo_print);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @Override
    public void onBackPressed() {
        if(backTimer != null) {
            backTimer.cancel();
        }
        if(isBack) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "프로그램을 종료하려면\n뒤로가기키를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
            isBack = true;
            backTimer = new Timer();
            backTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isBack = false;
                    backTimer = null;
                }
            }, 1000);
        }
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void onTranslateBtnClick(View view) {
        Toast.makeText(this,"지화 번역 클릭됨", Toast.LENGTH_SHORT).show();
    }

    public void onSpeechRecognizationClick(View view) {
        Toast.makeText(this,"음성 인식 클릭됨", Toast.LENGTH_SHORT).show();
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void onHistoryClick(View view) {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
    }
}
