package com.example.haihm.firstgreeting.new_feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by haihm on 8/8/2017.
 */

public class NewsFeedTab extends Fragment {
    private Button btnPostStatus;
    private EditText edtStatus;
    private TextView tvContentComment;
    private ListStatusAdapter adapter;
    private ArrayList<ListCommentAdapter> adapterComment;
    private ListView lvListNewsFeed;
    private ListStatus listPost;

    private DatabaseReference mDatabase;

    private int numberOfPost = 0;
    private String fbId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsfeed_tab, container, false);

        fbId = getArguments().getString("fbId");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lvListNewsFeed = (ListView) rootView.findViewById(R.id.lvNewsFeed);
        listPost = new ListStatus();
        adapter = new ListStatusAdapter(this, getContext(), R.layout.row_news_feed, listPost);
        lvListNewsFeed.setAdapter(adapter);

        btnPostStatus = (Button) rootView.findViewById(R.id.btnPostStatus);
        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);

        btnPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = edtStatus.getText().toString().trim();
                if (status.equals("")) {
                    return;
                }
                Status userStatus = new Status(getArguments().getString("fbName"),
                        status, getArguments().getString("fbImage"));
                mDatabase.child("Status").child(Integer.toString(numberOfPost)).setValue(userStatus);
                edtStatus.setText("");
//                String commentPost =  tvContentComment.getText().toString().trim();
//                mDatabase.child("Comment").child(Integer.toString(numberOfPost)).setValue(commentPost);
            }
        });

        lvListNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startIntent(i);
            }
        });

        loadData();

        return rootView;
    }

    public void startIntent(int position) {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("postIndex", position);
        bundle.putInt("postSize", listPost.size());
        bundle.putString("fbName", getArguments().getString("fbName"));
        bundle.putString("fbImg", getArguments().getString("fbImage"));
        bundle.putString("contentPost", listPost.get(position).getContentPost());
        intent.putExtra("MyPackage", bundle);
        startActivityForResult(intent, 0);
    }

    public String getFbId() {
        return fbId;
    }

    private void loadData() {
        mDatabase.child("Status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Status post = dataSnapshot.getValue(Status.class);
                numberOfPost++;
                listPost.add(0, post);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                Bundle bund = data.getBundleExtra("ReturnPackage");
                int position = bund.getInt("Position");
                int numberComment = bund.getInt("Comment");
                listPost.get(position).setCommentedNumber(numberComment);
                adapter.notifyDataSetChanged();
                mDatabase.child("Status").child(Integer.toString(listPost.size() - position - 1)).child("commentedNumber").setValue(numberComment);
        }
    }
}
