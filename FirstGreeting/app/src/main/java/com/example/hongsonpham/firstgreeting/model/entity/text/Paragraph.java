package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.User;

import java.util.Map;

/**
 * Created by HongSonPham on 3/13/18.
 */

public interface Paragraph {

    public String getContent();

    public void setContent(String content);

    public Map<String, String> getTimestamp();

    public void setTimestamp(Map<String, String> timestamp);

    public User getOwner();

    public void setOwner(User owner);

    @Override
    public String toString();
}
