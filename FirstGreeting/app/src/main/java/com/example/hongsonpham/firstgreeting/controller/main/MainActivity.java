package com.example.hongsonpham.firstgreeting.controller.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.entity.user.Caller;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

//        myRef.setValue("Hello, World!");

        User user = new UserImp("0012", "Pham Hong Son", "link");

        User fbUser = new FbUser(user, "20/07/1997", "hongsongp97@gmail.com", "Male");

        User caller = new Caller(user, "JS");

        System.out.println(fbUser);
        System.out.println(caller);

        myRef.child("FbUser").setValue(fbUser);
        myRef.child("Caller").setValue(caller);
    }
}
