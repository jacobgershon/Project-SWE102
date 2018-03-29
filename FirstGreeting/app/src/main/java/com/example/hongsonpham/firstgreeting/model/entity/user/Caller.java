package com.example.hongsonpham.firstgreeting.model.entity.user;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by HongSonPham on 3/13/18.
 */

@IgnoreExtraProperties
public class Caller extends UserImp {
    private String clubName;
    private String state;
    private String role;

    public Caller() {
    }

    public Caller(String userId, String userName, String userAvatar, String clubName, String state) {
        super(userId, userName, userAvatar);
        this.clubName = clubName;
        this.state = state;
        this.role = "Newbie";
    }

    public Caller(User user, String clubName, String state) {
        super(user);
        this.clubName = clubName;
        this.state = state;
        this.role = "Newbie";
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString() + "\nCaller{" +
                "clubName='" + clubName + '\'' +
                '}';
    }
}
