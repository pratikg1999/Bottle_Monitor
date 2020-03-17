package com.example.bottle_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView tv_splash_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_splash_title = findViewById(R.id.tv_splash_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_splash_title.setText(Html.fromHtml("<h4>Liqu<<font color=\"red\">IV</font>ics</h4>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_splash_title.setText(Html.fromHtml("<h4>Liqu<<font color=\"red\">IV</font>ics</h4>"));
        }
        ImageView logo=findViewById(R.id.splashlogo);

        Animation animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotation);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }


}
