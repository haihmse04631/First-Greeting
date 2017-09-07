package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;
    Intent intent;
    Bundle bund;
    TextView txtCheck;
    private ListView lvListMessage;
//    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mData = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
//        txtCheck = findViewById(R.id.txtCheck);
//        txtCheck.setText(bund.getString("fbSendId") + "\n" + bund.getString("fbReceiveId"));

    }

    public void loadMessage(String sendId, String receiveId) {

    }

    public void pushMessageToFirebase() {

    }
}
