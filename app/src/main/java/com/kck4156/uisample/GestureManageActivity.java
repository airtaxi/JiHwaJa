package com.kck4156.uisample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kck4156.uisample.viewdata.RVListGesture;
import com.kck4156.uisample.viewdata.RVListGestureAdapter;
import com.kck4156.uisample.viewdata.RVListMyo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.darken.myolib.Myo;
import eu.darken.myolib.MyoCmds;
import eu.darken.myolib.msgs.MyoMsg;
import eu.darken.myolib.processor.classifier.ClassifierEvent;
import eu.darken.myolib.processor.classifier.ClassifierProcessor;
import eu.darken.myolib.processor.emg.EmgData;
import eu.darken.myolib.processor.emg.EmgProcessor;
import eu.darken.myolib.processor.imu.ImuData;
import eu.darken.myolib.processor.imu.ImuProcessor;
import eu.darken.myolib.processor.imu.MotionEvent;
import eu.darken.myolib.processor.imu.MotionProcessor;

import static com.kck4156.uisample.FindMyo.mMyo;

public class GestureManageActivity extends AppCompatActivity implements EmgProcessor.EmgDataListener,
        ImuProcessor.ImuDataListener,
        ClassifierProcessor.ClassifierEventListener,
        MotionProcessor.MotionEventListener,
        Myo.ReadDeviceNameCallback,
        Myo.BatteryCallback,
        Myo.FirmwareCallback{

    public static RecyclerView recyclerView;
    public static ArrayList<RVListGesture> mItems;
    public static RVListGestureAdapter adapter;
    public static Activity mActivity;
    public static GestureManageActivity mThis;
    private double[] currentORI;
    private ArrayList<Double[]> totalEmgs;
    private EmgProcessor mEmgProcessor;
    private ImuProcessor mImuProcessor;
    private Double[] finalEmg;
    private TextView status;
    private ClassifierProcessor mClassifierProcessor;
    private MotionProcessor mMotionProcessor;
    private EditText maxSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_manage);
        mActivity = this;
        mThis = this;

        recyclerView = findViewById(R.id.recycler);
        mItems = loadList();
        adapter = new RVListGestureAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void saveList(List<RVListGesture> list) {
        SharedPreferences pref = getSharedPreferences("datajson", MODE_PRIVATE);
        Gson gson = new Gson();
        pref.edit().putString("data", gson.toJson(list)).apply();
    }

    public ArrayList<RVListGesture> loadList() {
        SharedPreferences pref = getSharedPreferences("datajson", MODE_PRIVATE);
        Gson gson = new Gson();
        String text =pref.getString("data", "");
        if(text.isEmpty())
            return new ArrayList<RVListGesture>();
        else
            return gson.fromJson(text.toString(),  new TypeToken<ArrayList<RVListGesture>>(){}.getType());
    }

    public void onGestureAddButtonClick(View view) {
        mMyo.writeVibrate(MyoCmds.VibrateType.SHORT, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        totalEmgs = new ArrayList<>();
        finalEmg = new Double[8];
        View dialogView = inflater.inflate(R.layout.dialog_gesture, null);
        Button addGesture = dialogView.findViewById(R.id.button4);
        final CheckBox useGyroscope = dialogView.findViewById(R.id.checkBox);
        final EditText jamoum = dialogView.findViewById(R.id.editText);
        maxSize = dialogView.findViewById(R.id.editText2);
        status = dialogView.findViewById(R.id.textView11);
        addGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMyo.
                mMyo.writeVibrate(MyoCmds.VibrateType.SHORT, null);
                status.setText("제스쳐 등록중...(0/"+String.valueOf(maxSize.getText().toString())+")");
                Log.d(MainActivity.TAG, "EMG DATA ZERO");
                mMyo.readDeviceName(GestureManageActivity.this);
                mMyo.readBatteryLevel(GestureManageActivity.this);
                mMyo.readFirmware(GestureManageActivity.this);
                mMyo.writeMode(MyoCmds.EmgMode.FILTERED, MyoCmds.ImuMode.ALL, MyoCmds.ClassifierMode.DISABLED, new Myo.MyoCommandCallback() {
                    @Override
                    public void onCommandDone(Myo myo, MyoMsg msg) {
                                Log.d("MYOEMG", msg.toString());
                    }
                });
                mEmgProcessor = new EmgProcessor();
                mEmgProcessor.addListener(GestureManageActivity.this);
                mMyo.addProcessor(mEmgProcessor);
                mImuProcessor = new ImuProcessor();
                mImuProcessor.addListener(GestureManageActivity.this);
                mMyo.addProcessor(mImuProcessor);
                Log.d(MainActivity.TAG, "RUN : "+mMyo.isRunning());
            }
        });
        builder.setTitle("MYO 제스쳐 입력");
        builder.setCancelable(false);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mMyo.writeVibrate(MyoCmds.VibrateType.SHORT, null);
                        if(totalEmgs.size() >= Integer.valueOf(maxSize.getText().toString())) {
                            mItems.add(new RVListGesture(jamoum.getText().toString(), useGyroscope.isChecked(), finalEmg, currentORI));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    saveList(mItems);
                                    Toast.makeText(GestureManageActivity.this, "제스쳐 추가됨 : " + mItems.size(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(GestureManageActivity.this, "제스쳐 추가중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @Override
    public void onNewEmgData(EmgData emgData) {
        Log.d(MainActivity.TAG, "EMG DATA");
        String[] datas = emgData.toString().split(" ");
        Double[] current = new Double[8];
        for(int i = 0; i < datas.length; i++) {
            current[i] = Math.pow(Double.valueOf(datas[i]), 2);
        }
        totalEmgs.add(current);
        if(totalEmgs.size()%50 == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("제스쳐 등록중...(" + String.valueOf(totalEmgs.size()) + "/"+maxSize.getText().toString()+")");
                }
            });
        }
        if(totalEmgs.size() > Integer.valueOf(maxSize.getText().toString())-2) {
            int totalCount = totalEmgs.size();
            Double[] currDbl2 = new Double[8];
            for(int ik = 0; ik < 8; ik++) {
                currDbl2[ik] =  0.0;
            }
            for(int ik = 0; ik < totalEmgs.size(); ik++) {
                Double[] currDbl = totalEmgs.get(ik);
                for(int ij = 0; ij < 8; ij++) {
                    currDbl2[ij] = currDbl2[ij] + currDbl[ij];
                }
            }
            for(int ik = 0; ik < 8; ik++) {
                Double varNow = currDbl2[ik] / totalCount;
                finalEmg[ik] = varNow;
            }
            mEmgProcessor.removeDataListener(this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("제스쳐 등록완료 (" + String.valueOf(totalEmgs.size()) + "/"+maxSize.getText().toString()+")");
                }
            });
        }
    }

    @Override
    public void onNewImuData(ImuData imuData) {
        Log.d(MainActivity.TAG, "EMG DATA IMU");
        if(totalEmgs.size() >= Integer.valueOf(maxSize.getText().toString())) {
            mImuProcessor.removeDataListener(this);
        }
        currentORI = imuData.getOrientationData();
    }

    @Override
    public void onClassifierEvent(ClassifierEvent classifierEvent) {
        Log.d(MainActivity.TAG, "Classifier");
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        Log.d(MainActivity.TAG, "MOTION");
    }

    @Override
    public void onDeviceNameRead(Myo myo, MyoMsg msg, String deviceName) {
        Log.d(MainActivity.TAG, "NAME:"+deviceName);
    }

    @Override
    public void onFirmwareRead(Myo myo, MyoMsg msg, String version) {
        Log.d(MainActivity.TAG, "FMR:"+version);
    }

    @Override
    public void onBatteryLevelRead(Myo myo, MyoMsg msg, int batteryLevel) {
        Log.d(MainActivity.TAG, "BTR:"+batteryLevel);
    }
}
