package com.example.hongsonpham.firstgreeting.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FacebookAPI;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity {

    FirebaseAPI firebaseAPI;
    FacebookAPI facebookAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAPI = new FirebaseAPI();
        facebookAPI = new FacebookAPI() {
            @Override
            public void moveToHome() {};
        };

//        firebaseAPI.demo();
        loginToApp();

    }

    public void loginToApp() {
        LoginManager.getInstance().logOut();
        if (facebookAPI.isLoginAlready()) {
            facebookAPI.loadImformation();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}
