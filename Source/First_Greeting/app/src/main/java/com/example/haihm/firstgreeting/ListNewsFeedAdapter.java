package com.example.haihm.firstgreeting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class ListNewsFeedAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    UserListNewsFeed userListNewsFeed;
    UserStatus userStatus;

    public ListNewsFeedAdapter(Context myContext, int myLayout, UserListNewsFeed userListNewsFeed) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userListNewsFeed = userListNewsFeed;
//                new UserListNewsFeed();
//        for (int i = userListNewsFeed.size()-1; i>=0; i--){
//            this.userListNewsFeed.add(userListNewsFeed.get(i));
//        }
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
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
        return rowView;
    }
}
