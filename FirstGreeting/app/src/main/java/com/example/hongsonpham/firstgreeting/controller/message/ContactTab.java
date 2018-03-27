package com.example.hongsonpham.firstgreeting.controller.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.adapter.ContactListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.text.Message;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class ContactTab extends Fragment {

    private FirebaseAPI firebaseAPI;

    private ListView lvContactList;
    private SearchView svSearchUser;

    private ContactListAdapter contactListAdapter;
    private UserList userList;

    private String fbId;
    private Map<String, Message> lastMessage;
    private Map<String, Integer> unReadCount;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_contact, container, false);

        firebaseAPI = new FirebaseAPI();
        fbId = getArguments().getString("fbId");
        lvContactList = (ListView) rootView.findViewById(R.id.lvContactList);
        svSearchUser = (SearchView) rootView.findViewById(R.id.svSearch);

        setListView();
        searchUser();

        return rootView;
    }

    private void setListView() {
        this.lastMessage = new HashMap<>();
        this.unReadCount = new HashMap<>();

        userList = new UserList(fbId) {
            @Override
            public void addedUserNotify() {
                setCountAndLastMessage(userList.get(userList.size() - 1).getUserId());
                contactListAdapter.notifyDataSetChanged();
            }
        };

        contactListAdapter = new ContactListAdapter(this.getContext(), R.layout.row_contact,
                userList, lastMessage, unReadCount);
        lvContactList.setAdapter(contactListAdapter);

        lvContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra("SentUserID", fbId);
                intent.putExtra("RecivedUserID", user.getUserId());
                startActivity(intent);
            }
        });

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
                    UserList tempList = new UserList() {
                        @Override
                        public void addedUserNotify() {

                        }
                    };
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUserName().toLowerCase().contains(s.toLowerCase())) {
                            tempList.add(userList.get(i));
                        }
                    }

                    contactListAdapter.setUserList(tempList);
                    lvContactList.setAdapter(contactListAdapter);
                } else {
                    contactListAdapter.setUserList(userList);
                    lvContactList.setAdapter(contactListAdapter);
                }
                return true;
            }
        });
    }

    private void setCountAndLastMessage(final String friendId) {
        firebaseAPI.getMyRef().child("paragraph-node/Message/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if ((dataSnapshot.child("owner/userId").getValue(String.class).equals(friendId)
                        && dataSnapshot.child("toUser/userId").getValue(String.class).equals(fbId))
                        || (dataSnapshot.child("toUser/userId").getValue(String.class).equals(friendId)
                        && dataSnapshot.child("owner/userId").getValue(String.class).equals(fbId))) {
                    Message message = dataSnapshot.getValue(Message.class);
                    lastMessage.put(friendId, message);
                    sortByTime();
                }

                if (dataSnapshot.child("owner/userId").getValue(String.class).equals(friendId)
                        && dataSnapshot.child("toUser/userId").getValue(String.class).equals(fbId)) {
                    if (!dataSnapshot.child("read").getValue(Boolean.class)) {
                        if (unReadCount.containsKey(friendId)) {
                            unReadCount.put(friendId, unReadCount.get(friendId) + 1);
                        } else {
                            unReadCount.put(friendId, 1);
                        }
                    } else {
                        unReadCount.put(friendId, 0);
                    }
                }
                onCountChange();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("owner/userId").getValue(String.class).equals(friendId)
                        && dataSnapshot.child("toUser/userId").getValue(String.class).equals(fbId)) {
                    if (!dataSnapshot.child("read").getValue(Boolean.class)) {
                        if (unReadCount.containsKey(friendId)) {
                            unReadCount.put(friendId, unReadCount.get(friendId) + 1);
                        } else {
                            unReadCount.put(friendId, 1);
                        }
                    } else {
                        unReadCount.put(friendId, 0);
                    }
                }
                onCountChange();
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

    private void onCountChange() {
        contactListAdapter.notifyDataSetChanged();
    }

    private void sortByTime() {
        if (userList.size() < 2) {
            return;
        }
        for (int i = 0; i < userList.size() - 1; i++) {
            for (int j = i + 1; j < userList.size(); j++) {
                String timestamp1 = "0";
                String timestamp2 = "0";
                if (lastMessage.containsKey(userList.get(i).getUserId())) {
                    timestamp1 = DateFormat.getTimeInstance().format(lastMessage.get(userList.get(i).getUserId()).getTimestamp());
                }
                if (lastMessage.containsKey(userList.get(j).getUserId())) {
                    timestamp2 = DateFormat.getTimeInstance().format(lastMessage.get(userList.get(j).getUserId()).getTimestamp());
                }
                if (timestamp1.compareTo(timestamp2) < 0) {
                    User user = userList.get(i);
                    userList.set(i, userList.get(j));
                    userList.set(j, user);
                }
            }
        }
    }
}
