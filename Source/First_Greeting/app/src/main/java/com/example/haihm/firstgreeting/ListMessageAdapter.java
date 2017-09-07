package com.example.haihm.firstgreeting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HongSonPham on 9/7/17.
 */

public class ListMessageAdapter extends BaseAdapter {
    Context MessContext;
    int SendLayout;
    int ReceiveLayout;
    List<SingleMessage> arrayMessage;

    public ListMessageAdapter(Context messContext, int sendLayout, int receiveLayout, List<SingleMessage> arrayMessage) {
        MessContext = messContext;
        SendLayout = sendLayout;
        ReceiveLayout = receiveLayout;
        this.arrayMessage = arrayMessage;
    }

    @Override
    public int getCount() {
        return arrayMessage.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayMessage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderSend holderSend = new ViewHolderSend();
        ViewHolderReceive holderReceive = new ViewHolderReceive();

        LayoutInflater inflater = (LayoutInflater) MessContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;


        if (arrayMessage.get(i).getType().equals("send")) {
            if (rowView == null) {
                rowView = inflater.inflate(SendLayout, null);
                holderSend.imgAvatarSend = (ImageView) rowView.findViewById(R.id.avatarSend);
                holderSend.txtMessageSend = (TextView) rowView.findViewById(R.id.txtMessageSend);
                rowView.setTag(holderSend);

                //convertView.setTag(holder);
            } else {
                holderSend = (ViewHolderSend) rowView.getTag();
                //holder = (ViewHolder) convertView.getTag();
            }
            holderSend.txtMessageSend.setText(arrayMessage.get(i).getContent());
            Picasso.with(MessContext).load(arrayMessage.get(i).getAvatarLink()).into(holderSend.imgAvatarSend);

        } else if (arrayMessage.get(i).getType().equals("receive")) {
            if (rowView == null) {
                rowView = inflater.inflate(ReceiveLayout, null);
                holderReceive.imgAvatarReceive = (ImageView) rowView.findViewById(R.id.avatarReceive);
                holderReceive.txtMessageReceive = (TextView) rowView.findViewById(R.id.txtMessageReceive);
                rowView.setTag(holderReceive);

                //convertView.setTag(holder);
            } else {
                holderReceive = (ViewHolderReceive) rowView.getTag();
                //holder = (ViewHolder) convertView.getTag();
            }
            holderReceive.txtMessageReceive.setText(arrayMessage.get(i).getContent());
            Picasso.with(MessContext).load(arrayMessage.get(i).getAvatarLink()).into(holderReceive.imgAvatarReceive);
        }


        return rowView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    private class ViewHolderReceive {
        TextView txtMessageReceive;
        ImageView imgAvatarReceive;
    }

    private class ViewHolderSend {
        TextView txtMessageSend;
        ImageView imgAvatarSend;
    }
}
