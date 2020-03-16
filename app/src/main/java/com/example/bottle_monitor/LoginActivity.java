 package com.example.bottle_monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

 public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

     SharedPreferences sharedPreferences;
     DatabaseReference passRef = FirebaseDatabase.getInstance().getReference("password");
     public static final String[] PASSWORD = {"abcd"};

//     String actualPassword;
//     String userEmail;
     public static final String AC_PASS_KEY = "actual password";
     public static final String AC_EMAIL_KEY = "email";
     public static final String IS_ALREADY_LOGGED_IN_KEY = "is already logged in";
     public static final String PASSWORD_KEY=AC_PASS_KEY;


     EditText etEmail;
     EditText etPassword;
     TextView tv_change_pass;
     Button bLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = this.getSharedPreferences("com.example.bottle_monitor", MODE_PRIVATE);
        String storedPass = sharedPreferences.getString(PASSWORD_KEY, null);
        if(storedPass != null){
            PASSWORD[0] = storedPass;
        }
//        actualPassword = sharedPreferences.getString(AC_PASS_KEY, null);
//        userEmail = sharedPreferences.getString(AC_EMAIL_KEY, null);
        boolean isAlreadyLoggedIn = sharedPreferences.getBoolean(IS_ALREADY_LOGGED_IN_KEY, false);
        if(isAlreadyLoggedIn){
            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NavActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }

        passRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PASSWORD[0] = dataSnapshot.getValue(String.class);
                sharedPreferences.edit().putString(PASSWORD_KEY, PASSWORD[0]).apply();
                Toast.makeText(LoginActivity.this, "password: "+ PASSWORD[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        tv_change_pass = findViewById(R.id.tv_change_pass);
        tv_change_pass.setPaintFlags(tv_change_pass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_change_pass.setOnClickListener(this);
        bLogin = findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
    }


    void login(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        //Toast.makeText(getContext(), "Email: "+email, Toast.LENGTH_SHORT).show();

        Toast.makeText(this, sharedPreferences.getString(AC_EMAIL_KEY, "no email"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, sharedPreferences.getString(AC_PASS_KEY, "no pass"), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), email.length()+"", Toast.LENGTH_SHORT).show();
//        if(email==null || email.length()==0){
//            etEmail.setError("Enter an email");
//            etEmail.requestFocus();
//        }
//        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            etEmail.setError("Enter a valid email address");
//            etEmail.requestFocus();
//        }
        if(password==""){
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
        }
//        else if(!email.equals(userEmail)){
//            etEmail.setError("Wrong email");
//        }
        else if (!password.equals(PASSWORD[0])){
            etPassword.setError("Wrong password");
        }
        else {
            sharedPreferences.edit().putBoolean(IS_ALREADY_LOGGED_IN_KEY, true).apply();
            startActivity(new Intent(this, NavActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    void changePassword(Context ctx){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.change_pass_view,null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(v);
        dialogBuilder.setTitle("Create credentials");
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText et_new_email = v.findViewById(R.id.et_new_email);
        EditText et_old_password = v.findViewById(R.id.et_old_pass);
        EditText et_new_password = v.findViewById(R.id.et_new_pass);
        Button  bt_change_credentials = v.findViewById(R.id.bt_change_credentials);

//        Toast.makeText(ctx, sharedPreferences.getString(AC_PASS_KEY, "nopass"), Toast.LENGTH_SHORT).show();
        bt_change_credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = et_new_email.getText().toString();
                String oldPass = et_old_password.getText().toString();
                String newPass = et_new_password.getText().toString();

                String tempActPass = sharedPreferences.getString(AC_PASS_KEY, "");
                Log.d("oldPass", oldPass);
                newEmail = newEmail!=null ? newEmail : "";
                oldPass = oldPass!=null ? oldPass : "";
                Log.d("password abcd", oldPass + " " + tempActPass);
                Log.d("password abcde", sharedPreferences.getString(AC_PASS_KEY, ""+"E"));
                if(oldPass.equals(tempActPass)){

                    sharedPreferences.edit().putString(AC_PASS_KEY, newPass).putString(AC_EMAIL_KEY, newEmail).apply();
                    PASSWORD[0] = newPass;
                    passRef.setValue(newPass);
//                    userEmail = newEmail;
//                    actualPassword = newPass;
//                    Toast.makeText(ctx, sharedPreferences.getString(AC_PASS_KEY, "nopass"), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                }
                else{
                    et_old_password.setError("Wrong Password");
                }
            }
        });

    }
     @Override
     public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                login();
                break;
            case R.id.tv_change_pass:
                changePassword(this);
                break;
        }
     }
 }
