package com.example.bottle_monitor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;


public class NavActivity extends AppCompatActivity  implements  AddBottleFragment.OnFragmentInteractionListener, StatusFragment.OnFragmentInteractionListener, DevicesInUse.OnFragmentInteractionListener{
    private AppBarConfiguration mAppBarConfiguration;
    public static final String CHANNEL_ID = "NOTIFICATION CHANNEL";
    SharedPreferences sharedPreferences;
    public static final String CUR_PATIENT_ID_KEY="CUR_PATIENT_ID_KEY";
    public static final String CUR_BOTTLE_ID_KEY="CUR_BOTTLE_ID_KEY";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public void createNotificationChannel(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "desc", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("fcm notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawer.openDrawer(navigationView);
                Log.d("NICK", "button button button..................");
            }
        });

        Menu menuNav = navigationView.getMenu();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.addBottleFragment, R.id.statusFragment, R.id.devicesInUse)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        createNotificationChannel();

        sharedPreferences = this.getSharedPreferences("com.example.bottle_monitor", MODE_PRIVATE);
        Patient.curPatientId = sharedPreferences.getInt(CUR_PATIENT_ID_KEY, 0);
        Bottle.curBottleId = sharedPreferences.getInt(CUR_BOTTLE_ID_KEY, 0);

        database.child("curPatientId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val = dataSnapshot.getValue(Integer.class);
                Patient.curPatientId = val;
                sharedPreferences.edit().putInt(CUR_BOTTLE_ID_KEY, val).apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.child("curBottleId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val = dataSnapshot.getValue(Integer.class);
                Bottle.curBottleId = val;
                sharedPreferences.edit().putInt(CUR_BOTTLE_ID_KEY, val).apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                sharedPreferences.edit().putBoolean(LoginActivity.IS_ALREADY_LOGGED_IN_KEY, false).apply();
                startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return true;
    }

//    @Override
//    public void applytexts(String password) {
//
//        Toast.makeText(this,"urhj",Toast.LENGTH_SHORT).show();
//
//    }
}
