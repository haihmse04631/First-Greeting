package com.example.haihm.firstgreeting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by haihm on 8/11/2017.
 */

public class List_Chat_Adapter extends BaseAdapter {

    private Chat context;
    private int layout;
    private List<List_Chat> list_chatList;

    public List_Chat_Adapter(Chat context, int layout, List<List_Chat> list_chatList) {
        this.context = context;
        this.layout = layout;
        this.list_chatList = list_chatList;
    }

    @Override
    public int getCount() {
        return list_chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgAvatar;
        TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater(context.getArguments());

            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            //ánh xạ convertView

            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.imageAvatar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        List_Chat listChat = list_chatList.get(position);

        holder.tvName.setText(listChat.getName());
        holder.imgAvatar.setImageResource(listChat.getAvatar());

        return convertView;
    }
}
