package com.example.hongsonpham.firstgreeting.model.entity.user;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class UserImp implements User{
    String userId;
    String userName;
    String userAvatar;

    public UserImp() {
    }

    public UserImp(String userId, String userName, String userAvatar) {
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    public UserImp(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userAvatar = user.getUserAvatar();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "UserImp{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
