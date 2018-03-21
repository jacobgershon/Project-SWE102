package com.example.hongsonpham.firstgreeting.model.entity.text;

import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;

/**
 * Created by HongSonPham on 3/13/18.
 */

public interface Paragraph {
    public String getParagraphId();

    public void setParagraphId(String paragraphId);

    public String getContent();

    public void setContent(String content);

    public Object getTimestamp();

    public void setTimestamp(Object timestamp);

    public UserImp getOwner();

    public void setOwner(UserImp owner);

    @Override
    public String toString();
}
