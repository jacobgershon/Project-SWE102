package com.example.hongsonpham.firstgreeting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.entity.text.Comment;
import com.example.hongsonpham.firstgreeting.model.entity.text.CommentList;
import com.squareup.picasso.Picasso;

/**
 * Created by HongSonPham on 3/20/18.
 */

public class CommentListAdapter extends BaseAdapter {

    private Context myContext;
    private int myLayout;
    private CommentList commentList;

    public CommentListAdapter(Context myContext, int myLayout, CommentList commentList) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
        TextView tvComment;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);
        holder.imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        holder.tvComment = (TextView) view.findViewById(R.id.tvComment);
        holder.tvName = (TextView) view.findViewById(R.id.tvName);
        view.setTag(holder);

        Comment comment = commentList.get(position);
        holder.tvComment.setText(comment.getContent());
        holder.tvName.setText(comment.getOwner().getUserName());
        Picasso.with(myContext).load(comment.getOwner().getUserAvatar()).into(holder.imgAvatar);
        return view;
    }
}
