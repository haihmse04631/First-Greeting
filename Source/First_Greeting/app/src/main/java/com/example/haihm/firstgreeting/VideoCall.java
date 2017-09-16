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
    private int joinState = 0;
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
        fbName = getArguments().getString("fbName");
        btnAttendance = (Button) rootView.findViewById(R.id.btnAttendance);
        btnAttend = (ImageButton) rootView.findViewById(R.id.btnAttend);
        btnLeave = (ImageButton) rootView.findViewById(R.id.btnLeave);
        btnReturnToCall = (ImageButton) rootView.findViewById(R.id.btnRetunToCall);

        try {
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {
        }
        mSocket.connect();
        mSocket.emit("client-send", "Successful!");

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joinState == 0) {
                    intent = new Intent(getActivity(), RoomVideoCall.class);
                    bund = new Bundle();
                    bund.putString("fbId", fbId);
                    bund.putString("name", fbName);
                    // Get from firebase
                    mData.child("User").child(fbId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals("role")) {
                                fbType = dataSnapshot.getValue().toString();
                                bund.putString("role", fbType);
                                intent.putExtra("UserInfo", bund);
                                startActivityForResult(intent, 0);
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

                } else if (joinState == 1) {
                    mSocket.emit("cancel-attendant", "");
                    joinState = 0;
                    btnAttendance.setText("Báo Danh");
                } else {
                    RoomVideoCall.mPublisher.destroy();
                    if (RoomVideoCall.mSubscriber1 != null) {
                        RoomVideoCall.mSubscriber1.destroy();
                    }
                    if (RoomVideoCall.mSubscriber2 != null) {
                        RoomVideoCall.mSubscriber2.destroy();
                    }
                    joinState = 0;
                    btnAttendance.setText("Báo Danh");
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
                    Toast.makeText(getApplicationContext(), "joinState", Toast.LENGTH_LONG).show();
                    joinState = 2;
                    btnAttendance.setText("Dừng Cuộc ");
                } else {
                    Toast.makeText(getApplicationContext(), "not yet", Toast.LENGTH_LONG).show();
                    joinState = 1;
                    btnAttendance.setText("Huỷ Báo Danh");
                }
        }
    }

}

