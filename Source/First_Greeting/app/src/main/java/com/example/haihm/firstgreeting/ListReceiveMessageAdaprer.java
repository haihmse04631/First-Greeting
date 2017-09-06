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

public class ListReceiveMessageAdaprer extends BaseAdapter {
    Context ReceiveMessContext;
    int ReceiveMessLayout;
    List<ReceiveMessage> arrayReceiveMessage;

    public ListReceiveMessageAdaprer(Context receiveMessContext, int receiveMessLayout, List<ReceiveMessage> arrayReceiveMessage) {
        ReceiveMessContext = receiveMessContext;
        ReceiveMessLayout = receiveMessLayout;
        this.arrayReceiveMessage = arrayReceiveMessage;
    }

    @Override
    public int getCount() {
        return arrayReceiveMessage.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayReceiveMessage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView txtMessageReceive;
        ImageView imgAvatarReceive;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) ReceiveMessContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;

        if(rowView == null){

        }else{

        }
        holder.txtMessageReceive.setText(arrayReceiveMessage.get(i).getMessageReceive());
        Picasso.with(ReceiveMessContext).load(arrayReceiveMessage.get(i).getLinkAvatar()).into(holder.imgAvatarReceive);


        return rowView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}
