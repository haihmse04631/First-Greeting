package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by haihm on 8/8/2017.
 */

public class VideoCall extends Fragment {
    Button btnAttendance;
    DatabaseReference mData;
    Bundle bund;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_call_tab, container, false);

        mData = FirebaseDatabase.getInstance().getReference();
        btnAttendance = (Button) rootView.findViewById(R.id.btnAttendance);

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fbId = getArguments().getString("fbId");
                mData.child("User").child(fbId).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals("role")) {
                            Intent intent = new Intent(getActivity(), RoomVideoCall.class);
                            bund = new Bundle();
                            bund.putString("fbId", fbId);
                            String fbType = dataSnapshot.getValue().toString();
                            bund.putString("fbType", fbType);
                            intent.putExtra("UserInfo", bund);
                            startActivity(intent);
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
        });


        return rootView;

    }


}

