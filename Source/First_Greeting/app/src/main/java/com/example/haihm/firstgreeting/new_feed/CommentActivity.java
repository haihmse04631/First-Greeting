package com.example.haihm.firstgreeting.new_feed;

import android.content.Context;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_comment_alert);

        showNameAndStatus();

        listComment = new CommentList();
        lvCommentList = findViewById(R.id.lvListComment);
        commentAdapter = new ListCommentAdapter(CommentActivity.this, R.layout.row_comment, listComment);
        lvCommentList.setAdapter(commentAdapter);

        ImageButton btnSendComment = findViewById(R.id.btnSendComment);

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtComment = findViewById(R.id.edtComment);
                if (!edtComment.getText().equals("")) {
                    //mDatabase.child("Comment").child(Integer.toString(numberOfPost)).setValue(edtComment.getText());
                }
            }
        });
    }

    public void showNameAndStatus() {
        intent = getIntent();
        bundle = intent.getBundleExtra("MyPackage");
        imgAvatarNewsFeed = (ImageView) findViewById(R.id.imgAvatarNewsFeed);
        Picasso.with(getApplicationContext()).load(bundle.getString("fbImg")).into(imgAvatarNewsFeed);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(bundle.getString("fbName"));
        tvContentPost = (TextView) findViewById(R.id.tvContentPost);
        tvContentPost.setText(bundle.getString("contentPost"));
    }
}
