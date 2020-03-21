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
    public boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationContext().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        flag=checkConnectivity();

        TextView title=findViewById(R.id.tv_splash_title);
        TextView title1=findViewById(R.id.tv_splash_dev1);
        TextView title2=findViewById(R.id.tv_splash_dev2);



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

//                if (flag){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                   finish();
//                }
//                else {
//                    title.setText("Internet Unavailable!");
//                    title1.setText("Please connect to Internet");
//                    title2.setText("& restart the app");
//                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public boolean checkConnectivity(){
       return ConnectivityReceiver.isConnected();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d("aboutcon", "onNetworkConnectionChanged: " + ConnectivityReceiver.isConnected());
        flag= ConnectivityReceiver.isConnected();


    }
}
