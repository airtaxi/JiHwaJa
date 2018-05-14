package com.kck4156.uisample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MyoPrint.class);
        startActivity(intent);
        finish();
    }

    public void onBTButtonClick(View view) {
        Intent intent = new Intent(this, FindMyo.class);
        startActivity(intent);
    }
}
