package com.example.haihm.firstgreeting.video_call;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 9/17/17.
 */

public class ListMemberAdapter extends BaseAdapter {
    Context MessContext;
    int layout;
    ArrayList<Member> roomList;

    public ListMemberAdapter(Context messContext, int layout, ArrayList<Member> roomList) {
        MessContext = messContext;
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

        LayoutInflater inflater = (LayoutInflater) MessContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        holder.tvRoomNumber = (TextView) view.findViewById(R.id.tvRoomNumber);
        holder.tvUser1 = (TextView) view.findViewById(R.id.tvUser1);
        holder.tvUser2 = (TextView) view.findViewById(R.id.tvUser2);
        holder.tvUser3 = (TextView) view.findViewById(R.id.tvUser3);
        holder.imgAvatarUser1 = (ImageView) view.findViewById(R.id.imgAvatarUser1);
        holder.imgAvatarUser2 = (ImageView) view.findViewById(R.id.imgAvatarUser2);
        holder.imgAvatarUser3 = (ImageView) view.findViewById(R.id.imgAvatarUser3);

        view.setTag(holder);

        holder.tvRoomNumber.setText("Room " + i);
        holder.tvUser1.setText(roomList.get(i).getMember1());
        holder.tvUser2.setText(roomList.get(i).getMember2());
        holder.tvUser3.setText(roomList.get(i).getMember3());
//        Picasso.with(MessContext).load(arrayMessage.get(i).getAvatarLink()).into(holderSend.imgAvatarSend);

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
