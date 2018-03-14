package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class Status extends ParagraphImp {
    private ArrayList<Comment> commentList;
    private ArrayList<User> likedUserList;

    public Status(User owner, String content, Map<String, String> timestamp) {
        super(owner, content, timestamp);
        this.commentList = new ArrayList<>();
        this.likedUserList = new ArrayList<>();
    }

    public Status(User owner, String content, Map<String, String> timestamp, ArrayList<Comment> commentList, ArrayList<User> likedUserList) {
        super(owner, content, timestamp);
        this.commentList = commentList;
        this.likedUserList = likedUserList;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<User> getLikedUserList() {
        return likedUserList;
    }

    public void setLikedUserList(ArrayList<User> likedUserList) {
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
