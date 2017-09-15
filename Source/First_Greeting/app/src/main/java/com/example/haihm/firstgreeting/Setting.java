package com.example.haihm.firstgreeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.socketio.client.Socket;

public class Setting extends AppCompatActivity {
    Button btnStart;
    private Socket mSocket;
    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSocket = VideoCall.mSocket;
        mSocket.connect();
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DaTa: ", "CLicked!");
                if (click % 2 == 0) {
                    mSocket.emit("start-call", "");
                    click++;
                }
            }
        });
    }
}
