package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/19/18.
 */

public abstract class StatusList extends ArrayList<Status> {

    FirebaseAPI firebaseAPI;

    public StatusList() {
        super();
        this.firebaseAPI = new FirebaseAPI();
        addListener();
    }

    public void addListener() {
        firebaseAPI.getMyRef().child("paragraph-node").child("Status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Status status = dataSnapshot.getValue(Status.class);
                add(status);
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
