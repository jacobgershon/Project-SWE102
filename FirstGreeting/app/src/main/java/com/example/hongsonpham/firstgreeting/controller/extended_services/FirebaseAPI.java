package com.example.hongsonpham.firstgreeting.controller.extended_services;

import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

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
        myRef.child("user-node/FbUser/" + user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.child("user-node/FbUser/" + user.getUserId()).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public FbUser pullFbUser(String fbId) {
        final FbUser[] user = {new FbUser()};
        myRef.child("user-node/FbUser/" + fbId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user[0] = dataSnapshot.getValue(FbUser.class);
                Log.e("Test user: ", user[0].toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user[0];
    }

    public void pushCaller(final Caller user) {
        myRef.child("user-node/Caller/" + user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    myRef.child("user-node/Caller/" + user.getUserId()).setValue(user);
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
        myRef.child("paragraph-node/Status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.child("paragraph-node/Status").push().setValue(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushComment(final String statusId, final Comment comment) {
        myRef.child("paragraph-node/Status/" + statusId).child("commentList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myRef.child("paragraph-node/Status/" + statusId + "/commentList").push().setValue(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void pushLike(final String statusId, final User user) {
        myRef.child("paragraph-node/Status/" + statusId + "/likedUserList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long commentCount = dataSnapshot.getChildrenCount();
                        myRef.child("paragraph-node/Status/" + statusId + "/likedUserList").push()
                                .setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void demo() {
        UserImp user = new UserImp("0012", "Pham Hong Son", "link");
//        User fbUser = new FbUser(user, "20/07/1997", "hongsongp97@gmail.com", "Male");
//        User caller = new Caller(user, "JS");


//        myRef.child("user-node").child("FbUser").child(fbUser.getUserId()).setValue(fbUser);
//        myRef.child("user-node").child("Caller").child(caller.getUserId()).setValue(caller);

        Paragraph aComment = new Comment(user, "ngonngon", ServerValue.TIMESTAMP);

        Map<String, Comment> commentList = new HashMap<>();
        commentList.put("CN0", (Comment) aComment);

        Map<String, UserImp> likedList = new HashMap<>();
        likedList.put("LN0", (UserImp) user);

        Paragraph status = new Status((UserImp) user, "This's a status", ServerValue.TIMESTAMP, commentList, likedList);
//        Log.e("Test: ", status.toString());

//        this.pushStatus((Status) status);
//        this.pushComment("1", (Comment) aComment);
    }
}
