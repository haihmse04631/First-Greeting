package com.example.haihm.firstgreeting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by haihm on 8/8/2017.
 */

public class Chat extends Fragment {
    private ListView lvListChat;
    private ArrayList<List_Chat> arrayListChat;
    private List_Chat_Adapter adapter;
    DatabaseReference mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        mData = FirebaseDatabase.getInstance().getReference();
        arrayListChat = new ArrayList<>();

        //arrayListChat.add(new List_Chat("Hoang Minh Hai", R.drawable.apple));
       // loadData();
       // adapter = new List_Chat_Adapter(this, R.layout.row_list_chat, arrayListChat);

        lvListChat.setAdapter(adapter);

        return rootView;
    }

    private void loadData(){
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List_Chat listChat = dataSnapshot.getValue(List_Chat.class);
                arrayListChat.add(new List_Chat(listChat.name, listChat.linkAvatar));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


