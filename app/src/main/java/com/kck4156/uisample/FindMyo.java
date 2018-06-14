package com.kck4156.uisample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kck4156.uisample.viewdata.RVListMyo;
import com.kck4156.uisample.viewdata.RVListMyoAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.darken.myolib.BaseMyo;
import eu.darken.myolib.Myo;
import eu.darken.myolib.MyoConnector;
import eu.darken.myolib.msgs.MyoMsg;

public class FindMyo extends AppCompatActivity{

    public static Myo mMyo;
    public static Activity mActivity;

    public static RecyclerView recyclerView;
    private ArrayList<RVListMyo> mItems;
    private RVListMyoAdapter adapter;
    private boolean isScanable = true;
    private MyoConnector mMyoConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_myo);
        recyclerView = findViewById(R.id.list);
        mItems = new ArrayList<RVListMyo>();
        adapter = new RVListMyoAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMyoConnector = new MyoConnector(this);
        mMyoConnector.scan(mScannerCallback, 1000);
        mActivity = this;
    }

    private MyoConnector.ScannerCallback mScannerCallback = new MyoConnector.ScannerCallback() {
        @Override
        public void onScanFinished(final List<Myo> myos) {
            if(isScanable) {
                mItems.clear();
                Log.d(MainActivity.TAG, "MYOS:"+myos.size());
                for(Myo myo : myos) {
                    final RVListMyo node = new RVListMyo("Myo", myo.getDeviceAddress(), myo);
                    mItems.add(node);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                mMyoConnector.scan(mScannerCallback, 1000);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isScanable = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isScanable = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isScanable = false;
    }
}