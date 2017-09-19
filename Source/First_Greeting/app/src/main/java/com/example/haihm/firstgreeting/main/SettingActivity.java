package com.example.haihm.firstgreeting.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.haihm.firstgreeting.R;
import com.github.nkzawa.socketio.client.Socket;

public class SettingActivity extends AppCompatActivity {
    Button btnStart;
    private Socket mSocket;
    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }
}
