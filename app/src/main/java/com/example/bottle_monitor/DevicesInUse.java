package com.example.bottle_monitor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DevicesInUse extends Fragment {

    ListView listView;
    TextView textView;
    ArrayList<String> listItem;
    List<Device> devices;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dRef = database.getReference("devices");




    private OnFragmentInteractionListener mListener;

    public DevicesInUse() {
        // Required empty public constructor
    }


    public static DevicesInUse newInstance(String param1, String param2) {
        DevicesInUse fragment = new DevicesInUse();
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
        View v = inflater.inflate(R.layout.fragment_devices_in_use, container, false);
        devices = new ArrayList<Device>();
        listItem = new ArrayList<String>();
        listView=(ListView)v.findViewById(R.id.lvDevices);
        textView=(TextView)v.findViewById(R.id.textView);



        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    adapter.add(ds.getKey());
                    devices.add(ds.child(ds.getKey()).getValue(Device.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value=adapter.getItem(position);
                Toast.makeText(getContext(),value,Toast.LENGTH_SHORT).show();

                StatusFragment statusFragment = StatusFragment.newInstance(value, 220);
                Bundle statusFragmentBundle = new Bundle();
                statusFragmentBundle.putString(StatusFragment.ARG_DEV_ID, value);
                statusFragmentBundle.putFloat(StatusFragment.ARG_BOTTLE_QTY, 220);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.statusFragment, statusFragmentBundle);

            }
        });


        return v;
    }


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
