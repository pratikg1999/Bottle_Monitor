package com.example.bottle_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new SocketThread()).start();
    }

class SocketThread implements  Runnable{

    Socket s;
//    ServerSocket ss;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    Handler h = new Handler();
    @Override
    public void run() {
        try {
//            ss = new ServerSocket(7801);
            s = new Socket("192.168.0.106", 7800);
            inputStreamReader = new InputStreamReader(s.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            while(true) {
                final String message = bufferedReader.readLine();
                Thread.sleep(2000);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }

    }
}
}
