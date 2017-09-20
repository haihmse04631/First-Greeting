package com.example.haihm.firstgreeting.new_feed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by DuyNguyen on 9/19/2017.
 */

public class CommentActivity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;

    ListCommentAdapter commentAdapter;
    ListView lvCommentList;
    CommentList listComment;
    private DatabaseReference mDatabase;

    public ImageView imgAvatarNewsFeed;
    public TextView tvContentPost;
    public TextView tvUserName;
    EditText edtComment;

    private int numberOfComment = 0;
    private int postIndex;
    private int postSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_comment_alert);

        showData();

        listComment = new CommentList();
        lvCommentList = findViewById(R.id.lvListComment);
        commentAdapter = new ListCommentAdapter(CommentActivity.this, R.layout.row_comment, listComment);
        lvCommentList.setAdapter(commentAdapter);

        ImageButton btnSendComment = findViewById(R.id.btnSendComment);

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentComment = edtComment.getText().toString().trim();
                if (!contentComment.isEmpty()) {
                    Comment aComment = new Comment(bundle.getString("fbImg"), bundle.getString("fbName"), contentComment);
                    mDatabase.child("Comment").child(Integer.toString(postSize - postIndex - 1)).child(Integer.toString(numberOfComment)).setValue(aComment);
                    edtComment.setText("");
                }
            }
        });
    }

    public void showData() {
        intent = getIntent();
        bundle = intent.getBundleExtra("MyPackage");
        imgAvatarNewsFeed = (ImageView) findViewById(R.id.imgAvatarNewsFeed);
        Picasso.with(getApplicationContext()).load(bundle.getString("fbImg")).into(imgAvatarNewsFeed);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(bundle.getString("fbName"));
        tvContentPost = (TextView) findViewById(R.id.tvContentPost);
        tvContentPost.setText(bundle.getString("contentPost"));

        postIndex = bundle.getInt("postIndex");
        postSize = bundle.getInt("postSize");
        edtComment = findViewById(R.id.edtComment);

        loadComment();
    }

    public void loadComment() {
        mDatabase.child("Comment").child(Integer.toString(postSize - postIndex - 1)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                numberOfComment++;
                listComment.add(0, comment);
                commentAdapter.notifyDataSetChanged();
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
    public void onBackPressed() {
        Intent data = new Intent();
        Bundle bund = new Bundle();
        bund.putInt("Position", postIndex);
        bund.putInt("Comment", numberOfComment);
        data.putExtra("ReturnPackage", bund);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
