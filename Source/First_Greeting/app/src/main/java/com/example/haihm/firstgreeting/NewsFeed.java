package com.example.haihm.firstgreeting;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by haihm on 8/8/2017.
 */

public class NewsFeed extends Fragment {
    private Button btnPostStatus;
    private EditText edtStatus;
    private TextView tvContentComment;
    private ListNewsFeedAdapter adapter;
    private ArrayList<ListCommentAdapter> adapterComment;
    // private UserStatus userNewsFeed;
    private ListView lvListNewsFeed;
    private ArrayList<ListView> lvListComment;
    private UserListNewsFeed listPost;
    private ArrayList<CommentList> commentList;

    private DatabaseReference mDatabase;
    View rootView;
    Context thisContext;

    // private FirebaseAuth firebaseAuth;
    Bundle bundle;
    int numberOfPost=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newsfeed_tab, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lvListNewsFeed = (ListView) rootView.findViewById(R.id.lvNewsFeed);
        listPost = new UserListNewsFeed();
        thisContext = this.getContext();
        adapter = new ListNewsFeedAdapter(this.getContext(), R.layout.row_news_feed, listPost, lvListComment);
        lvListNewsFeed.setAdapter(adapter);

        adapterComment = new ArrayList<>();
        lvListComment = new ArrayList<>();
        commentList = new ArrayList<>();

        btnPostStatus  = (Button) rootView.findViewById(R.id.btnPostStatus);
        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);
        tvContentComment = (TextView) rootView.findViewById(R.id.tvContentPost);

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

                String commentPost =  tvContentComment.getText().toString().trim();
                mDatabase.child("Comment").child(Integer.toString(numberOfPost)).setValue(commentPost);
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
                listPost.add(0, userNF);
                adapter.notifyDataSetChanged();

                ListView aLvComment = (ListView) rootView.findViewById(R.id.lvListComment);
                CommentList aCommentList = new CommentList();
                ListCommentAdapter aCommentAdapter = new ListCommentAdapter(thisContext, R.layout.row_comment, aCommentList);
//                aLvComment.setAdapter(aCommentAdapter);
                mDatabase.child("Comment").child(Integer.toString(numberOfPost)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


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
