package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;
    Intent intent;
    Bundle bund;
    TextView txtTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_layout);
        txtTitle = (TextView) findViewById(R.id.txtMyTitle);
        txtTitle.setText("Name of you friend");

        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
//        txtCheck = findViewById(R.id.txtCheck);
//        txtCheck.setText(bund.getString("fbSendId") + "\n" + bund.getString("fbReceiveId"));

    }

    public void pushMessageToFirebase() {

    }
}
