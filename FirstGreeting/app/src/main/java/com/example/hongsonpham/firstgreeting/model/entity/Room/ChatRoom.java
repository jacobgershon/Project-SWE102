package com.example.hongsonpham.firstgreeting.model.entity.Room;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;

/**
 * Created by HongSonPham on 9/20/17.
 */

public class ChatRoom {
    private String nameOfUser1;
    private String nameOfUser2;
    private String nameOfUser3;
    private String avatarOfUser1;
    private String avatarOfUser2;
    private String avatarOfUser3;

    public ChatRoom(User mem1, User mem2, User mem3) {
        this.nameOfUser1 = mem1.getUserName();
        this.nameOfUser2 = mem2.getUserName();
        this.nameOfUser3 = mem3.getUserName();
        this.avatarOfUser1 = mem1.getUserAvatar();
        this.avatarOfUser2 = mem2.getUserAvatar();
        this.avatarOfUser3 = mem3.getUserAvatar();
    }

    public String getNameOfUser1() {
        return nameOfUser1;
    }

    public String getNameOfUser2() {
        return nameOfUser2;
    }

    public String getNameOfUser3() {
        return nameOfUser3;
    }

    public String getAvatarOfUser1() {
        return avatarOfUser1;
    }

    public String getAvatarOfUser2() {
        return avatarOfUser2;
    }

    public String getAvatarOfUser3() {
        return avatarOfUser3;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "nameOfUser1='" + nameOfUser1 + '\'' +
                ", nameOfUser2='" + nameOfUser2 + '\'' +
                ", nameOfUser3='" + nameOfUser3 + '\'' +
                ", avatarOfUser1='" + avatarOfUser1 + '\'' +
                ", avatarOfUser2='" + avatarOfUser2 + '\'' +
                ", avatarOfUser3='" + avatarOfUser3 + '\'' +
                '}';
    }
}
