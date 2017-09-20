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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.R.attr.id;

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

        mDatabase.child("Status").child(Integer.toString(size - position)).child("likedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean liked = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.e("ID: ", data.getValue().toString());
                    if (data.getValue().equals(id)) {
                        Log.e("ID: ", "true");
                        liked = true;
                        break;
                    }
                }
                if (liked) {
                    holder.btnLike.setBackgroundResource(R.drawable.liked);
                } else {
                    holder.btnLike.setBackgroundResource(R.drawable.like);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Picasso.with(myContext).load(userListNewsFeed.get(position).getLinkAvatar()).into(holder.imgAvatarNewsFeed);
        holder.tvLiked.setText("Like: (" + userListNewsFeed.get(position).getLikedNumber() + ")");
        holder.tvCommented.setText("Comment: (" + userListNewsFeed.get(position).getCommentedNumber() + ")");

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                par.startIntent(position);
            }
        });


        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = par.getFbId();
                mDatabase.child("Status").child(Integer.toString(size - position)).child("likedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean liked = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getValue().equals(id)) {
                                liked = true;
                                break;
                            }
                        }
                        if (!liked) {
                            userStatus.setLikedNumber(userStatus.getLikedNumber() + 1);
                            mDatabase.child("Status").child(Integer.toString(size - position)).child("likedNumber").setValue(userStatus.getLikedNumber());
                            mDatabase.child("Status").child(Integer.toString(size - position)).child("likedUsers").child(Integer.toString(userStatus.getLikedNumber())).setValue(id);
                            holder.tvLiked.setText("Like: (" + Integer.toString(userStatus.getLikedNumber()) + ")");
                            holder.btnLike.setBackgroundResource(R.drawable.liked);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return rowView;
    }
}
