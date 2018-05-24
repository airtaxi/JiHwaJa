package com.kck4156.uisample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kck4156.uisample.viewdata.RVListMyo;
import com.kck4156.uisample.viewdata.RVListMyoAdapter;

import java.util.ArrayList;

public class FindMyo extends AppCompatActivity {

    public static RecyclerView recyclerView;
    private ArrayList<RVListMyo> mItems;
    private RVListMyoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_myo);
        recyclerView = findViewById(R.id.list);
        mItems = new ArrayList<RVListMyo>();
        adapter = new RVListMyoAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItems.add(new RVListMyo("<Place any dummy MYO name here>(0)", "<Place any dummy MYO UDID here>"));

        adapter.notifyDataSetChanged();
    }

    public void onScanButtonClick(View view) {
        mItems.add(new RVListMyo("<Place any dummy MYO name here>("+String.valueOf(mItems.size())+")","<Place any dummy MYO UDID here>"));
        adapter.notifyDataSetChanged();
    }
}