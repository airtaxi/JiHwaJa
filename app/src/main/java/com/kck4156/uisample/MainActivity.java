package com.kck4156.uisample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "JiHwaJa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindMyo.mMyo = null;
    }

    public void onClick(View view) {
        if(FindMyo.mMyo != null) {
            Intent intent = new Intent(this, MyoPrint.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "연결된 MYO가 없습니다. 상단의 버튼을 클릭하여 MYO에 연결해주세요", Toast.LENGTH_LONG).show();
        }
    }

    public void onBTButtonClick(View view) {
        Intent intent = new Intent(this, FindMyo.class);
        startActivity(intent);
    }
}
