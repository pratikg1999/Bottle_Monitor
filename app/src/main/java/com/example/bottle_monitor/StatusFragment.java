package com.example.bottle_monitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_DEV_ID = "dev_id";
    public static final String ARG_BOTTLE_QTY = "bottle_qty";
    private String device_id = "1"; //TODO remove default value important
    private Float bottle_qty = 1f; //TODO  may remove default value

    TextView tv_rate;
    TextView tv_rem_qty;
    TextView tv_est_time;
    TextView tv_alarm_status;
    CheckBox cb_notify;
    EditText et_alarm_time;
    Button bt_set_alarm;
    Button bt_rem_alarm;

    Switch tb_on_off;
    Switch switch_flow;

    float rate;
    float rem_qty;
    float ls_reading;
    float sm_reading;
    int time_rem_sec;
    float density = 1;
    float rem_qty_in_ml;

    Calendar est_time = Calendar.getInstance();


    DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("devices");
    DatabaseReference curDeviceRef;


    private OnFragmentInteractionListener mListener;

    static String getFormattedDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return  dateFormat.format(date);

    }
    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dev_id Parameter 1.
     * @param bottle_qty Parameter 1.
     * @return A new instance of fragment StatusFragment.
     */
    public static StatusFragment newInstance(String dev_id, float bottle_qty) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEV_ID, dev_id);
        args.putFloat(ARG_BOTTLE_QTY, bottle_qty);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device_id = getArguments().getString(ARG_DEV_ID);
            bottle_qty = getArguments().getFloat(ARG_BOTTLE_QTY);
            curDeviceRef = deviceRef.child(device_id+"");
            Toast.makeText(getActivity(), device_id+"", Toast.LENGTH_SHORT).show();
        }
    }

    void disableFunctions(){
        et_alarm_time.setEnabled(false);
        bt_set_alarm.setEnabled(false);
        switch_flow.setEnabled(false);

    }

    void enableFunctions(){
        et_alarm_time.setEnabled(true);
        bt_set_alarm.setEnabled(true);
        switch_flow.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NotificationHelper.dispNotification(getActivity(), "test", "successful");
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        tv_est_time = v.findViewById(R.id.tv_est_time);
        tv_rate = v.findViewById(R.id.tv_rate);
        tv_rem_qty = v.findViewById(R.id.tv_rem_qty);
        tv_alarm_status = v.findViewById(R.id.tv_alarm_status);
        et_alarm_time = v.findViewById(R.id.et_alarm_time);

        tb_on_off = v.findViewById(R.id.tb_on_off);
        switch_flow = v.findViewById(R.id.switch_flow);

        cb_notify = v.findViewById(R.id.cb_notify);
        cb_notify.setOnClickListener(this);

        bt_rem_alarm = v.findViewById(R.id.bt_rem_alarm);
        bt_set_alarm = v.findViewById(R.id.bt_set_alarm);
        bt_set_alarm.setOnClickListener(this);
        bt_rem_alarm.setOnClickListener(this);

        switch_flow.setOnClickListener(this);
        tb_on_off.setOnClickListener(this);

        curDeviceRef.child("on_off").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isOnOff = dataSnapshot.getValue(Boolean.class);
                tb_on_off.setChecked(isOnOff!=null ? isOnOff : false);
                if(!tb_on_off.isChecked()){
                    disableFunctions();
                }
                else {
                    enableFunctions();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        curDeviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rate = dataSnapshot.child("rate").getValue(Float.class);
                ls_reading = dataSnapshot.child("ls_reading").getValue(Float.class);
                sm_reading = dataSnapshot.child("sm_reading").getValue(Float.class);
                rate = rate>0.001f ? rate : 0.001f;
                time_rem_sec = (int) (ls_reading/rate);
                est_time = Calendar.getInstance();
                est_time.add(Calendar.SECOND, time_rem_sec);
                tv_rate.setText(rate+"");
                tv_est_time.setText(getFormattedDate(est_time.getTime()));
                rem_qty_in_ml = ls_reading/density;
                tv_rem_qty.setText(rem_qty_in_ml+"");
                if(rem_qty_in_ml<=1){
//                    et_alarm_time.setEnabled(false);
//                    bt_set_alarm.setEnabled(false);
                    cb_notify.setChecked(false);
                    cb_notify.setEnabled(false);
                    curDeviceRef.child("on_off").setValue(false);
                    if(cb_notify.isChecked()){
                        NotificationHelper.dispNotification(getActivity(), device_id + " completed", device_id + " bottle is now emptied and turned off");
                    }
                }
                Toast.makeText(getActivity(), rem_qty_in_ml+"", Toast.LENGTH_SHORT).show();
                Boolean on_off_status = dataSnapshot.child("on_off").getValue(Boolean.class);
                tb_on_off.setChecked(on_off_status!=null ? on_off_status : false);
                if(!tb_on_off.isChecked()){
                    disableFunctions();
                }

                switch_flow.setChecked(!(sm_reading <=5.1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_set_alarm:
                String min_bef_string = et_alarm_time.getText().toString();

                        if(min_bef_string!=null && !min_bef_string.equals("")) {
                            int min_bef = Integer.parseInt(min_bef_string);

                            Calendar alarmCal  = (Calendar) est_time.clone();
                            alarmCal.add(Calendar.MINUTE, -min_bef);
//                            Calendar cal = Calendar.getInstance();
//
//                            cal.setTimeInMillis(System.currentTimeMillis());
//                            cal.clear();
//                            cal.set(2012, 2, 8, 18, 16);

                            AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                            // cal.add(Calendar.SECOND, 5);
                            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(), pendingIntent);
                            Toast.makeText(getActivity(), "Alarm set for "+ alarmCal.getTime().toString() , Toast.LENGTH_SHORT).show();
                            tv_alarm_status.setText("Alarm set at "+ getFormattedDate(alarmCal.getTime()));
                        }
                        else{
                            et_alarm_time.setError("necessary");
                        }


                break;
            case R.id.bt_rem_alarm:
                AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

                alarmMgr.cancel(pendingIntent);
                Toast.makeText(getActivity(), "alarm cancelled", Toast.LENGTH_SHORT).show();
                tv_alarm_status.setText("No alarm set");
                break;

            case R.id.cb_notify:
                if (cb_notify.isChecked()) {

                } else {

                }
                break;

            case R.id.tb_on_off:
                curDeviceRef.child("on_off").setValue(tb_on_off.isChecked());
                break;


            case R.id.switch_flow:
                if(switch_flow.isChecked()) {
                    if(rem_qty_in_ml>4) {
                        curDeviceRef.child("sm_reading").setValue(6);
                    }
                    else{
                        Toast.makeText(getActivity(), "Can't open on low quantity", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    curDeviceRef.child("sm_reading").setValue(5);
                }

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
