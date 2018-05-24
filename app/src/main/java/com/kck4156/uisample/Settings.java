package com.kck4156.uisample;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView tvFontPreview;
    private TextView tvFontSize;
    private int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tvFontSize = findViewById(R.id.textView7);
        tvFontPreview = findViewById(R.id.textView10);
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        fontSize = pref.getInt("fontSize", 16);
        tvFontSize.setText(String.valueOf(fontSize));
        tvFontPreview.setTextSize((float) fontSize);
    }

    public void onApply(View view) {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        pref.edit().putInt("fontSize", fontSize).commit();
        finish();
    }

    public void validate() {
        if(fontSize > 48) {
            fontSize = 48;
        } else if(fontSize < 12) {
            fontSize = 12;
        }
        tvFontPreview.setTextSize((float) fontSize);
        tvFontSize.setText(String.valueOf(fontSize));
    }

    public void onValueUp(View view) {
        fontSize += 2;
        validate();
    }

    public void onValueDown(View view) {
        fontSize -= 2;
        validate();
    }
}
