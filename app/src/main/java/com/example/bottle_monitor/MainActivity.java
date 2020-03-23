package com.example.bottle_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener
{

    TextView tv_splash_title;
    public TextView title,title1,title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationContext().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

// this area is use less

    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


        title = findViewById(R.id.tv_splash_title);
        title1 = findViewById(R.id.tv_splash_dev1);
        title2 = findViewById(R.id.tv_splash_dev2);


        tv_splash_title = findViewById(R.id.tv_splash_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_splash_title.setText(Html.fromHtml("<h4>Liqu<<font color=\"red\">IV</font>ics</h4>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_splash_title.setText(Html.fromHtml("<h4>Liqu<<font color=\"red\">IV</font>ics</h4>"));
        }
        ImageView logo = findViewById(R.id.splashlogo);

        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotation);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (ConnectivityReceiver.isConnected()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    title.setText("Internet Unavailable!");
                    title1.setText("Please connect to Internet");
                    title2.setText("");
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
