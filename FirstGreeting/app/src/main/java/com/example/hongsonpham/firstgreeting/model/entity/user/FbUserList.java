package com.example.hongsonpham.firstgreeting.model.entity.user;

import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/16/18.
 */

public abstract class FbUserList extends ArrayList<FbUser> {

    FirebaseAPI firebaseAPI;

    public FbUserList() {
        super();
        firebaseAPI = new FirebaseAPI();
        addListener();
    }

    public void addListener() {
        firebaseAPI.getMyRef().child("user-node").child("FbUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FbUser fbUser = dataSnapshot.getValue(FbUser.class);
                add(fbUser);
                addedFbUserNotify();
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

    public abstract void addedFbUserNotify();
}
