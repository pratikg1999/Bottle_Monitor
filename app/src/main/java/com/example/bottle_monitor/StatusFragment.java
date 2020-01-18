package com.example.bottle_monitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private static final String ARG_DEV_ID = "dev_id";
    private String device_id;

    TextView tv_rate;
    TextView tv_rem_qty;
    TextView tv_est_time;
    TextView tv_alarm_status;
    CheckBox cb_notify;
    EditText et_alarm_time;
    Button bt_set_alarm;
    Button bt_rem_alarm;

    int rate;
    int rem_qty;
    Calendar est_time = Calendar.getInstance();

    DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("devices");



    private OnFragmentInteractionListener mListener;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StatusFragment.
     */
    public static StatusFragment newInstance(String param1) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEV_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device_id = getArguments().getString(ARG_DEV_ID);
        }
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

        cb_notify = v.findViewById(R.id.cb_notify);
        cb_notify.setOnClickListener(this);

        bt_rem_alarm = v.findViewById(R.id.bt_rem_alarm);
        bt_set_alarm = v.findViewById(R.id.bt_set_alarm);
        bt_set_alarm.setOnClickListener(this);
        bt_rem_alarm.setOnClickListener(this);


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
                            alarmCal.add(Calendar.MINUTE, min_bef);
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
                            tv_alarm_status.setText("Alarm set at "+ alarmCal.getTime().toString());
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
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
