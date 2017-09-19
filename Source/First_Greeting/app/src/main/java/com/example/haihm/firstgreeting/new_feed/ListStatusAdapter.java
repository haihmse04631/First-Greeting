package com.example.haihm.firstgreeting.new_feed;

import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class ListStatusAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    ListStatus userListNewsFeed;
    Status userStatus;
    ArrayList<ListView> lvListComment;
    private Button btnComment;
    private TextView tvContentComment;
    private DatabaseReference mDatabase;
    private LinearLayout wrap_comment;
    public ListStatusAdapter(Context myContext, int myLayout, ListStatus userListNewsFeed,
                             ArrayList<ListView> lvListComment) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userListNewsFeed = userListNewsFeed;
        this.lvListComment = lvListComment;
    }

    public ListStatusAdapter() {
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
        ImageView imgAvatarNewsFeed;
        TextView tvUserName;
        TextView tvContentPost;
        ListView lvListComment;
        Button btnComment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        rowView = inflater.inflate(myLayout, null);
        holder.imgAvatarNewsFeed = (ImageView) rowView.findViewById(R.id.imgAvatarNewsFeed);
        holder.tvUserName = (TextView) rowView.findViewById(R.id.tvUserName);
        holder.tvContentPost = (TextView) rowView.findViewById(R.id.tvContentPost);

        rowView.setTag(holder);
        userStatus = userListNewsFeed.get(position);
//        Log.e("data adapter", userStatus.getName());
        holder.tvUserName.setText(userStatus.getName());
        holder.tvContentPost.setText(userListNewsFeed.get(position).getContentPost());
        Picasso.with(myContext).load(userListNewsFeed.get(position).getLinkAvatar()).into(holder.imgAvatarNewsFeed);

        final CommentList aCommentList = new CommentList();
        holder.btnComment = (Button) rowView.findViewById(R.id.btnComment);
        final NewsFeedTab newsFeedTab = new NewsFeedTab();
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                newsFeedTab.actionDialog();
                CommentActivity commentActivity = new CommentActivity();
                //commentActivity.showDialog();
             //   commentActivity.showDialog();
            }
        });


        return rowView;
    }
}
