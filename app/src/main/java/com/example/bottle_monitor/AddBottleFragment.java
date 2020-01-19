package com.example.bottle_monitor;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddBottleFragment extends Fragment implements PasswordDialog.PasswordDialogListener {

    private OnFragmentInteractionListener mListener;
//    String[] lDevices = {"Device 1","Device 2","Device 3","Device 4"};
    ArrayList<String> lDevices;
    int curLenDev;
//    String[] lPatients = {"Patient 1","Patient 2","Patient 3","Patient 4"};
    ArrayList<String> lPatients;
    private Button btnSwitchOn;
    private String password;
    private final String passwordSet = "123456";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference bRef = database.getReference("bottles");
    DatabaseReference curBottleIdRef = database.getReference("curBottleId");
    DatabaseReference dRef = database.getReference("devices");
    DatabaseReference pRef = database.getReference("patients");

    ArrayAdapter<String> aaDevices;
    ArrayAdapter<String> aaPatients;




    private EditText etBottleContent;
    private EditText etBottleQty;
    private EditText etRoomNo;
    private String device_id;
    private String patient_id;
    

    EditText etPass;

    public AddBottleFragment() {
        // Required empty public constructor
    }

    public static AddBottleFragment newInstance() {
        AddBottleFragment fragment = new AddBottleFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_bottle, container, false);
        lDevices = new ArrayList<String>();
        lPatients = new ArrayList<String>();
//        lDevices.add("1");



        etBottleContent = (EditText) v.findViewById(R.id.etBottleContent);
        etBottleQty = (EditText) v.findViewById(R.id.etBottleQty);
        etRoomNo = (EditText) v.findViewById(R.id.etRoomNo);

        btnSwitchOn = (Button) v.findViewById(R.id.btnSwitchOn);
        btnSwitchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });

        //Setting the ArrayAdapter data on the Spinner


        Spinner spDevices = (Spinner) v.findViewById(R.id.spDevice);



        aaDevices = new ArrayAdapter<String>( getContext() ,android.R.layout.simple_spinner_item, lDevices);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    aaDevices.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        aaDevices.add("1");
        aaDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDevices.setAdapter(aaDevices);
        spDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                device_id = String.valueOf(adapterView.getItemIdAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Spinner spPatients = (Spinner) v.findViewById(R.id.spPatient);
        spPatients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patient_id = String.valueOf(adapterView.getItemIdAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        aaPatients = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lPatients);
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    aaPatients.add(ds.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        aaPatients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPatients.setAdapter(aaPatients);

        return v;
    }

    private void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getFragmentManager(), "Password dialog");
        passwordDialog.setTargetFragment(AddBottleFragment.this, 1);

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
        
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void applytexts(String password) {

        this.password = password.trim();
        if(password.equals(passwordSet)){
            Toast.makeText(getContext(),"entered",Toast.LENGTH_SHORT).show();

            Date date = new Date();


            Bottle bottle = new Bottle(etBottleContent.getText().toString(), Float.parseFloat(etBottleQty.getText().toString()), device_id, date,patient_id, etRoomNo.getText().toString() );
//            bRef.setValue("Test");
            bRef.child(Integer.toString(bottle.getId())).setValue(bottle);
            curBottleIdRef.setValue(Bottle.curBottleId);
            dRef.child(device_id).child("on_off").setValue(true);
            Toast.makeText(getContext(),"yes",Toast.LENGTH_SHORT).show();

            StatusFragment statusFragment = StatusFragment.newInstance(device_id, bottle.getQuantity());
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.nav_host_fragment, statusFragment, "findThisFragment")
//                    .addToBackStack(null)
//                    .commit();
            Bundle statusFragmentBundle = new Bundle();
            statusFragmentBundle.putString(StatusFragment.ARG_DEV_ID, device_id);
            statusFragmentBundle.putFloat(StatusFragment.ARG_BOTTLE_QTY, bottle.getQuantity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.statusFragment, statusFragmentBundle);
        }
        else{
            Toast.makeText(getContext(),"wrong", Toast.LENGTH_SHORT).show();
        }



    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
