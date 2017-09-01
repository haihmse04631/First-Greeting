package com.example.haihm.firstgreeting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by haihm on 8/8/2017.
 */

public class Chat extends Fragment{

    private ListView lvListChat;
    private ArrayList<List_Chat> arrayListChat;
    private List_Chat_Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);

        arrayListChat = new ArrayList<>();

        arrayListChat.add(new List_Chat("Hoang Minh Hai", R.drawable.apple));

        adapter = new List_Chat_Adapter(this, R.layout.row_list_chat, arrayListChat);
        lvListChat.setAdapter(adapter);

        return rootView;
    }
}


