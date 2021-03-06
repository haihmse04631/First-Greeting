package com.example.haihm.firstgreeting.message;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by haihm on 8/8/2017.
 */

public class ChatTab extends Fragment {
    private ListView lvListChat;
    private ListUserAdapter adapter;
    private static UserList userList, tempList;
    private String fbId;
    private SearchView svSearchUser;
    private String fbImage;
    private DatabaseReference mData;
    Bundle bundle;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab, container, false);
        lvListChat = (ListView) rootView.findViewById(R.id.listViewListChat);
        svSearchUser = (SearchView) rootView.findViewById(R.id.svSearchUser);
        mData = FirebaseDatabase.getInstance().getReference();


        fbId = getArguments().getString("fbId");
        fbImage = getArguments().getString("fbImage");

        userList = new UserList();
        adapter = new ListUserAdapter(this.getContext(), R.layout.row_list_chat, userList);
        lvListChat.setAdapter(adapter);

        loadData();

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

        searchUser();

        return rootView;
    }

    private void searchUser() {
        svSearchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null && !s.isEmpty()) {
                    tempList = new UserList();
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getName().toLowerCase().contains(s.toLowerCase())) {
                            tempList.add(new User(userList.get(i).getName(), userList.get(i).getLinkAvatar(), userList.get(i).getId(), userList.get(i).getRole(), userList.get(i).getLastMessage()));
                        }
                    }

                    adapter = new ListUserAdapter(getActivity(), R.layout.row_list_chat, tempList);
                    lvListChat.setAdapter(adapter);
                } else {
                    adapter = new ListUserAdapter(getActivity(), R.layout.row_list_chat, userList);
                    lvListChat.setAdapter(adapter);
                }
                return true;
            }
        });

    }


    private void sortList() {
        Collections.sort(userList, new Comparator<User>() {
            public int compare(User p1, User p2) {
                return p2.getLastMessage().get(p2.getId()).getDate().compareTo(p1.getLastMessage().get(p1.getId()).getDate());
            }
        });
    }

    private void loadData() {
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String friendId = (String) dataSnapshot.getKey();
                if (friendId.equals(fbId)) {
                    return;
                }
                final User user = dataSnapshot.getValue(User.class);
                mData.child("Message").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, SingleMessage> lastMessages = new HashMap<>();
                        long indexOfLastMess = dataSnapshot.child(fbId).child(friendId).getChildrenCount();
                        SingleMessage lastMess1;
                        SingleMessage lastMess2;
                        if (indexOfLastMess == 0) {
                            Date date = new Date();
                            date.setTime(0);
                            lastMess1 = new SingleMessage(date, " ", "");
                        } else {
                            lastMess1 = dataSnapshot.child(fbId).child(friendId).child(Long.toString(indexOfLastMess - 1)).getValue(SingleMessage.class);
                        }

                        indexOfLastMess = dataSnapshot.child(friendId).child(fbId).getChildrenCount();
                        if (indexOfLastMess == 0) {
                            lastMess2 = new SingleMessage(lastMess1.getDate(), " ", "");
                        } else {
                            lastMess2 = dataSnapshot.child(friendId).child(fbId).child(Long.toString(indexOfLastMess - 1)).getValue(SingleMessage.class);
                        }

                        SingleMessage lastMess = lastMess1.getDate().compareTo(lastMess2.getDate()) >= 0 ? lastMess1 : lastMess2;
                        lastMessages.put(friendId, lastMess);
                        user.setLastMessage(lastMessages);

                        userList.add(user);
                        sortList();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

        mData.child("Message").child(fbId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String friendId = dataSnapshot.getKey();
                if (friendId.equals(fbId)) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SingleMessage aMessage = child.getValue(SingleMessage.class);
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId().equals(friendId)) {
                            userList.get(i).getLastMessage().put(friendId, aMessage);
                            sortList();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String friendId = dataSnapshot.getKey();
                if (friendId.equals(fbId)) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SingleMessage aMessage = child.getValue(SingleMessage.class);
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId().equals(friendId)) {
                            userList.get(i).getLastMessage().put(friendId, aMessage);
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

        mData.child("Message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String friendId = dataSnapshot.getKey();
                if (friendId.equals(fbId)) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.child(fbId).getChildren()) {
                    SingleMessage aMessage = child.getValue(SingleMessage.class);
                    aMessage.setType("receive");
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId().equals(friendId)) {
                            userList.get(i).getLastMessage().put(friendId, aMessage);
                            sortList();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String friendId = dataSnapshot.getKey();
                if (friendId.equals(fbId)) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.child(fbId).getChildren()) {
                    SingleMessage aMessage = child.getValue(SingleMessage.class);
                    aMessage.setType("receive");
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId().equals(friendId)) {
                            userList.get(i).getLastMessage().put(friendId, aMessage);
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


