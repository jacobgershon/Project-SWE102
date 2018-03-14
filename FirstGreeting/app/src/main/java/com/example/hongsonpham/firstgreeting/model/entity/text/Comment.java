package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;

import java.util.Map;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class Comment extends ParagraphImp {

    public Comment(User owner, String content, Map<String, String> timestamp) {
        super(owner, content, timestamp);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
