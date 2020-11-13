package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.R;

public class SplashScreenAct extends AppCompatActivity {
    private String TAG = "SplashScreenAct";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Intent mainAct = new Intent(SplashScreenAct.this, LoginAct.class);
                startActivity(mainAct);
                finish();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1500);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }
}
