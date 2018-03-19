package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.newsfeed.NewsfeedTab;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 3/19/18.
 */

public class StatusListAdapter extends BaseAdapter{
    private NewsfeedTab parent;
    private Context myContext;
    private int myLayout;
    private ArrayList<Status> statusList;

    public StatusListAdapter(NewsfeedTab parent, Context myContext, int myLayout, ArrayList<Status> statusList) {
        this.parent = parent;
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.statusList = statusList;
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    @Override
    public Object getItem(int i) {
        return statusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUserName;
        TextView tvStatusContent;
        ImageView btnComment;
        ImageView btnLike;
        TextView tvLikedNumber;
        TextView tvCommentedNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final StatusListAdapter.ViewHolder holder = new StatusListAdapter.ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(myLayout, null);
        holder.imgUserAvatar = (ImageView) rowView.findViewById(R.id.imgUserAvatar);
        holder.tvUserName = (TextView) rowView.findViewById(R.id.tvUserName);
        holder.tvStatusContent = (TextView) rowView.findViewById(R.id.tvStatusContent);
        holder.tvLikedNumber = (TextView) rowView.findViewById(R.id.tvLikedNumber);
        holder.tvCommentedNumber = (TextView) rowView.findViewById(R.id.tvLikedNumber);
        holder.btnLike = (ImageView) rowView.findViewById(R.id.btnLike);
        holder.btnComment = (ImageView) rowView.findViewById(R.id.btnComment);
        rowView.setTag(holder);

        //Set value
        Status status = statusList.get(position);
        holder.tvUserName.setText(status.getOwner().getUserName());
        holder.tvStatusContent.setText(status.getContent());
        holder.tvLikedNumber.setText("Like: (" + status.getLikedUserList().size() + ")");
        holder.tvCommentedNumber.setText("Like: (" + status.getCommentList().size() + ")");
        Glide.with(myContext).load(status.getOwner().getUserAvatar()).into(holder.imgUserAvatar);

//        final int size = statusList.size() - 1;
//        final Status userStatus = statusList.get(position);
//        holder.tvUserName.setText(userStatus.getOwnerName());
//        holder.tvStatusContent.setText(statusList.get(position).getContentOfStatus());
//        boolean liked = false;
//        if (userStatus.likedUserId == null) {
//            userStatus.likedUserId = new ArrayList<String>();
//        }
//        for (String liker : userStatus.likedUserId) {
//            if (liker.equals(parent.getFbId())) {
//                liked = true;
//                break;
//            }
//        }
//        if (liked) {
//            holder.btnLike.setBackgroundResource(R.drawable.liked);
//        } else {
//            holder.btnLike.setBackgroundResource(like);
//        }
//
//        Picasso.with(myContext).load(userStatus.getOwnerAvatar()).into(holder.imgUserAvatar);
//        holder.tvLikedNumber.setText("Like: (" + userStatus.getLikedNumber() + ")");
//        holder.tvCommentedNumber.setText("Comment: (" + userStatus.getCommentedNumber() + ")");
//
//        holder.btnComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                parent.startIntent(position);
//            }
//        });
//
//
//        holder.btnLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean liked = false;
//                for (String liker : userStatus.likedUserId) {
//                    if (liker.equals(parent.getFbId())) {
//                        liked = true;
//                        break;
//                    }
//                }
//                if (!liked) {
//                    userStatus.likedUserId.add(parent.getFbId());
//                    Log.e("Data: ", userStatus.toString());
//                    userStatus.setLikedNumber(userStatus.likedUserId.size());
//                    mDatabase.child("Status").child(Integer.toString(size - position)).child("likedNumber").setValue(userStatus.getLikedNumber());
//                    mDatabase.child("Status").child(Integer.toString(size - position)).child("likedUsers").child(Integer.toString(userStatus.getLikedNumber() - 1)).setValue(par.getFbId());
//                    holder.tvLikedNumber.setText("Like: (" + Integer.toString(userStatus.getLikedNumber()) + ")");
//                    holder.btnLike.setBackgroundResource(R.drawable.liked);
//                }
//            }
//        });

        return rowView;
    }
}
