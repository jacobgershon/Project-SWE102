package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/26/18.
 */

public abstract class MessageList extends ArrayList<Message> {
    private FirebaseAPI firebaseAPI;
    private String ownerId, friendId;

    public MessageList(String ownerId, String friendId) {
        super();
        this.firebaseAPI = new FirebaseAPI();
        this.ownerId = ownerId;
        this.friendId = friendId;
        addListener();
    }

    public void addListener() {
        firebaseAPI.getMyRef().child("paragraph-node").child("Message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if ((dataSnapshot.child("owner").child("userId").getValue().equals(ownerId)
                        && dataSnapshot.child("toUser").child("userId").getValue().equals(friendId))
                        || (dataSnapshot.child("toUser").child("userId").getValue().equals(ownerId)
                        && dataSnapshot.child("owner").child("userId").getValue().equals(friendId))) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setParagraphId(dataSnapshot.getKey());

                    add(message);
                    addedMessageNotify();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if ((dataSnapshot.child("owner").child("userId").getValue().equals(ownerId)
                        && dataSnapshot.child("toUser").child("userId").getValue().equals(friendId))
                        || (dataSnapshot.child("toUser").child("userId").getValue().equals(ownerId)
                        && dataSnapshot.child("owner").child("userId").getValue().equals(friendId))) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setParagraphId(dataSnapshot.getKey());

                    for (int i = 0; i < size(); i++) {
                        if (get(i).getParagraphId().equals(message.getParagraphId())) {
                            set(i, message);
                            break;
                        }
                    }
                    addedMessageNotify();
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

    public abstract void addedMessageNotify();
}
