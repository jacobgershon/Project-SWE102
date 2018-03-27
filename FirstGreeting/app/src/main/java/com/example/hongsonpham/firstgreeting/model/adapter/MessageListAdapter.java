package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.entity.text.MessageList;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;

/**
 * Created by HongSonPham on 3/26/18.
 */

public class MessageListAdapter extends BaseAdapter {
    private Context MessContext;
    private int SendLayout;
    private int ReceiveLayout;
    private MessageList messageList;
    private String fbId;

    public MessageListAdapter(Context messContext, int sendLayout, int receiveLayout, MessageList messageList, String fbId) {
        this.MessContext = messContext;
        this.SendLayout = sendLayout;
        this.ReceiveLayout = receiveLayout;
        this.messageList = messageList;
        this.fbId = fbId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolderReceive {
        TextView tvRecievedMessage;
        ImageView imgReceiverAvatar;
        TextView tvTime;
    }

    private class ViewHolderSend {
        TextView tvSentMessage;
        ImageView imgSenderAvatar;
        TextView tvTime;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolderSend sendHolder = new ViewHolderSend();
        ViewHolderReceive recieveHolder = new ViewHolderReceive();
        LayoutInflater inflater = (LayoutInflater) MessContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        if (messageList.get(position).getOwner().getUserId().equals(fbId)) {
            rowView = inflater.inflate(SendLayout, null);
            sendHolder.imgSenderAvatar = (ImageView) rowView.findViewById(R.id.imgSenderAvatar);
            sendHolder.tvSentMessage = (TextView) rowView.findViewById(R.id.tvMessageContent);
            sendHolder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            rowView.setTag(sendHolder);
            sendHolder = (ViewHolderSend) rowView.getTag();

            sendHolder.tvSentMessage.setText(messageList.get(position).getContent());
            String date = DateFormat.getTimeInstance().format(messageList.get(position).getTimestamp());
            sendHolder.tvTime.setText(date);
            if (((position > 0) && !messageList.get(position - 1).getOwner().getUserId().equals(fbId)) || (position == 0)) {
                Picasso.with(MessContext).load(messageList.get(position).getOwner().getUserAvatar()).into(sendHolder.imgSenderAvatar);
            }

        } else {
            rowView = inflater.inflate(ReceiveLayout, null);
            recieveHolder.imgReceiverAvatar = (ImageView) rowView.findViewById(R.id.imgRecieveAvatar);
            recieveHolder.tvRecievedMessage = (TextView) rowView.findViewById(R.id.tvMessageContent);
            recieveHolder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            rowView.setTag(recieveHolder);
            recieveHolder = (ViewHolderReceive) rowView.getTag();

            recieveHolder.tvRecievedMessage.setText(messageList.get(position).getContent());
            String stringDate = DateFormat.getTimeInstance().format(messageList.get(position).getTimestamp());
            recieveHolder.tvTime.setText(stringDate);
            if (((position > 0) && messageList.get(position - 1).getOwner().getUserId().equals(fbId)) || (position == 0)) {
                Picasso.with(MessContext).load(messageList.get(position).getOwner().getUserAvatar()).into(recieveHolder.imgReceiverAvatar);
            }
        }

        return rowView;
    }
}
