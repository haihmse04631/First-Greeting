package com.example.haihm.firstgreeting.new_feed;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haihm.firstgreeting.R;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.haihm.firstgreeting.R;
import com.example.haihm.firstgreeting.video_call.VideoCallActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by DuyNguyen on 9/19/2017.
 */

public class CommentActivity extends AppCompatActivity {

    Intent intent;
    ListCommentAdapter commentAdapter;
    ListView lvCommentList;
    CommentList listComment;
    private DatabaseReference mDatabase;
    ListStatus userListNewsFeed;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_comment_alert);
        intent = getIntent();
        listComment = new CommentList();
        lvCommentList = findViewById(R.id.lvListComment);
        commentAdapter = new ListCommentAdapter(CommentActivity.this, R.layout.row_comment, listComment);
        lvCommentList.setAdapter(commentAdapter);
        ImageButton btnSendComment = findViewById(R.id.btnSendComment);

        userListNewsFeed = NewsFeedTab.listPost;
        Log.e("vi tri:", String.valueOf(ListStatusAdapter.positionStatus));
//        ImageView imgAvatarNewsFeed = (ImageView)findViewById(R.id.imgAvatarNewsFeed);
//        //Log.e("anh avatar", String.valueOf(userListNewsFeed.get(ListStatusAdapter.positionStatus)));
//        Picasso.with(getApplicationContext()).load(userListNewsFeed.get(ListStatusAdapter.positionStatus).getLinkAvatar()).into(imgAvatarNewsFeed);
//
//        TextView tvUserName = (TextView)findViewById(R.id.tvUserName);
//        tvUserName.setText(userListNewsFeed.get(ListStatusAdapter.positionStatus).getName());
//
//        TextView tvContentPost = (TextView)findViewById(R.id.tvContentPost);
//        tvContentPost.setText(userListNewsFeed.get(ListStatusAdapter.positionStatus).getContentPost());

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtComment = findViewById(R.id.edtComment);
                if(!edtComment.getText().equals("")){

                }
            }
        });
    }
}
