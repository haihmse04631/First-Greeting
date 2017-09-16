package com.example.haihm.firstgreeting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by DuyNguyen on 9/17/2017.
 */

public class ListCommentAdapter extends BaseAdapter{

    Context myContext;
    int myLayout;
    CommentList commentList;

    public ListCommentAdapter(Context myContext, int myLayout, CommentList commentList) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.commentList = commentList;

    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imgAvatarComment;
        TextView tvComment;
    }
    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(myLayout, null);
        holder.imgAvatarComment = (ImageView) rowView.findViewById(R.id.imgAvatarComment);
        holder.tvComment = (TextView) rowView.findViewById(R.id.tvComment);
        rowView.setTag(holder);
        holder.tvComment.setText(commentList.get(position).getCommentUser());
        Picasso.with(myContext).load(commentList.get(position).getLinkAvatar()).into(holder.imgAvatarComment);
        return rowView;
    }
}
