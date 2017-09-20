package com.example.haihm.firstgreeting.new_feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.haihm.firstgreeting.R.drawable.like;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class ListStatusAdapter extends BaseAdapter {

    private NewsFeedTab par;
    private Context myContext;
    private int myLayout;
    private ListStatus userListNewsFeed;
    private DatabaseReference mDatabase;

    public ListStatusAdapter(NewsFeedTab par, Context myContext, int myLayout, ListStatus userListNewsFeed) {
        this.par = par;
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userListNewsFeed = userListNewsFeed;
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        ImageView btnComment;
        ImageView btnLike;
        TextView tvLiked;
        TextView tvCommented;
    }

    //  public static
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        rowView = inflater.inflate(myLayout, null);
        holder.imgAvatarNewsFeed = (ImageView) rowView.findViewById(R.id.imgAvatarNewsFeed);
        holder.tvUserName = (TextView) rowView.findViewById(R.id.tvUserName);
        holder.tvContentPost = (TextView) rowView.findViewById(R.id.tvContentPost);
        holder.tvLiked = (TextView) rowView.findViewById(R.id.tvNumberLike);
        holder.tvCommented = (TextView) rowView.findViewById(R.id.tvNumberComment);
        holder.btnLike = (ImageView) rowView.findViewById(R.id.btnLike);
        holder.btnComment = (ImageView) rowView.findViewById(R.id.btnComment);
        rowView.setTag(holder);

        //Set value
        final int size = userListNewsFeed.size() - 1;
        final Status userStatus = userListNewsFeed.get(position);
        holder.tvUserName.setText(userStatus.getName());
        holder.tvContentPost.setText(userListNewsFeed.get(position).getContentPost());
        boolean liked = false;
        if (userStatus.likedUsers == null) {
            userStatus.likedUsers = new ArrayList<String>();
        }
        for (String liker : userStatus.likedUsers) {
            if (liker.equals(par.getFbId())) {
                liked = true;
                break;
            }
        }
        if (liked) {
            holder.btnLike.setBackgroundResource(R.drawable.liked);
        } else {
            holder.btnLike.setBackgroundResource(like);
        }

        Picasso.with(myContext).load(userStatus.getLinkAvatar()).into(holder.imgAvatarNewsFeed);
        holder.tvLiked.setText("Like: (" + userStatus.getLikedNumber() + ")");
        holder.tvCommented.setText("Comment: (" + userStatus.getCommentedNumber() + ")");

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                par.startIntent(position);
            }
        });


        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean liked = false;
                for (String liker : userStatus.likedUsers) {
                    if (liker.equals(par.getFbId())) {
                        liked = true;
                        break;
                    }
                }
                if (!liked) {
                    userStatus.likedUsers.add(par.getFbId());
                    Log.e("Data: ", userStatus.toString());
                    userStatus.setLikedNumber(userStatus.likedUsers.size());
                    mDatabase.child("Status").child(Integer.toString(size - position)).child("likedNumber").setValue(userStatus.getLikedNumber());
                    mDatabase.child("Status").child(Integer.toString(size - position)).child("likedUsers").child(Integer.toString(userStatus.getLikedNumber() - 1)).setValue(par.getFbId());
                    holder.tvLiked.setText("Like: (" + Integer.toString(userStatus.getLikedNumber()) + ")");
                    holder.btnLike.setBackgroundResource(R.drawable.liked);
                }
            }
        });

        return rowView;
    }
}
