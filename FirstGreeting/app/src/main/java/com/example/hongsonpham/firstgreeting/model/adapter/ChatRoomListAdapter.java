package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.entity.Room.ChatRoom;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HongSonPham on 9/17/17.
 */

public class ChatRoomListAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<ChatRoom> chatRoomList;

    public ChatRoomListAdapter(Context context, int layout, List<ChatRoom> chatRoomList) {
        this.context = context;
        this.layout = layout;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatRoomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        holder.tvRoomNumber = (TextView) view.findViewById(R.id.tvRoomNumber);
        holder.tvUser1 = (TextView) view.findViewById(R.id.tvUser1);
        holder.tvUser2 = (TextView) view.findViewById(R.id.tvUser2);
        holder.tvUser3 = (TextView) view.findViewById(R.id.tvUser3);
        holder.imgAvatarUser1 = (ImageView) view.findViewById(R.id.imgAvatarUser1);
        holder.imgAvatarUser2 = (ImageView) view.findViewById(R.id.imgAvatarUser2);
        holder.imgAvatarUser3 = (ImageView) view.findViewById(R.id.imgAvatarUser3);

        view.setTag(holder);

        holder.tvRoomNumber.setText("ChatRoom " + (i + 1));
        holder.tvUser1.setText(chatRoomList.get(i).getNameOfUser1());
        holder.tvUser2.setText(chatRoomList.get(i).getNameOfUser2());
        holder.tvUser3.setText(chatRoomList.get(i).getNameOfUser3());
        if (chatRoomList.get(i).getAvatarOfUser1() != null)
            Picasso.with(context).load(chatRoomList.get(i).getAvatarOfUser1()).into(holder.imgAvatarUser1);
        if (chatRoomList.get(i).getAvatarOfUser2() != null)
            Picasso.with(context).load(chatRoomList.get(i).getAvatarOfUser2()).into(holder.imgAvatarUser2);
        if (chatRoomList.get(i).getAvatarOfUser3() != null)
            Picasso.with(context).load(chatRoomList.get(i).getAvatarOfUser3()).into(holder.imgAvatarUser3);

        return view;
    }

    class ViewHolder {
        TextView tvRoomNumber;
        ImageView imgAvatarUser1;
        TextView tvUser1;
        ImageView imgAvatarUser2;
        TextView tvUser2;
        ImageView imgAvatarUser3;
        TextView tvUser3;
    }
}
