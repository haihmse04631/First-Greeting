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
 * Created by haihm on 9/6/2017.
 */

public class ListSendMessageAdapter extends BaseAdapter {
    Context SendMessContext;
    int SendMessLayout;
    List<SendMessage> arraySendMessage;

    public ListSendMessageAdapter(Context sendMessContext, int sendMessLayout, List<SendMessage> arraySendMessage) {
        SendMessContext = sendMessContext;
        SendMessLayout = sendMessLayout;
        this.arraySendMessage = arraySendMessage;
    }

    @Override
    public int getCount() {
        return arraySendMessage.size();
    }

    @Override
    public Object getItem(int i) {
        return arraySendMessage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView txtMessageSend;
        ImageView imgAvatarSend;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) SendMessContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;

        if(rowView == null){

        }else{

        }
        holder.txtMessageSend.setText(arraySendMessage.get(i).getMessageSend());
        Picasso.with(SendMessContext).load(arraySendMessage.get(i).getLinkAvatar()).into(holder.imgAvatarSend);


        return rowView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }


}
