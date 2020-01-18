package com.example.bottle_monitor;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.List;


public class AddBottleFragment extends Fragment implements AdapterView.OnItemSelectedListener, PasswordDialog.PasswordDialogListener {

    private OnFragmentInteractionListener mListener;
    String[] lDevices = {"Device 1","Device 2","Device 3","Device 4"};
    String[] lPatients = {"Patient 1","Patient 2","Patient 3","Patient 4"};
    private Button btnSwitchOn;
    private String password;
    

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
        btnSwitchOn = (Button) v.findViewById(R.id.btnSwitchOn);
        btnSwitchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });
        Spinner spDevices = (Spinner) v.findViewById(R.id.spDevice);
        spDevices.setOnItemSelectedListener(this);

        ArrayAdapter aaDevices = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,lDevices);
        aaDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spDevices.setAdapter(aaDevices);

        Spinner spPatients = (Spinner) v.findViewById(R.id.spPatient);
        spPatients.setOnItemSelectedListener(this);

        ArrayAdapter aaPatients = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, lPatients);
        aaPatients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPatients.setAdapter(aaPatients);

        return v;
    }

    private void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getFragmentManager(), "Password dialog");

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(getContext(),lDevices[i] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void applytexts(String password) {

        password = this.password;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
