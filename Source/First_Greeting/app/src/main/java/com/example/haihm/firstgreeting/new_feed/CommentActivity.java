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
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

/**
 * Created by DuyNguyen on 9/19/2017.
 */

public class CommentActivity extends AppCompatActivity {

    Intent intent;
    ListCommentAdapter commentAdapter;
    ListView lvCommentList;
    CommentList listComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_alert);
        intent = getIntent();
        listComment = new CommentList();
        lvCommentList = findViewById(R.id.lvListComment);
        commentAdapter = new ListCommentAdapter(CommentActivity.this, R.layout.row_comment, listComment);
        lvCommentList.setAdapter(commentAdapter);
    }
}
