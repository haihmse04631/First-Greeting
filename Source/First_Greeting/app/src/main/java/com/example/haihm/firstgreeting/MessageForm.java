package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;
    //TextView txtCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //txtCheck = (TextView) findViewById(R.id.txtCheck);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myPackage");

        //txtCheck.setText(bundle.getString("fbId"));

    }

    public void pushMessageToFirebase() {

    }
}
