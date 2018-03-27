package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/20/18.
 */

public abstract class CommentList extends ArrayList<Comment> {
    private FirebaseAPI firebaseAPI;
    private String statusId;

    public CommentList(String statusId) {
        super();
        this.firebaseAPI = new FirebaseAPI();
        this.statusId = statusId;
        addListener();
    }

    public void addListener() {
        firebaseAPI.getMyRef().child("paragraph-node/Status/" + statusId + "/commentList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comment.setParagraphId(dataSnapshot.getKey());
                add(0,comment);
                addedFbUserNotify();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comment.setParagraphId(dataSnapshot.getKey());

                for (int i = 0; i < size(); i++) {
                    if (get(i).getParagraphId().equals(comment.getParagraphId())) {
                        set(i, comment);
                        break;
                    }
                }
                addedFbUserNotify();
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
