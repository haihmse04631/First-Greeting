package com.example.haihm.firstgreeting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by haihm on 8/8/2017.
 */

public class Chat extends Fragment {
    private ListView lvListChat;
    private ArrayList<User> arrayListChat;
    private ListUserAdapter adapter;
    private UserList userList;
    private String fbId;
    DatabaseReference mData;
    Bundle bundle;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        fbId = getArguments().getString("fbId");
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        mData = FirebaseDatabase.getInstance().getReference();
        arrayListChat = new ArrayList<>();

        //arrayListChat.add(new User("Hoang Minh Hai", R.drawable.apple));
        loadData();
        adapter = new ListUserAdapter(this.getContext(), R.layout.row_list_chat, arrayListChat);
        userList = new UserList();
        lvListChat.setAdapter(adapter);

        lvListChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                System.out.println(user.getName());
                Log.e("data", user.getName());
            }
        });

        //Load data from Firebase
        loadData();

        return rootView;
    }

    private void loadData() {
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                String id = (String) dataSnapshot.getKey();
                if (id.equals(fbId)) {
                    return;
                }
                String linkAvatar = (String) dataSnapshot.child("linkAvatar").getValue();
                String name = (String) dataSnapshot.child("name").getValue();
                String role = (String) dataSnapshot.child("role").getValue();
                Message message = new Message();

                Iterator iterator1 = dataSnapshot.child("message").getChildren().iterator();
                while (iterator1.hasNext()) {
                    DataSnapshot item1 = (DataSnapshot) iterator1.next();
                    ArrayList<SingleMessage> list = new ArrayList<SingleMessage>();
                    Iterator iterator2 = dataSnapshot.child("message").child(item1.getKey()).getChildren().iterator();
                    while (iterator2.hasNext()) {
                        DataSnapshot item2 = (DataSnapshot) iterator2.next();
                        SingleMessage aMessage = item2.getValue(SingleMessage.class);
                        list.add(aMessage);
                    }
                    message.put(item1.getKey(), list);
                }

                User user = new User(name, linkAvatar, id, message, role);
                userList.add(user);
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


