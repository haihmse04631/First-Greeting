package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MessageForm extends AppCompatActivity {

    DatabaseReference mData;

    Intent intent;
    Bundle bund;
    TextView txtTitle;

    String sendId;
    String sendAvartarLink;
    String receiveId;
    String receiveAvartarLink;

    private ListView lvListMessage;
    private ListMessageAdapter adapter;

  //  private MessageList messList;

    ImageButton btnSend;
    EditText txtInput;
    String title = "Name of your friend";
    private ArrayList<SingleMessage> messList;

    int numberOfSend;
    int lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_layout);

        mData = FirebaseDatabase.getInstance().getReference();
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        txtInput = (EditText) findViewById(R.id.txtInput);

        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");
        sendId = bund.getString("fbSendId");
        sendAvartarLink = bund.getString("fbSendAvatarLink");
        receiveId = bund.getString("fbReceiveId");
        receiveAvartarLink = bund.getString("fbReceiveAvatarLink");
        title = bund.getString("fbReceiveName");

        txtTitle = (TextView) findViewById(R.id.txtMyTitle);
        txtTitle.setText(title);

        lvListMessage = (ListView) findViewById(R.id.listViewMessage);
        messList = new ArrayList<>();
        adapter = new ListMessageAdapter(MessageForm.this, R.layout.row_send_message, R.layout.row_receive_message, messList);
        lvListMessage.setAdapter(adapter);

        numberOfSend = 0;
        lock = 0;
        loadMessage(sendId, receiveId);
//        Thread t = Thread.currentThread();
//        try {
//            t.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        pushMessageToFirebase(sendId, receiveId);
    }

    private void sortList() {
        Collections.sort(messList, new Comparator<SingleMessage>(){
            public int compare(SingleMessage p1, SingleMessage p2){
                return p1.getDate().compareTo(p2.getDate());
            }
        });
    }

    public void loadMessage(String sendId, String receiveId) {
        mData.child("Message").child(sendId).child(receiveId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                numberOfSend++;
                SingleMessage aMessage = dataSnapshot.getValue(SingleMessage.class);
                aMessage.setType("send");
                messList.add(aMessage);
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
                messList.add(aMessage);
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
                if (content.isEmpty()) {
                    return;
                }
                txtInput.setText("");
                SingleMessage aMess = new SingleMessage(new Date(), content, sendAvartarLink);
                mData.child("Message").child(sendId).child(receiveId).child(Integer.toString(numberOfSend)).setValue(aMess);
            }
        });
    }
}
