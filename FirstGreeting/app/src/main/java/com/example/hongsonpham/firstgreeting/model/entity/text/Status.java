package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HongSonPham on 3/13/18.
 */

@IgnoreExtraProperties
public class Status extends ParagraphImp {
    private Map<String, Comment> commentList;
    private Map<String, UserImp> likedUserList;

    public Status() {
    }

    public Status(UserImp owner, String content, Object timestamp) {
        super(owner, content, timestamp);
        this.commentList = new HashMap<>();
        this.likedUserList = new HashMap<>();
    }

    public Status(UserImp owner, String content, Object timestamp, Map<String, Comment> commentList, Map<String, UserImp> likedUserList) {
        super(owner, content, timestamp);
        this.commentList = commentList;
        this.likedUserList = likedUserList;
    }

    public Status(String userId, String userName, String userAvatar, String content, Object timestamp, Map<String, Comment> commentList, Map<String, UserImp> likedUserList) {
        super(userId, userName, userAvatar, content, timestamp);
        this.commentList = commentList;
        this.likedUserList = likedUserList;
    }

    public Map<String, Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(Map<String, Comment> commentList) {
        this.commentList = commentList;
    }

    public Map<String, UserImp> getLikedUserList() {
        return likedUserList;
    }

    public void setLikedUserList(Map<String, UserImp> likedUserList) {
        this.likedUserList = likedUserList;
    }

    @Override
    public String toString() {
        return super.toString() + "\nStatus{" +
                "commentList=" + commentList +
                ", likedUserList=" + likedUserList +
                '}';
    }
}
