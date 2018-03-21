package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class Comment extends ParagraphImp {

    public Comment() {
    }

    public Comment(UserImp owner, String content, Object timestamp) {
        super(owner, content, timestamp);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
