 package com.example.bottle_monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

 public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

     SharedPreferences sharedPreferences;
     DatabaseReference passRef = FirebaseDatabase.getInstance().getReference("password");
     DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("username");
     public static final String[] PASSWORD = {"abcd"};
     public static final String[] USERNAME = {"saurabh"}; //TODO remove "saurabh"

//     String actualPassword;
//     String userEmail;
     public static final String AC_PASS_KEY = "actual password";
//     public static final String AC_EMAIL_KEY = "email";
     public static final String IS_ALREADY_LOGGED_IN_KEY = "is already logged in";
     public static final String PASSWORD_KEY=AC_PASS_KEY;
     public static final String USERNAME_KEY= "username";


     EditText etUsername;
     EditText etPassword;
     TextView tv_change_pass;
     TextView tv_login_title;
     TextView tv_show_pass_proto;
     TextView tv_show_username_proto;

     Button bLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_change_pass = findViewById(R.id.tv_change_pass);
        tv_change_pass.setPaintFlags(tv_change_pass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_change_pass.setOnClickListener(this);
        tv_show_pass_proto = findViewById(R.id.tv_show_pass_proto);
        tv_show_username_proto = findViewById(R.id.tv_show_username_proto);
        tv_login_title = findViewById(R.id.tv_login_title);
        bLogin = findViewById(R.id.bLogin);

        etUsername = findViewById(R.id.etUsernameLogin);
        etPassword = findViewById(R.id.etPasswordLogin);


        sharedPreferences = this.getSharedPreferences("com.example.bottle_monitor", MODE_PRIVATE);
        String storedPass = sharedPreferences.getString(PASSWORD_KEY, null);
        String storedUsername = sharedPreferences.getString(USERNAME_KEY, null);
        if(storedPass != null){
            PASSWORD[0] = storedPass;
            String decryptedPass = AES.decrypt(PASSWORD[0], AES.SECRET_KEY);
            setHtmlText(tv_show_pass_proto, "For the prototype, current password is <span style=\"background-color: #FFFF00\"><b>"+decryptedPass+"</b></span>");
        }
        if(storedUsername != null){
            USERNAME[0] = storedUsername;
            String decryptedPass = AES.decrypt(USERNAME[0], AES.SECRET_KEY);
            setHtmlText(tv_show_username_proto, "For the prototype, current username is <span style=\"background-color: #FFFF00\"><b>"+decryptedPass+"</b></span>");
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
//                Toast.makeText(LoginActivity.this, "password: "+ PASSWORD[0], Toast.LENGTH_SHORT).show();
                String decryptedPass = AES.decrypt(PASSWORD[0], AES.SECRET_KEY);
                setHtmlText(tv_show_pass_proto, "For the prototype, current password is <span style=\"background-color: #FFFF00\"><b>"+decryptedPass+"</b></span>");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                USERNAME[0] = dataSnapshot.getValue(String.class);
                sharedPreferences.edit().putString(USERNAME_KEY, USERNAME[0]).apply();
//                Toast.makeText(LoginActivity.this, "username: "+ USERNAME[0], Toast.LENGTH_SHORT).show();
                setHtmlText(tv_show_username_proto, "For the prototype, current username is <span style=\"background-color: #FFFF00\"><b>"+USERNAME[0]+"</b></span>");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setHtmlText(tv_login_title, "<h6>Liqu<<font color=\"red\">IV</font>ics</h6>");


        bLogin.setOnClickListener(this);
    }


    public static void setHtmlText(TextView tv, String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv.setText(Html.fromHtml(html));
        }
    }
    void login(){
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        //Toast.makeText(getContext(), "Email: "+username, Toast.LENGTH_SHORT).show();
        String encPassword = AES.encrypt(password, AES.SECRET_KEY);
        Toast.makeText(this, sharedPreferences.getString(USERNAME_KEY, "no username"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, sharedPreferences.getString(AC_PASS_KEY, "no pass"), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), email.length()+"", Toast.LENGTH_SHORT).show();
        if(username.isEmpty() || username.length()==0){
            etUsername.setError("Enter an username");
            etUsername.requestFocus();
        }
        if(password.isEmpty()){
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
        }
        else if(!username.equals(USERNAME[0])){
            etUsername.setError("Wrong username");
            etUsername.requestFocus();
        }
        else if (!encPassword.equals(PASSWORD[0])){
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
        final AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText et_new_username = v.findViewById(R.id.et_new_username);
        EditText et_old_password = v.findViewById(R.id.et_old_pass);
        EditText et_new_password = v.findViewById(R.id.et_new_pass);
        Button  bt_change_credentials = v.findViewById(R.id.bt_change_credentials);

//        Toast.makeText(ctx, sharedPreferences.getString(AC_PASS_KEY, "nopass"), Toast.LENGTH_SHORT).show();
        bt_change_credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = et_new_username.getText().toString();
                String oldPass = et_old_password.getText().toString();
                String newPass = et_new_password.getText().toString();

                String tempActPass = sharedPreferences.getString(AC_PASS_KEY, "");
                Log.d("oldPass", oldPass);
                newUsername = newUsername!=null ? newUsername : "";
                oldPass = oldPass!=null ? oldPass : "";
                oldPass = AES.encrypt(oldPass, AES.SECRET_KEY);
                Log.d("password abcd", oldPass + " " + tempActPass);
                Log.d("password abcde", sharedPreferences.getString(AC_PASS_KEY, ""+"E"));
                if(oldPass.equals(tempActPass)){
                    newPass = AES.encrypt(newPass, AES.SECRET_KEY);
                    sharedPreferences.edit().putString(AC_PASS_KEY, newPass).putString(USERNAME_KEY, newUsername).apply();
                    usernameRef.setValue(newUsername);
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
