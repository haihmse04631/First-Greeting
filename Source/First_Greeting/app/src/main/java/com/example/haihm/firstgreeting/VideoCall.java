package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by haihm on 8/8/2017.
 */

public class VideoCall extends Fragment {
    private Button btnAttendance;
    private DatabaseReference mData;
    private Bundle bund;
    private int joined = 0;
    protected static Socket mSocket;
    private String fbId;
    private String fbType;
    private String fbName;
    private ImageButton btnAttend, btnLeave, btnReturnToCall;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_call_tab, container, false);

        mData = FirebaseDatabase.getInstance().getReference();
        fbId = getArguments().getString("fbId");
        btnAttendance = (Button) rootView.findViewById(R.id.btnAttendance);
        btnAttend = (ImageButton) rootView.findViewById(R.id.btnAttend);
        btnLeave = (ImageButton) rootView.findViewById(R.id.btnLeave);
        btnReturnToCall = (ImageButton) rootView.findViewById(R.id.btnRetunToCall);

        try {
            mSocket = IO.socket("http://192.168.1.157:3000");
        } catch (URISyntaxException e) {
        }
        mSocket.connect();
        mSocket.emit("client-send", "Successful!");

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joined == 0) {
                    intent = new Intent(getActivity(), RoomVideoCall.class);
                    bund = new Bundle();
                    bund.putString("fbId", fbId);
                    // Get from firebase
                    mData.child("User").child(fbId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals("role")) {
                                fbType = dataSnapshot.getValue().toString();
                                bund.putString("role", fbType);
                            }
                            if (dataSnapshot.getKey().equals("name")) {
                                fbName = dataSnapshot.getValue().toString();
                                bund.putString("name", fbName);
                            }
                            intent.putExtra("UserInfo", bund);
                            startActivityForResult(intent, 0);
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

                } else {
                    RoomVideoCall.mPublisher.destroy();
                    if (RoomVideoCall.mSubscriber1 != null) {
                        RoomVideoCall.mSubscriber1.destroy();
                    }
                    if (RoomVideoCall.mSubscriber2 != null) {
                        RoomVideoCall.mSubscriber2.destroy();
                    }
                    joined = 0;
                    btnAttendance.setText("Attend");
                }

            }
        });
        return rootView;

    }

    /* Called when the second activity's finished */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
//                    String state = data.getStringExtra("state");
                    Toast.makeText(getApplicationContext(), "joined", Toast.LENGTH_LONG).show();
                    joined = 1;
                    btnAttendance.setText("Leave");
                } else {
                    Toast.makeText(getApplicationContext(), "not yet", Toast.LENGTH_LONG).show();
                }
        }
    }

}

