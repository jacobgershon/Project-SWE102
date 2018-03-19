package com.example.hongsonpham.firstgreeting.controller.extended_services;

import com.example.hongsonpham.firstgreeting.model.entity.text.Comment;
import com.example.hongsonpham.firstgreeting.model.entity.text.Paragraph;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;
import com.example.hongsonpham.firstgreeting.model.entity.user.Caller;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/14/18.
 */

public class FirebaseAPI {
    DatabaseReference myRef;

    public FirebaseAPI() {
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }


    public void pushFbUser(final FbUser user) {
        myRef.child("user-node").child("FbUser").child(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.child("user-node").child("FbUser").child(user.getUserId()).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushCaller(final Caller user) {
        myRef.child("user-node").child("Caller").child(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    myRef.child("user-node").child("Caller").child(user.getUserId()).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushACaller(User user) {
        Caller caller = new Caller(user, "none");
        pushCaller(caller);
    }

    public void pushStatus(final Status status) {
        myRef.child("paragraph-node").child("Status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long statusCount = dataSnapshot.getChildrenCount();
                myRef.child("paragraph-node").child("Status").child(Long.toString(statusCount)).setValue(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushComment(final String statusId, final Comment comment) {
        myRef.child("paragraph-node").child("Status").child(statusId).child("commentList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long commentCount = dataSnapshot.getChildrenCount();
                        myRef.child("paragraph-node").child("Status").child(statusId).child("commentList")
                                .child(Long.toString(commentCount)).setValue(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void demo() {
        User user = new UserImp("0012", "Pham Hong Son", "link");
//        User fbUser = new FbUser(user, "20/07/1997", "hongsongp97@gmail.com", "Male");
        User caller = new Caller(user, "JS");


//        myRef.child("user-node").child("FbUser").child(fbUser.getUserId()).setValue(fbUser);
        myRef.child("user-node").child("Caller").child(caller.getUserId()).setValue(caller);

        Paragraph aComment = new Comment(user, "ngonngon", ServerValue.TIMESTAMP);
        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add((Comment) aComment);
        Paragraph aStatus = new Status(user, "This's a status", ServerValue.TIMESTAMP, commentList, new ArrayList<User>());

        ArrayList<Status> statusList = new ArrayList<>();
        statusList.add((Status) aStatus);
//        this.postStatus((Status) aStatus);
        this.pushComment("1", (Comment) aComment);
    }
}
