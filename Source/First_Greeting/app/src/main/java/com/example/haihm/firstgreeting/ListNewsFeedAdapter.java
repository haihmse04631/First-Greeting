package com.example.haihm.firstgreeting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class ListNewsFeedAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    UserListNewsFeed userListNewsFeed;
    UserStatus userStatus;
    ArrayList<ListView> lvListComment;
    private Button btnComment;
    private TextView tvContentComment;
    private DatabaseReference mDatabase;
    private LinearLayout wrap_comment;
    public ListNewsFeedAdapter(Context myContext, int myLayout, UserListNewsFeed userListNewsFeed,
                               ArrayList<ListView> lvListComment) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userListNewsFeed = userListNewsFeed;
        this.lvListComment = lvListComment;
    }

    public ListNewsFeedAdapter() {
    }

    @Override
    public int getCount() {
        return userListNewsFeed.size();
    }

    @Override
    public Object getItem(int i) {
        return userListNewsFeed.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageButton imgAvatarNewsFeed;
        TextView tvUserName;
        TextView tvContentPost;
        ListView lvListComment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        rowView = inflater.inflate(myLayout, null);
        holder.imgAvatarNewsFeed = (ImageButton) rowView.findViewById(R.id.imgAvatarNewsFeed);
        holder.tvUserName = (TextView) rowView.findViewById(R.id.tvUserName);
        holder.tvContentPost = (TextView) rowView.findViewById(R.id.tvContentPost);

        rowView.setTag(holder);
        userStatus = userListNewsFeed.get(position);
        Log.e("data adapter", userStatus.getName());
        holder.tvUserName.setText(userStatus.getName());
        holder.tvContentPost.setText(userListNewsFeed.get(position).getContentPost());
        Picasso.with(myContext).load(userListNewsFeed.get(position).getLinkAvatar()).into(holder.imgAvatarNewsFeed);

        final CommentList aCommentList = new CommentList();

        ListView aLvComment = (ListView) rowView.findViewById(R.id.lvListComment);

        ListCommentAdapter aCommentAdapter = new ListCommentAdapter(myContext, R.layout.row_comment, aCommentList);
        aLvComment.setAdapter(aCommentAdapter);

        holder.lvListComment = aLvComment;

        return rowView;
    }
}
