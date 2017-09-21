package com.example.haihm.firstgreeting.message;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by haihm on 8/11/2017.
 */

public class ListUserAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    UserList userList;
    HashMap<String, Integer> count;

    public ListUserAdapter(Context myContext, int myLayout, UserList userList, HashMap<String, Integer> count) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userList = userList;
        this.count = count;
    }

    public ListUserAdapter(Context myContext, int myLayout, UserList userList) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userList = userList;
        this.count = new HashMap<>();
        for (User user : userList) {
            count.put(user.getId(), 0);
        }
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
    public synchronized View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        rowView = inflater.inflate(myLayout, null);
        holder.imgAvatar = (ImageView) rowView.findViewById(R.id.imageAvatar);
        holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
        holder.tvLastMessage = (TextView) rowView.findViewById(R.id.tvLastMessage);
        holder.tvCountMess = (TextView) rowView.findViewById(R.id.txtCountMess);
        rowView.setTag(holder);
        holder = (ViewHolder) rowView.getTag();

        User friendUser = userList.get(position);
        String friendId = friendUser.getId();
        SingleMessage lasMess = friendUser.getLastMessage().get(friendId);

        holder.tvName.setText(friendUser.getName());
        holder.tvLastMessage.setText(lasMess.getContent());

        if (lasMess.getStatus().equals("true")) {
            holder.tvCountMess.setVisibility(View.INVISIBLE);
            holder.tvLastMessage.setTypeface(null, Typeface.NORMAL);
            count.put(friendId, 0);
        } else if (lasMess.getType().equals("receive")) {
            count.put(friendId, count.get(friendId) + 1);
            holder.tvLastMessage.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.tvCountMess.setText(Integer.toString(count.get(friendId)));
            holder.tvCountMess.setVisibility(View.VISIBLE);
        }

        Picasso.with(myContext).load(userList.get(position).getLinkAvatar()).into(holder.imgAvatar);


        return rowView;
    }
}
