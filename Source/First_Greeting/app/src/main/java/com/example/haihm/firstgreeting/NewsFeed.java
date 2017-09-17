package com.example.haihm.firstgreeting;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Button btnComment;
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
    public static int numberOfPost = 0;
    public static String avatar, name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newsfeed_tab, container, false);

        avatar = getArguments().getString("fbImage");
        name = getArguments().getString("fbName");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lvListNewsFeed = (ListView) rootView.findViewById(R.id.lvNewsFeed);
        listPost = new UserListNewsFeed();
        thisContext = this.getContext();
        adapter = new ListNewsFeedAdapter(this.getContext(), R.layout.row_news_feed, listPost, lvListComment);
        lvListNewsFeed.setAdapter(adapter);

        adapterComment = new ArrayList<>();
        lvListComment = new ArrayList<>();
        commentList = new ArrayList<>();

        btnPostStatus = (Button) rootView.findViewById(R.id.btnPostStatus);


        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);


        btnPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = edtStatus.getText().toString().trim();

                if (status.equals("")) {
                    return;
                }
                UserStatus userStatus = new UserStatus(getArguments().getString("fbName"),
                        status, getArguments().getString("fbImage"));
                mDatabase.child("Status").child(Integer.toString(numberOfPost)).setValue(userStatus);

//                String commentPost =  tvContentComment.getText().toString().trim();
//                mDatabase.child("Comment").child(Integer.toString(numberOfPost)).setValue(commentPost);
            }
        });

        lvListNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout wrap_comment;
//                wrap_comment = (LinearLayout)adapterView.getItemAtPosition(i);
//                wrap_comment.findViewById(R.id.wrap_comment).setVisibility(View.VISIBLE);
                //  wrap_comment.setVisibility(View.VISIBLE);
            }
        });
        loadData();
        return rootView;
    }

    private void loadData() {
        mDatabase.child("Status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserStatus userNF = dataSnapshot.getValue(UserStatus.class);
                numberOfPost++;
//                Log.e("ghjkl", Integer.toString(numberOfPost));
                listPost.add(0, userNF);
                adapter.notifyDataSetChanged();


                final CommentList aCommentList = new CommentList();

                mDatabase.child("Comment").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Log.e("Dt", dataSnapshot.getValue().toString());
                        if (Integer.parseInt(dataSnapshot.getKey()) == numberOfPost) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            aCommentList.add(0, comment);
                            adapter.notifyDataSetChanged();
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