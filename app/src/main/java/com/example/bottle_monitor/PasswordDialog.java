package com.example.bottle_monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PasswordDialog extends DialogFragment {

    public static PasswordDialog getInstance() {
        PasswordDialog fragment = new PasswordDialog();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PasswordDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implemet Password dialog");
        }
    }

    private PasswordDialogListener listener;

    EditText etPass;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Enter Password")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String password = etPass.getText().toString();
                        Toast.makeText(getContext(), password, Toast.LENGTH_SHORT).show();
                        listener.applytexts(password);
                        Toast.makeText(getContext(),"ajdsfnkj",Toast.LENGTH_SHORT).show();
                    }
                });

        etPass = (EditText) view.findViewById(R.id.etPassword);

        return builder.create();


    }

    public interface PasswordDialogListener{
        void applytexts(String password);
    }


}
