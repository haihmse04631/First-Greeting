package com.example.haihm.firstgreeting;


import android.content.Intent;
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
    private ListUserAdapter adapter;
    private UserList userList;
    private String fbId;
    DatabaseReference mData;
    Bundle bundle;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        mData = FirebaseDatabase.getInstance().getReference();

        fbId = getArguments().getString("fbId");
        userList = new UserList();
        adapter = new ListUserAdapter(this.getContext(), R.layout.row_list_chat, userList);
        lvListChat.setAdapter(adapter);

        lvListChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), MessageForm.class);
                bundle = new Bundle();
                bundle.putString("fbSendId", fbId);
                bundle.putString("fbReceiveId", user.getId());
                intent.putExtra("MyPackage", bundle);
                startActivity(intent);
            }
        });

        //Load data from Firebase
        loadData();
        sortList();

        return rootView;
    }

    private void sortList() {
        for (int i = 0; i < userList.size()-1; i++) {
            for (int j = 0; j < userList.size()-1; j++) {
                if (userList.get(i).getLastMessage().toString().compareTo(userList.get(j).getLastMessage().toString()) > 0) {
                    User user = userList.get(i);
                    userList.set(i, userList.get(j));
                    userList.set(j, user);
                }
            }
        }
    }

    private void loadData() {
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = (String) dataSnapshot.getKey();
                Log.e("data", id);
                if (id.equals(fbId)) {
                    return;
                }
                User user = dataSnapshot.getValue(User.class);
                Log.e("data", user.getName());
                Log.e("data", user.getLastMessage().toString());
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

        mData.child("Message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = (String) dataSnapshot.getKey();
                if (id.equals(fbId)) {
                    return;
                }
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


