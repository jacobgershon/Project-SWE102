package com.example.hongsonpham.firstgreeting.controller.extended_services;

import com.example.hongsonpham.firstgreeting.model.entity.text.Comment;
import com.example.hongsonpham.firstgreeting.model.entity.text.Message;
import com.example.hongsonpham.firstgreeting.model.entity.text.Paragraph;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;
import com.example.hongsonpham.firstgreeting.model.entity.user.Caller;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

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

    public void pushMessage(final Message message) {
        myRef.child("paragraph-node/Message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.child("paragraph-node/Message").push().setValue(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pullMessage() {
        myRef.child("paragraph-node").child("Message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setParagraphId(dataSnapshot.getKey());

//                Log.e("Added: ", message.toString());
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

    public void demo() {
        UserImp user = new UserImp("0012", "Pham Hong Son", "link");
//        User fbUser = new FbUser(user, "20/07/1997", "hongsongp97@gmail.com", "Male");
//        User caller = new Caller(user, "JS");


//        myRef.child("user-node").child("FbUser").child(fbUser.getUserId()).setValue(fbUser);
//        myRef.child("user-node").child("Caller").child(caller.getUserId()).setValue(caller);

//        Paragraph aComment = new Comment(user, "ngonngon", ServerValue.TIMESTAMP);

//        Map<String, Comment> commentList = new HashMap<>();
//        commentList.put("CN0", (Comment) aComment);
//
//        Map<String, UserImp> likedList = new HashMap<>();
//        likedList.put("LN0", (UserImp) user);

//        Paragraph status = new Status((UserImp) user, "This's a status", ServerValue.TIMESTAMP, commentList, likedList);
//        Log.e("Test: ", status.toString());

//        this.pushStatus((Status) status);
//        this.pushComment("1", (Comment) aComment);

        Paragraph message = new Message(user, "This is a message", ServerValue.TIMESTAMP, user, false);
//        this.pushMessage((Message) message);
//        this.pullMessage();

    }
}
