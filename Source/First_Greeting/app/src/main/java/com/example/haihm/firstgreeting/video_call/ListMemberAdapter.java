package com.example.haihm.firstgreeting.video_call;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 9/17/17.
 */

public class ListMemberAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Room> roomList;

    public ListMemberAdapter(Context context, int layout, ArrayList<Room> roomList) {
        context = context;
        this.layout = layout;
        this.roomList = roomList;
    }


    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return roomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        holder.tvRoomNumber = (TextView) view.findViewById(R.id.tvRoomNumber);
        holder.tvUser1 = (TextView) view.findViewById(R.id.tvUser1);
        holder.tvUser2 = (TextView) view.findViewById(R.id.tvUser2);
        holder.tvUser3 = (TextView) view.findViewById(R.id.tvUser3);
        holder.imgAvatarUser1 = (ImageView) view.findViewById(R.id.imgAvatarUser1);
        holder.imgAvatarUser2 = (ImageView) view.findViewById(R.id.imgAvatarUser2);
        holder.imgAvatarUser3 = (ImageView) view.findViewById(R.id.imgAvatarUser3);

        view.setTag(holder);

        holder.tvRoomNumber.setText("Room " + (i + 1));
        holder.tvUser1.setText(roomList.get(i).getName1());
        holder.tvUser2.setText(roomList.get(i).getName2());
        holder.tvUser3.setText(roomList.get(i).getName3());
        if (!roomList.get(i).getImg1().isEmpty())
            Picasso.with(context).load(roomList.get(i).getImg1()).into(holder.imgAvatarUser1);
        if (!roomList.get(i).getImg2().isEmpty())
            Picasso.with(context).load(roomList.get(i).getImg2()).into(holder.imgAvatarUser2);
        if (!roomList.get(i).getImg3().isEmpty())
            Picasso.with(context).load(roomList.get(i).getImg3()).into(holder.imgAvatarUser3);

        return view;
    }

    class ViewHolder {
        TextView tvRoomNumber;
        ImageView imgAvatarUser1;
        TextView tvUser1;
        ImageView imgAvatarUser2;
        TextView tvUser2;
        ImageView imgAvatarUser3;
        TextView tvUser3;
    }
}
