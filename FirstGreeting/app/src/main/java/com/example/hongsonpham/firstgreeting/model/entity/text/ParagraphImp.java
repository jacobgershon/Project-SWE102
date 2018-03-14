package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;

import java.util.Map;

/**
 * Created by HongSonPham on 3/13/18.
 */

public class ParagraphImp implements Paragraph {
    private User owner;
    private String content;
    private Map<String, String> timestamp;

    public ParagraphImp(User owner, String content, Map<String, String> timestamp) {
        this.owner = owner;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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
