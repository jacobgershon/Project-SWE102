package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;

/**
 * Created by HongSonPham on 3/26/18.
 */

public class Message extends ParagraphImp {
    private UserImp toUser;
    private boolean isRead;

    public Message() {
    }

    public Message(UserImp owner, String content, Object timestamp, UserImp toUser, boolean isRead) {
        super(owner, content, timestamp);
        this.toUser = toUser;
        this.isRead = isRead;
    }

    public Message(String userId, String userName, String userAvatar, String content, Object timestamp, UserImp toUser, boolean isRead) {
        super(userId, userName, userAvatar, content, timestamp);
        this.toUser = toUser;
        this.isRead = isRead;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(UserImp toUser) {
        this.toUser = toUser;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Message{" +
                "toUser=" + toUser +
                ", isRead=" + isRead +
                '}';
    }
}
