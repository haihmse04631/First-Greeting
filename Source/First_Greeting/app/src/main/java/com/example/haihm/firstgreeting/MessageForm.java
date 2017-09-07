package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;
    Intent intent;
    Bundle bund;
    TextView txtCheck;
    String title = "Name of your friend";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
//        txtCheck = findViewById(R.id.txtCheck);
//        txtCheck.setText(bund.getString("fbSendId") + "\n" + bund.getString("fbReceiveId"));

    }

    public void pushMessageToFirebase() {

    }
}
