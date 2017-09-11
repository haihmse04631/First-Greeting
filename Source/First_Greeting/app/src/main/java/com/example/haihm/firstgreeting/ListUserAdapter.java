package com.example.haihm.firstgreeting;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by haihm on 8/11/2017.
 */

public class ListUserAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    UserList userList;
    int count;

    public ListUserAdapter(Context myContext, int myLayout, UserList userList) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userList = userList;
        this.count = 0;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
        TextView tvLastMessage;
        TextView tvCountMess;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(myLayout, null);
            holder.imgAvatar = (ImageView) rowView.findViewById(R.id.imageAvatar);
            holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
            holder.tvLastMessage = (TextView) rowView.findViewById(R.id.tvLastMessage);
            holder.tvCountMess = (TextView) rowView.findViewById(R.id.txtCountMess);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tvName.setText(userList.get(position).getName());
        holder.tvLastMessage.setText(userList.get(position).getLastMessage().get(userList.get(position).getId()).getContent());
        SingleMessage lasMess = userList.get(position).getLastMessage().get(userList.get(position).getId());


        if (lasMess.getStatus().equals("false") && lasMess.getType().equals("receive")) {
            if (holder.tvLastMessage.getTypeface() == Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)) {
                count ++;
                holder.tvCountMess.setVisibility(View.VISIBLE);
                holder.tvCountMess.setText(Integer.toString(count));
            } else {
                count = 1;
                holder.tvCountMess.setVisibility(View.VISIBLE);
                holder.tvCountMess.setText(Integer.toString(count));
            }
            holder.tvLastMessage.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {
            holder.tvLastMessage.setTypeface(null, Typeface.NORMAL);
        }
        if (lasMess.getStatus().equals("true") && lasMess.getType().equals("receive")) {
            holder.tvCountMess.setVisibility(View.INVISIBLE);
            holder.tvCountMess.setText("");
        }
        Picasso.with(myContext).load(userList.get(position).getLinkAvatar()).into(holder.imgAvatar);

        return rowView;
    }
}
