package com.example.hongsonpham.firstgreeting.model.entity.user;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class FbUser extends UserImp {
    private String userDOB;
    private String userEmail;
    private String userGender;

    public FbUser(String userDOB, String userEmail, String userGender) {
        this.userDOB = userDOB;
        this.userEmail = userEmail;
        this.userGender = userGender;
    }

    public FbUser(String userId, String userName, String userAvatar, String userDOB, String userEmail, String userGender) {
        super(userId, userName, userAvatar);
        this.userDOB = userDOB;
        this.userEmail = userEmail;
        this.userGender = userGender;
    }

    public FbUser(User user, String userDOB, String userEmail, String userGender) {
        super(user);
        this.userDOB = userDOB;
        this.userEmail = userEmail;
        this.userGender = userGender;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    @Override
    public String toString() {
        return "FbUser{" +
                "userDOB='" + userDOB + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userGender='" + userGender + '\'' +
                '}';
    }
}
