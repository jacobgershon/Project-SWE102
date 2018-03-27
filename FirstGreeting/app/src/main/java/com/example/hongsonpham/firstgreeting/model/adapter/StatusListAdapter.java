package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.controller.newsfeed.NewsfeedTab;
import com.example.hongsonpham.firstgreeting.controller.newsfeed.StatusDetailActivity;
import com.example.hongsonpham.firstgreeting.model.entity.text.Comment;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;
import com.example.hongsonpham.firstgreeting.model.entity.text.StatusList;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HongSonPham on 3/19/18.
 */

public class StatusListAdapter extends BaseAdapter {
    private NewsfeedTab parent;
    private Context myContext;
    private int myLayout;
    private StatusList statusList;
    private Map<String, Boolean> isLiked;
    private FirebaseAPI firebaseAPI;
    private String fbId;

    public StatusListAdapter(NewsfeedTab parent, Context myContext, int myLayout, StatusList statusList, String fbId) {
        this.parent = parent;
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.statusList = statusList;
        this.firebaseAPI = new FirebaseAPI();
        this.isLiked = new HashMap<>();
        this.fbId = fbId;
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
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(myLayout, null);

        holder.imgUserAvatar = (ImageView) rowView.findViewById(R.id.imgUserAvatar);
        holder.tvUserName = (TextView) rowView.findViewById(R.id.tvUserName);
        holder.tvStatusContent = (TextView) rowView.findViewById(R.id.tvStatusContent);
        holder.tvLikedNumber = (TextView) rowView.findViewById(R.id.tvLikedNumber);
        holder.tvCommentedNumber = (TextView) rowView.findViewById(R.id.tvCommentedNumber);
        holder.btnLike = (ImageView) rowView.findViewById(R.id.btnLike);
        holder.btnComment = (ImageView) rowView.findViewById(R.id.btnComment);
        rowView.setTag(holder);

        //Set value
        final Status status = statusList.get(position);
        holder.tvUserName.setText(status.getOwner().getUserName());
        holder.tvStatusContent.setText(status.getContent());
        if (status.getLikedUserList() == null) {
            status.setLikedUserList(new HashMap<String, UserImp>());
        }
        if (status.getCommentList() == null) {
            status.setCommentList(new HashMap<String, Comment>());
        }
        holder.tvLikedNumber.setText("Like: (" + status.getLikedUserList().size() + ")");
        holder.tvCommentedNumber.setText("Comment: (" + status.getCommentList().size() + ")");
        Picasso.with(myContext).load(status.getOwner().getUserAvatar()).into(holder.imgUserAvatar);

        isLiked.put(status.getParagraphId(), false);
        for (String key : status.getLikedUserList().keySet()) {
            if (status.getLikedUserList().get(key).getUserId().equals(fbId)) {
                isLiked.put(status.getParagraphId(), true);
                break;
            }
        }

        if (isLiked.get(status.getParagraphId()) == true) {
            holder.btnLike.setBackgroundResource(R.drawable.shape_liked);
        } else {
            holder.btnLike.setBackgroundResource(R.drawable.shape_like);
        }

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAPI.getMyRef().child("user-node/FbUser/" + fbId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (isLiked.get(status.getParagraphId())) return;
                                User fbUser = dataSnapshot.getValue(FbUser.class);
                                User user = new UserImp(fbUser.getUserId(), fbUser.getUserName(), fbUser.getUserAvatar());
                                firebaseAPI.pushLike(status.getParagraphId(), user);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getActivity(), StatusDetailActivity.class);
                intent.putExtra("Status ID", status.getParagraphId());
                intent.putExtra("fbId", fbId);
                parent.startActivity(intent);
            }
        });

        return rowView;
    }
}
