package com.example.bottle_monitor;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView rv_patients;
    TextView tv_no_patients;

    HashMap<Integer, Patient> patients = new HashMap<Integer, Patient>();
    ArrayList<Patient> patientList = new ArrayList<Patient>(){
        @Override
        public int indexOf(@Nullable Object o) {
            if(o instanceof Patient) {
                o = (Patient)o;
                for (int i = 0; i < this.size(); i++)
                    if (this.get(i).getId() == ((Patient) o).getId())
                        return i;
                return -1;
            }
            return -1;
        }
    };
//    ArrayList<Integer> keys = new ArrayList<Integer>();
    DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
DatabaseReference curPatientRef = FirebaseDatabase.getInstance().getReference("curPatientId");

    public PatientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientsFragment newInstance(String param1, String param2) {
        PatientsFragment fragment = new PatientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        curPatientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer curPatientId = dataSnapshot.getValue(Integer.class);
                Patient.curPatientId = curPatientId;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patients, container, false);
        rv_patients = v.findViewById(R.id.rv_patients);
        tv_no_patients = v.findViewById(R.id.tv_no_patients);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_patient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPatient();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_patients.setLayoutManager(layoutManager);
        final PatientAdapter adapter = new PatientAdapter(getActivity(), patientList);
        rv_patients.setAdapter(adapter);
//        if(patients.isEmpty()){
//            tv_no_patients.setVisibility(View.VISIBLE);
//            rv_patients.setVisibility(View.GONE);
//        }
//        else{
            tv_no_patients.setVisibility(View.GONE);
            rv_patients.setVisibility(View.VISIBLE);
//        }

        patientsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Patient newPatient = dataSnapshot.getValue(Patient.class);
                patients.put(Integer.parseInt(dataSnapshot.getKey()), newPatient);
//                keys.add(Integer.parseInt(dataSnapshot.getKey()));
                patientList.add(dataSnapshot.getValue(Patient.class));
                adapter.notifyItemInserted(patientList.size()-1);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int key = Integer.parseInt(dataSnapshot.getKey());
                Patient curPatient = dataSnapshot.getValue(Patient.class);
                patients.put(key, dataSnapshot.getValue(Patient.class));
                int index = patientList.indexOf(curPatient);
                patientList.set(index, curPatient);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                patients.remove(Integer.parseInt(dataSnapshot.getKey()));
                Patient curPatient = dataSnapshot.getValue(Patient.class);
                int index = patientList.indexOf(curPatient);
                patientList.remove(curPatient);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  v;

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder>{
        Context ctx;
        ArrayList<Patient> patients;

        public PatientAdapter(Context ctx, ArrayList<Patient> patients) {
            this.ctx = ctx;
            this.patients = patients;
        }

        @NonNull
        @Override
        public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false);
            PatientViewHolder holder = new PatientViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
            Patient patient = patients.get(position);
            holder.tv_patient_name.setText(patient.getName());
            holder.tv_patient_disease.setText(patient.getDiseaseName());
        }

        @Override
        public int getItemCount() {
            return patientList.size();
        }

        class PatientViewHolder extends RecyclerView.ViewHolder{
            TextView tv_patient_name;
            TextView tv_patient_disease;
            public PatientViewHolder(@NonNull View v) {
                super(v);
                tv_patient_name = v.findViewById(R.id.tv_patient_name);
                tv_patient_disease = v.findViewById(R.id.tv_patient_disease);

            }
        }
    }

    void addNewPatient(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.new_patient_card,null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(v);
        dialogBuilder.setTitle("Create new outpass");
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText et_patient_name = v.findViewById(R.id.et_patient_name);
        EditText et_patient_disease = v.findViewById(R.id.et_patient_disease);
        Button b_add_patient = v.findViewById(R.id.b_add_patient);

        b_add_patient.setOnClickListener(view -> {
            String name = et_patient_name.getText().toString();
            String diseaseName = et_patient_disease.getText().toString();
            Patient newPatient = new Patient(name, diseaseName, null);
            patientsRef.child(""+newPatient.getId()).setValue(newPatient);
            curPatientRef.setValue(Patient.curPatientId);
            dialog.dismiss();
        });
    }
}
