package com.example.haihm.firstgreeting.video_call;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.haihm.firstgreeting.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by haihm on 8/8/2017.
 */

public class VideoCallTab extends Fragment {
    //    private Button btnAttendance;
    private DatabaseReference mData;
    private Bundle bund;
    private static int attendedState = 0;
    protected static Socket mSocket;
    private String fbId;
    private String fbType;
    private String fbName;
    private String fbImg;
    private ImageButton btnAttend, btnLeave;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_call_tab, container, false);

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.video_call_tab, container, false);
        } catch (InflateException e) {

        }

        mData = FirebaseDatabase.getInstance().getReference();
        fbId = getArguments().getString("fbId");
        fbName = getArguments().getString("fbName");
        fbImg = getArguments().getString("fbImage");
        btnAttend = (ImageButton) rootView.findViewById(R.id.btnAttend);
        btnLeave = (ImageButton) rootView.findViewById(R.id.btnLeave);

        try {
            if (mSocket == null) {

                mSocket = IO.socket("https://firstgreeting.herokuapp.com/");

                mSocket.connect();
            }
        } catch (URISyntaxException e) {
        }

        mSocket.emit("client-send", "Successful!");

        btnAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendedState == 0) {
                    intent = new Intent(getActivity(), VideoCallActivity.class);
                    bund = new Bundle();
                    bund.putString("fbId", fbId);
                    bund.putString("name", fbName);
                    bund.putString("fbImage", fbImg);
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

                } else if (attendedState == 1) {
                    startActivityForResult(intent, 0);
                } else {
                    startActivityForResult(intent, 0);
                }

            }
        });
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendedState == 1) {
                    mSocket.emit("cancel-attendant", fbId);
                    attendedState = 0;
                    btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.attend));
                    btnLeave.setVisibility(View.GONE);
                }
                if (attendedState == 2) {
                    mSocket.emit("cancel-attendant", fbId);

                    if (VideoCallActivity.mSubscriber1 != null) {
                        VideoCallActivity.mSubscriber1.destroy();
                        VideoCallActivity.mSubscriber1 = null;
                    }
                    if (VideoCallActivity.mSubscriber2 != null) {
                        VideoCallActivity.mSubscriber2.destroy();
                        VideoCallActivity.mSubscriber2 = null;
                    }

                    VideoCallActivity.mSession.disconnect();
                    VideoCallActivity.mSession = null;
                    attendedState = 0;
                    btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.attend));
                    btnLeave.setVisibility(View.GONE);
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
                    attendedState = 2;
                    btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.back_room));
                    btnLeave.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.leave));
                    btnLeave.setVisibility(View.VISIBLE);
                } else {
                    attendedState = 1;
                    btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.attended));
                    btnLeave.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.stop_attend));
                    btnLeave.setVisibility(View.VISIBLE);
                }
        }
    }

}

