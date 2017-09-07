package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;
    Intent intent;
    Bundle bund;

    TextView txtTitle;

    String sendId;
    String receiveId;
    private ListView lvListMessage;
    private ListMessageAdapter adapter;
    private ArrayList<SingleMessage> messList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_layout);
        txtTitle = (TextView) findViewById(R.id.txtMyTitle);
        txtTitle.setText("Name of your friend");

        mData = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
        sendId = bund.getString("fbSendId");
        receiveId = bund.getString("fbReceiveId");
        lvListMessage = (ListView) findViewById(R.id.listViewMessage);

        messList = new ArrayList<>();
        adapter = new ListMessageAdapter(MessageForm.this, R.layout.row_send_message, R.layout.row_receive_message, messList);
        lvListMessage.setAdapter(adapter);
        SingleMessage aMess1 = new SingleMessage(new Date(), "Anh yeu me", "https://graph.facebook.com/1150825215061261/picture?width=960&height=960");
        aMess1.setType("send");
        SingleMessage aMess2 = new SingleMessage(new Date(), "Em yeu bo", "https://graph.facebook.com/879445752208301/picture?width=960&height=960");
        aMess2.setType("receive");
        messList.add(aMess1);
        messList.add(aMess2);

//        loadMessage(sendId, receiveId);
    }

    public void loadMessage(final String sendId, String receiveId) {
        mData.child("Message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = (String) dataSnapshot.getKey();
                if (id.equals(sendId)) {
                    return;
                }
                Message message = new Message();
                Iterator iterator1 = dataSnapshot.child("message").getChildren().iterator();
                while (iterator1.hasNext()) {
                    DataSnapshot item1 = (DataSnapshot) iterator1.next();
                    ArrayList<SingleMessage> list = new ArrayList<SingleMessage>();
                    Iterator iterator2 = dataSnapshot.child("message").child(item1.getKey()).getChildren().iterator();
                    while (iterator2.hasNext()) {
                        DataSnapshot item2 = (DataSnapshot) iterator2.next();
                        SingleMessage aMessage = item2.getValue(SingleMessage.class);
                        list.add(aMessage);
                    }
                    message.put(item1.getKey(), list);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushMessageToFirebase() {

    }
}
