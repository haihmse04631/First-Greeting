package com.example.haihm.firstgreeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Setting extends AppCompatActivity {
    Button btnStart;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try {
            mSocket = IO.socket("http://192.168.1.9:3000");
        } catch (URISyntaxException e) {
        }
        mSocket.connect();
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DaTa: ", "CLicked!");
                mSocket.emit("start-call", "");
            }
        });
    }
}
