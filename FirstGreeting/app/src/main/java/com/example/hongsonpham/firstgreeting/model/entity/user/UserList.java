package com.example.hongsonpham.firstgreeting.model.entity.user;

import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/26/18.
 */

public abstract class UserList extends ArrayList<User> {

    private FirebaseAPI firebaseAPI;
    private String fbId;

    public UserList() {
    }

    public UserList(String fbId) {
        super();
        firebaseAPI = new FirebaseAPI();
        this.fbId = fbId;
        addListener();
    }

    public void addListener() {
        firebaseAPI.getMyRef().child("user-node").child("FbUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals(fbId)) {
                    FbUser fbUser = dataSnapshot.getValue(FbUser.class);
                    User user = new UserImp(fbUser);
                    add(user);
                    addedUserNotify();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals(fbId)) {
                    FbUser fbUser = dataSnapshot.getValue(FbUser.class);
                    User user = new UserImp(fbUser);
                    for (int i = 0; i < size(); i++) {
                        if (get(i).getUserId().equals(user.getUserId())) {
                            set(i, user);
                            break;
                        }
                    }
                    addedUserNotify();
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

    public abstract void addedUserNotify();
}
