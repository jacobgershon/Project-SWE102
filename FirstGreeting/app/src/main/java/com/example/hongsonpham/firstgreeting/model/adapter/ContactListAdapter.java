package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.entity.text.Message;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserList;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by HongSonPham on 3/26/18.
 */

public class ContactListAdapter extends BaseAdapter {
    private Context myContext;
    private int myLayout;
    private UserList userList;
    private Map<String, Message> lastMessage;
    private Map<String, Integer> unReadCount;
    private FirebaseAPI firebaseAPI;
    private String fbId;

    public ContactListAdapter(Context myContext, int myLayout, UserList userList,
                              Map<String, Message> lastMessage, Map<String, Integer> unReadCount) {
        firebaseAPI = new FirebaseAPI();
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.userList = userList;
        this.lastMessage = lastMessage;
        this.unReadCount = unReadCount;
    }

    public void setUserList(UserList userList) {
        this.userList = userList;
    }

    public void setUnReadCount(Map<String, Integer> unReadCount) {
        this.unReadCount = unReadCount;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUserName;
        TextView tvLastMessage;
        TextView tvUnreadMessageCount;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ContactListAdapter.ViewHolder holder = new ContactListAdapter.ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        holder.imgUserAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        holder.tvUserName = (TextView) view.findViewById(R.id.tvName);
        holder.tvLastMessage = (TextView) view.findViewById(R.id.tvLastMessage);
        holder.tvUnreadMessageCount = (TextView) view.findViewById(R.id.tvUnreadMessageCount);

        view.setTag(holder);

        final User user = userList.get(position);
        holder.tvUserName.setText(user.getUserName());
        if (lastMessage.containsKey(user.getUserId())) {
            holder.tvLastMessage.setText(lastMessage.get(user.getUserId()).getContent());
        }
        if (unReadCount.containsKey(user.getUserId())) {
            holder.tvUnreadMessageCount.setText(Integer.toString(unReadCount.get(user.getUserId())));
            if (unReadCount.get(user.getUserId()) != 0) {
                holder.tvUnreadMessageCount.setVisibility(View.VISIBLE);
            }
        }

        Picasso.with(myContext).load(user.getUserAvatar()).into(holder.imgUserAvatar);

        return view;
    }
}
