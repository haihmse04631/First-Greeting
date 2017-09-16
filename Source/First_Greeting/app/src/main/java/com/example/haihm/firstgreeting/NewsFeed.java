package com.example.haihm.firstgreeting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by haihm on 8/8/2017.
 */

public class NewsFeed extends Fragment {
    private Button btnPostStatus;
    private EditText edtStatus;
    private ListNewsFeedAdapter adapter;
    // private UserStatus userNewsFeed;
    private ListView lvListNewsFeed;
    private UserListNewsFeed userListNewsFeed;
    private DatabaseReference mDatabase;
    // private FirebaseAuth firebaseAuth;
    Bundle bundle;
    int numberOfPost=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsfeed_tab, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lvListNewsFeed = (ListView) rootView.findViewById(R.id.lvNewsFeed);
        userListNewsFeed = new UserListNewsFeed();
        adapter = new ListNewsFeedAdapter(this.getContext(), R.layout.row_news_feed, userListNewsFeed);
        lvListNewsFeed.setAdapter(adapter);

        btnPostStatus  = (Button) rootView.findViewById(R.id.btnPostStatus);
        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);


        btnPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = edtStatus.getText().toString().trim();

                if(status.equals("")){
                    return;
                }
                UserStatus userStatus = new UserStatus(getArguments().getString("fbName"),
                        status, getArguments().getString("fbImage"));
                mDatabase.child("Status").child(Integer.toString(numberOfPost)).setValue(userStatus);
                mDatabase.child("Comment").child(Integer.toString(numberOfPost)).setValue(new Comment());
            }
        });
        loadData();
        return rootView;
    }
    private void loadData(){
        mDatabase.child("Status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserStatus userNF = dataSnapshot.getValue(UserStatus.class);
                numberOfPost++;
                userListNewsFeed.add(0, userNF);
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

        mDatabase.child("Comment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);

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
}
