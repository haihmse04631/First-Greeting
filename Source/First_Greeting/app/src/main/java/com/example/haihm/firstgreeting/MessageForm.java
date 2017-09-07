package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;

    Intent intent;
    Bundle bund;
    String sendId;
    String sendAvartarLink;
    String receiveId;
    String receiveAvartarLink;

    private ListView lvListMessage;
    private ListMessageAdapter adapter;
    private MessageList messList;

    ImageButton btnSend;
    EditText txtInput;
    String title = "Name of your friend";

    int numberOfSend;
    int lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        setContentView(R.layout.activity_message);

        mData = FirebaseDatabase.getInstance().getReference();
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        txtInput = (EditText) findViewById(R.id.txtInput);

        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
        sendId = bund.getString("fbSendId");
        sendAvartarLink = bund.getString("fbSendAvatarLink");
        receiveId = bund.getString("fbReceiveId");
        receiveAvartarLink = bund.getString("fbReceiveAvatarLink");
        lvListMessage = (ListView) findViewById(R.id.listViewMessage);

        messList = new MessageList();
        adapter = new ListMessageAdapter(MessageForm.this, R.layout.row_send_message, R.layout.row_receive_message, messList);
        lvListMessage.setAdapter(adapter);

        numberOfSend = 0;
        lock = 0;
        loadMessage(sendId, receiveId);
        Thread t = Thread.currentThread();
        try {
            t.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pushMessageToFirebase(sendId, receiveId);
    }

    private void sortList() {
        for (int i = 0; i < messList.size() - 1; i++) {
            for (int j = i + 1; j < messList.size(); j++) {
                if (messList.get(i).getDate().toString().compareTo(messList.get(j).getDate().toString()) > 0) {
                    SingleMessage aMess = messList.get(i);
                    messList.set(i, messList.get(j));
                    messList.set(j, aMess);
                }
            }
        }
    }

    public void loadMessage(String sendId, String receiveId) {
        mData.child("Message").child(sendId).child(receiveId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                numberOfSend++;
                SingleMessage aMessage = dataSnapshot.getValue(SingleMessage.class);
                aMessage.setType("send");
                messList.addMess(aMessage);
                sortList();
                adapter.notifyDataSetChanged();
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

        mData.child("Message").child(receiveId).child(sendId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SingleMessage aMessage = dataSnapshot.getValue(SingleMessage.class);
                aMessage.setType("receive");
                messList.addMess(aMessage);
                sortList();
                adapter.notifyDataSetChanged();
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

    public void pushMessageToFirebase(final String sendId, final String receiveId) {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = txtInput.getText().toString();
                txtInput.setText("");
                SingleMessage aMess = new SingleMessage(new Date(), content, sendAvartarLink);
                mData.child("Message").child(sendId).child(receiveId).child(Integer.toString(numberOfSend)).setValue(aMess);
            }
        });
    }
}
