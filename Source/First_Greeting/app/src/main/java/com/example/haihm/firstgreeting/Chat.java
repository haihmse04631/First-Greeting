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

/**
 * Created by haihm on 8/8/2017.
 */

public class Chat extends Fragment {
    private ListView lvListChat;
    private ArrayList<User> arrayListChat;
    private List_Chat_Adapter adapter;
    DatabaseReference mData;
    Bundle bundle;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        mData = FirebaseDatabase.getInstance().getReference();
        arrayListChat = new ArrayList<>();

        //arrayListChat.add(new User("Hoang Minh Hai", R.drawable.apple));
        loadData();
        adapter = new List_Chat_Adapter(this.getContext(), R.layout.row_list_chat, arrayListChat);
        lvListChat.setAdapter(adapter);

        lvListChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Log.e("data", user.getName());
//                bundle.putString("fbId",arrayListChat.get(i).getId());
//                bundle.putString("fbName", arrayListChat.get(i).getName());
//                bundle.putString("fbImage", arrayListChat.get(i).getLinkAvatar());
//                Intent intent = new Intent(getActivity(), MessageForm.class);
//                intent.putExtra("myPackage", bundle);
//                startActivity(intent);
            }
        });

        return rootView;
    }

    private void loadData(){
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                arrayListChat.add(user);
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


