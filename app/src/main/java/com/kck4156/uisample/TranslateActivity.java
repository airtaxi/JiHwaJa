package com.kck4156.uisample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kck4156.uisample.viewdata.ChatLog;
import com.kck4156.uisample.viewdata.ChatLogs;
import com.kck4156.uisample.viewdata.RVListGesture;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import eu.darken.myolib.Myo;
import eu.darken.myolib.MyoCmds;
import eu.darken.myolib.msgs.MyoMsg;
import eu.darken.myolib.processor.emg.EmgData;
import eu.darken.myolib.processor.emg.EmgProcessor;
import eu.darken.myolib.processor.imu.ImuData;
import eu.darken.myolib.processor.imu.ImuProcessor;

import static com.kck4156.uisample.FindMyo.mMyo;

public class TranslateActivity extends AppCompatActivity implements EmgProcessor.EmgDataListener, ImuProcessor.ImuDataListener, View.OnTouchListener, RecognitionListener, TextToSpeech.OnInitListener {
    Handler handler;
    private boolean isRunning;
    private List<ChatLog> logs;
    private List<String> jaums = Arrays.asList("ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ");
    private List<String> moums = Arrays.asList("ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ");
    private List<String> chosungs = Arrays.asList("ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ");
    private List<String> jungsungs = Arrays.asList("ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ");
    private List<String[]> ssangsungs = Arrays.asList(
            new String[] {"ㅂ", "ㅃ"},
            new String[] {"ㅈ", "ㅉ"},
            new String[] {"ㄷ", "ㄸ"},
            new String[] {"ㄱ", "ㄲ"},
            new String[] {"ㅅ", "ㅆ"});
    private List<String[]> ssangsungs2 = Arrays.asList(
            new String[]{"ㄱ", "ㅅ", "ㄳ"},
            new String[]{"ㄴ", "ㅈ", "ㄵ"},
            new String[]{"ㄴ", "ㅎ", "ㄶ"} ,
            new String[]{"ㄹ", "ㄱ", "ㄺ"},
            new String[]{"ㄹ", "ㅂ", "ㄼ"},
            new String[]{"ㄱ", "ㅅ", "ㄽ"},
            new String[]{"ㄹ", "ㅌ", "ㄾ"},
            new String[]{"ㄹ", "ㅎ", "ㅀ"},
            new String[]{"ㅂ", "ㅅ", "ㅄ"},
            new String[]{"ㅅ", "ㅅ", "ㅆ"},
            new String[]{"ㄱ", "ㄱ", "ㄲ"});
    private List<String> jongsungs = Arrays.asList("ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ");
    private int currentState = 1;
    private String currentStr = null;
    private String currentEtStr = "";
    private TextView tvCurrent;
    private ToggleButton toggleButton;
    private EditText etNow;
    private List<String> currentList = new ArrayList<>();
    private EmgProcessor mEmgProcessor;
    private ImuProcessor mImuProcessor;
    private ArrayList<RVListGesture> gestures;
    private List<Double[]> dblNow = new ArrayList<>();
    private double[] mCurrentORI;
    private LuaValue merge;
    private LuaValue removeLast;
    private String lastMoum;
    private LuaValue removeWord;
    private boolean isActivated = false;
    private ImageButton btRecord;
    private SpeechRecognizer mRecognizer;
    private String lastWord;
    private TextToSpeech myTTS;

    public ArrayList<RVListGesture> loadList() {
        SharedPreferences pref = getSharedPreferences("datajson", MODE_PRIVATE);
        Gson gson = new Gson();
        String text =pref.getString("data", "");
        if(text.isEmpty())
            return new ArrayList<RVListGesture>();
        else
            return gson.fromJson(text.toString(),  new TypeToken<ArrayList<RVListGesture>>(){}.getType());
    }

    private void addMessage(String message, boolean isMe) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isMe) {
            fragmentTransaction.add(R.id.list, ChatMeFragment.newInstance(message));
        } else {
            fragmentTransaction.add(R.id.list, ChatOtherFragment.newInstance(message));
        }
        fragmentTransaction.commit();

        fragmentTransaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final ScrollView scrollView = findViewById(R.id.scroll);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        logs.add(new ChatLog(message, isMe));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTTS = new TextToSpeech(this, this);

        setContentView(R.layout.activity_translate);
        isRunning = true;
        logs = new ArrayList<>();
        handler = new Handler();
        tvCurrent = findViewById(R.id.tvCurrent);
        toggleButton = findViewById(R.id.button5);
        btRecord = findViewById(R.id.btRecord);
        btRecord.setOnTouchListener(this);


        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        int fontSize = pref.getInt("fontSize", 24);
        tvCurrent.setTextSize(fontSize);

        etNow = findViewById(R.id.etDeaf);

        etNow.setCursorVisible(false);
        ((EditText) findViewById(R.id.etEnter)).setCursorVisible(false);

        tvCurrent.setText("로딩중...");

        Globals globals = JsePlatform.standardGlobals();
        globals.loadfile("assets/main.lua").call();
        merge = globals.get("merge");
        removeLast = globals.get("removeLast");
        removeWord =globals.get("removeWord");

        mMyo.connect();
        mMyo.writeMode(MyoCmds.EmgMode.FILTERED, MyoCmds.ImuMode.ALL, MyoCmds.ClassifierMode.DISABLED, new Myo.MyoCommandCallback() {
            @Override
            public void onCommandDone(Myo myo, MyoMsg msg) {
                Log.d("MYOEMG", msg.toString());
            }
        });
        mMyo.writeMode(MyoCmds.EmgMode.FILTERED, MyoCmds.ImuMode.ALL, MyoCmds.ClassifierMode.DISABLED, new Myo.MyoCommandCallback() {
            @Override
            public void onCommandDone(Myo myo, MyoMsg msg) {
                Log.d("MYOEMG", msg.toString());
            }
        });
        mEmgProcessor = new EmgProcessor();
        mEmgProcessor.addListener(this);
        mMyo.addProcessor(mEmgProcessor);
        mImuProcessor = new ImuProcessor();
        mImuProcessor.addListener(this);
        mMyo.addProcessor(mImuProcessor);

        gestures = loadList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(logs.size() > 0) {
            SharedPreferences pref = getSharedPreferences("logs2", MODE_PRIVATE);
            Gson gson = new Gson();
            String text =pref.getString("data", "");
            List<ChatLogs> totalLog;
            if(text.isEmpty())
                totalLog = new ArrayList<>();
            else
                totalLog = gson.fromJson(text.toString(),  new TypeToken<List<ChatLogs>>(){}.getType());
            totalLog.add(new ChatLogs(System.currentTimeMillis(), logs));
            pref.edit().putString("data", gson.toJson(totalLog)).apply();
        }

        myTTS.shutdown();

        mEmgProcessor.removeDataListener(this);
        mImuProcessor.removeDataListener(this);
        isRunning = false;
    }

    public void onDeafSend(View view) {
        String msg = etNow.getText().toString();
        if(msg.length() > 0) {
            addMessage(msg, true);
            etNow.setText("");
            currentEtStr = "";
            currentStr = null;
            currentList.clear();
            currentState = 1;

            myTTS.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @SuppressLint("CutPasteId")
    public void onOtherSend(View view) {
        String msg = ((EditText) findViewById(R.id.etEnter)).getText().toString();
        if(msg.length() > 0) {
            addMessage(msg, false);
            EditText etNow = findViewById(R.id.etEnter);
            etNow.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void onDeafInput(View view) {
        if(!isActivated) {
            return;
        }
        String currentVal = tvCurrent.getText().toString();
        if (currentState == 1) {
            if (chosungs.contains(currentVal)) {
                currentList.add(currentVal);
                currentStr = currentVal;
                currentState = 2;
            }
        } else if (currentState == 2) {
            if (jungsungs.contains(currentVal)) {
                currentList.add(currentVal);
                currentStr = merge.call(LuaValue.valueOf(currentStr), LuaValue.valueOf(currentVal)).tojstring();
                lastMoum = currentVal;
                currentState = 3;
            } else if(currentStr.equals(currentVal)){
                for(String[] ssang : ssangsungs) {
                    if(ssang[0].equals(currentStr) && ssang[0].equals(currentVal)) {
                        currentStr = ssang[1];
                        currentList.set(0, currentStr);
                    }
                }
            }
        } else if (currentState == 3) {
            if (jongsungs.contains(currentVal)) {
                currentList.add(currentVal);

                currentStr = merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentList.get(1)), LuaValue.valueOf(currentList.get(2))).tojstring();
                currentState = 4;
            } else if (chosungs.contains(currentVal)) {
                currentList.add(currentVal);
                currentEtStr = currentEtStr + merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentList.get(1))).tojstring();

                currentStr = currentVal;
                currentState = 2;

                currentList.clear();
                currentList.add(currentStr);
            } else {
                if(lastMoum.equals("ㅗ") && currentVal.equals("ㅏ")) {
                    currentVal = "ㅘ";
                } else if(lastMoum.equals("ㅜ") && currentVal.equals("ㅓ")) {
                    currentVal = "ㅝ";
                } else if(lastMoum.equals("ㅗ") && currentVal.equals("ㅐ")) {
                    currentVal = "ㅙ";
                } else if(lastMoum.equals("ㅜ") && currentVal.equals("ㅔ")) {
                    currentVal = "ㅞ";
                } else if(lastMoum.equals("ㅜ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅟ";
                } else if(lastMoum.equals("ㅗ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅚ";
                } else if(lastMoum.equals("ㅡ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅢ";
                } else if(lastMoum.equals("ㅓ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅔ";
                } else if(lastMoum.equals("ㅕ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅖ";
                } else if(lastMoum.equals("ㅏ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅐ";
                } else if(lastMoum.equals("ㅑ") && currentVal.equals("ㅣ")) {
                    currentVal = "ㅒ";
                }
                lastMoum = currentVal;
                currentList.set(currentList.size()-1, currentVal);
                currentStr = merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentVal)).tojstring();
            }
        } else if (currentState == 4) {
            if(!jungsungs.contains(currentVal)) {
                for(String[] ssang : ssangsungs2) {
                    Log.d("DOGE", lastWord+"/"+currentVal);
                    if(ssang[0].equals(lastWord) && ssang[1].equals(currentVal)) {
                        currentVal = ssang[2];
                        currentEtStr = currentEtStr + merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentList.get(1)), LuaValue.valueOf(currentVal)).tojstring();
                        currentState = 1;
                        currentStr = null;
                        currentList.clear();
                        etNow.setText(currentEtStr);
                        return;
                    }
                }
            }
            if (jungsungs.contains(currentVal)) {
                currentList.add(currentVal);
                currentEtStr = currentEtStr + merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentList.get(1))).tojstring();

                String j1 = currentList.get(2), j2 = currentList.get(3);
                currentStr = merge.call(LuaValue.valueOf(j1), LuaValue.valueOf(j2)).tojstring();
                lastMoum = currentVal;
                currentState = 3;
                currentList.clear();
                currentList.add(j1);
                currentList.add(j2);
            } else if (chosungs.contains(currentVal)) {
                currentList.add(currentVal);
                currentEtStr = currentEtStr + merge.call(LuaValue.valueOf(currentList.get(0)), LuaValue.valueOf(currentList.get(1)), LuaValue.valueOf(currentList.get(2))).tojstring();

                currentStr = currentVal;
                currentState = 2;

                currentList.clear();
                currentList.add(currentStr);
            }
        }
        lastWord = currentVal;
        if(currentStr != null) {
            etNow.setText(currentEtStr + currentStr);
        }
    }

    private RVListGesture getMinKey(HashMap<RVListGesture, Double> map) {
        RVListGesture minKey = null;
        Double minValue = Double.MAX_VALUE;
        for(RVListGesture key : map.keySet()) {
            Double value = Math.sqrt(map.get(key));
            if(value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
        return minKey;
    }

     private Long lastTime = null;

    @Override
    public void onNewEmgData(EmgData emgData) {
        if(lastTime == null) {lastTime = System.currentTimeMillis();}
        if ((System.currentTimeMillis() - lastTime) <= 2000) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleButton.setChecked(true);
                }
            });
            String[] datas = emgData.toString().split(" ");
            Double[] currDbl = new Double[8];
            for (int i = 0; i < datas.length; i++) {
                currDbl[i] = Math.pow(Double.valueOf(datas[i]), 2);
            }
            dblNow.add(currDbl);

            if (dblNow.size() > 50) {
                FindMyo.mMyo.writeVibrate(MyoCmds.VibrateType.SHORT, null);

                HashMap<RVListGesture, Double> diffs = new HashMap<>();
                for (RVListGesture gesture : gestures) {
                    Double[] dblCmpNow = gesture.emgData;
                    double[] nowORI = gesture.origData;
                    Double[] dblCurr = new Double[8];
                    for (int ik = 0; ik < 8; ik++) {
                        dblCurr[ik] = 0.0;
                    }
                    for (int ik = 0; ik < dblNow.size(); ik++) {
                        Double[] now = dblNow.get(ik);
                        for (int ij = 0; ij < 8; ij++) {
                            dblCurr[ij] = dblCurr[ij] + now[ij];
                        }
                    }
                    for (int ik = 0; ik < 8; ik++) {
                        dblCurr[ik] = dblCurr[ik] / dblNow.size();
                    }
                    Double diffTotal = 0.0;
                    for (int ik = 0; ik < 8; ik++) {
                        Double target = dblCmpNow[ik];
                        Double curr = dblCurr[ik];
                        Double diff = Math.sqrt(Math.abs(curr - target));
                        diffTotal += Math.pow(diff, 2);
                        if (gesture.isOrig) {
                            diffTotal += Math.abs(nowORI[0] * 10 - mCurrentORI[0] * 10);
                            diffTotal += Math.abs(nowORI[1] * 10 - mCurrentORI[1] * 10);
                            diffTotal += Math.abs(nowORI[2] * 10 - mCurrentORI[2] * 10);
                            diffTotal += Math.abs(nowORI[3] * 10 - mCurrentORI[3] * 10);
                        }
                    }
                    diffs.put(gesture, diffTotal);
                }
                final RVListGesture nearest = getMinKey(diffs);
                tvCurrent.post(new Runnable() {
                    @Override
                    public void run() {
                        tvCurrent.setText(nearest.name);
                        isActivated = true;
                    }
                });
                dblNow.clear();
            }
        } else if((System.currentTimeMillis() - lastTime) > 3000) {
            lastTime = System.currentTimeMillis();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleButton.setChecked(false);
                }
            });
        }
    }

    @Override
    public void onNewImuData(ImuData imuData) {
        mCurrentORI = imuData.getOrientationData();
    }

    public void onRemoveClicked(View view) {
        if(currentState == 3 || currentState == 4) {
            currentStr = removeLast.call(LuaValue.valueOf(currentStr)).tojstring();
            currentState--;
            currentList.remove(currentList.size()-1);
        } else if(currentState == 2) {
            currentStr = null;
            currentState = 1;
            currentList.clear();
        } else if(currentState == 1) {
            currentEtStr = removeWord.call(LuaValue.valueOf(currentEtStr)).tojstring();
        }

        if(currentStr != null) {
            etNow.setText(currentEtStr + currentStr);
        } else {
            etNow.setText(currentEtStr);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == btRecord) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                mRecognizer.setRecognitionListener(this);
                mRecognizer.startListening(intent);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                mRecognizer.stopListening();
            }
        }
        return false;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        String key = "";
        key = SpeechRecognizer.RESULTS_RECOGNITION;
        ArrayList<String> mResult = results.getStringArrayList(key);
        String[] rs = new String[mResult.size()];
        mResult.toArray(rs);
        ((EditText) findViewById(R.id.etEnter)).setText(rs[0]);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    public void onInit(int status) {

    }

    public void onSpaceBar(View view) {
        currentEtStr = currentEtStr + currentStr + " ";
        currentStr = null;
        currentState = 1;
        currentList.clear();
    }
}
