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

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by haihm on 8/8/2017.
 */

public class Chat extends Fragment {
    private ListView lvListChat;
    private ListUserAdapter adapter;
    private UserList userList;
    private String fbId;
    private String fbImage;
    DatabaseReference mData;
    Bundle bundle;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        mData = FirebaseDatabase.getInstance().getReference();

        fbId = getArguments().getString("fbId");
        fbImage = getArguments().getString("fbImage");

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
                bundle.putString("fbSendAvatarLink", fbImage);
                bundle.putString("fbReceiveId", user.getId());
                bundle.putString("fbReceiveAvatarLink", user.getLinkAvatar());
                bundle.putString("fbReceiveName", user.getName());
                intent.putExtra("MyPackage", bundle);
                startActivity(intent);
            }
        });

        //Load data from Firebase
        loadData();

        return rootView;
    }

    private void sortList() {
        Collections.sort(userList, new Comparator<User>(){
            public int compare(User p1, User p2){
                return p1.getLastMessage().getDate().compareTo(p2.getLastMessage().getDate());
            }
        });
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
                userList.add(user);
                sortList();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id = (String) dataSnapshot.getKey();
                SingleMessage changedMess = dataSnapshot.child("lastMessage").getValue(SingleMessage.class);
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getId().equals(id)) {
                        userList.get(i).setLastMessage(changedMess);
                        sortList();
                        adapter.notifyDataSetChanged();
                    }
                }
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

        mData.child("Message").child(fbId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String receiveId = dataSnapshot.getKey();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SingleMessage sendMess = child.getValue(SingleMessage.class);
                    SingleMessage receiveMess = new SingleMessage();
                    mData.child("User").child(fbId).child("lastMessage").setValue(sendMess);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String receiveId = dataSnapshot.getKey();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SingleMessage aMessage = child.getValue(SingleMessage.class);
                    mData.child("User").child(fbId).child("lastMessage").setValue(aMessage);
                    mData.child("User").child(receiveId).child("lastMessage").setValue(aMessage);
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId().equals(receiveId)) {
                            userList.get(i).setLastMessage(aMessage);
                            sortList();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
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


