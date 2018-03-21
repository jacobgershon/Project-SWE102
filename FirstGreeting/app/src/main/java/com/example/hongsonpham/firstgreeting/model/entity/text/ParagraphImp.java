package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by HongSonPham on 3/13/18.
 */

@IgnoreExtraProperties
public class ParagraphImp implements Paragraph {
    private String paragraphId;
    private UserImp owner;
    private String content;
    private Object timestamp;

    public ParagraphImp() {

    }

    public ParagraphImp(UserImp owner, String content, Object timestamp) {
        this.owner = owner;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ParagraphImp(String userId, String userName, String userAvatar, String content, Object timestamp) {
        this.owner = new UserImp(userId, userName, userAvatar);
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public Long getTimestamp(boolean isLong) {
        if (timestamp instanceof Long) return (Long) timestamp;
        else return null;
    }

    public UserImp getOwner() {
        return owner;
    }

    public void setOwner(UserImp owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ParagraphImp{" +
                "owner=" + owner +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
