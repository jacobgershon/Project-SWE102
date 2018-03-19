package com.example.hongsonpham.firstgreeting.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FacebookAPI;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by HongSonPham on 3/14/18.
 */

public class LoginActivity extends AppCompatActivity {
    FirebaseAPI firebaseAPI;
    FacebookAPI facebookAPI;
    LoginButton btnLoginFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAPI = new FirebaseAPI();
        facebookAPI = new FacebookAPI() {
            @Override
            public void moveToHome() {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        };
        btnLoginFacebook = findViewById(R.id.login_button);
        btnLoginFacebook.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));

        facebookAPI.processLogin();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookAPI.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }
}
