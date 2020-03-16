package com.example.bottle_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmWakeupScreen extends AppCompatActivity implements View.OnClickListener{

    class MediaCheckerTask extends TimerTask{

        @Override
        public void run() {
            if(AlarmReceiver.count[0]>=AlarmReceiver.COUNT_TH){
                finishAndRemoveTask();
            }
        }
    }
    Button bStopAlarm;
    Timer periodicTimer;
//    Button bKillAct;
    CountDownTimer timer = new CountDownTimer(2000,1000){

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            hideSystemUI();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        Remove   notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm_wakeup_screen);
        View decorView = getWindow().getDecorView();
        periodicTimer = new Timer();

        decorView.setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    // Note that system bars will only be "visible" if none of the
                    // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        Log.d("abcd1234", "onCreate: system ui visible");
                        timer.start();
                        // adjustments to your UI, such as showing the action bar or
                        // other navigational controls.
                    } else {
                        Log.d("abcd1234", "onCreate: system ui not visible");
                        // adjustments to your UI, such as hiding the action bar or
                        // other navigational controls.
                    }
                });

        bStopAlarm = findViewById(R.id.b_stop_alarm);
//        bKillAct = findViewById(R.id.b_kill_activity);
        bStopAlarm.setOnClickListener(this);
//        bKillAct.setOnClickListener(this);
        periodicTimer.scheduleAtFixedRate(new MediaCheckerTask(), 1000,5000);

//        Intent intent = getIntent();
//        int classId = intent.getIntExtra(CLASS_NAME, -1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_stop_alarm:
                if(AlarmReceiver.player!=null && AlarmReceiver.player.isPlaying()) {
                    AlarmReceiver.player.stop();
                }
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
                finishAndRemoveTask();
                break;
//            case R.id.b_kill_activity:
//                finish();
//                break;
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        String TAG = "abcd";
        Log.d(TAG, "onWindowFocusChanged: change in focus abcd");
        if (hasFocus) {
            Log.d(TAG, "onWindowFocusChanged: foucs received abcd");
            hideSystemUI();
        }
    }

//    @Override
//    protected void onStop() {
//        onClick(bStopAlarm);
//        super.onStop();
//    }

    @Override
    protected void onResume() {
        if(AlarmReceiver.player == null || !AlarmReceiver.player.isPlaying()){
            finish();
        }
        super.onResume();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

//    private void showSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }
}
