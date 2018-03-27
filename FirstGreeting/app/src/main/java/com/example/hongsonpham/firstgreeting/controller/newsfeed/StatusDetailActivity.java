package com.example.hongsonpham.firstgreeting.controller.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.adapter.CommentListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.text.Comment;
import com.example.hongsonpham.firstgreeting.model.entity.text.CommentList;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by HongSonPham on 3/20/18.
 */

public class StatusDetailActivity extends AppCompatActivity {

    FirebaseAPI firebaseAPI;

    private String statusId;
    private Status status;

    private ImageView imgOwnerAvatar;
    private TextView tvOwnerName;
    private TextView tvStatusContent;
    private EditText edtComment;
    private ImageButton btnCommentSubmit;

    private CommentList commentList;
    private ListView lvCommentList;
    private CommentListAdapter commentListAdapter;

    private String fbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        firebaseAPI = new FirebaseAPI();
        statusId = getIntent().getStringExtra("Status ID");

        imgOwnerAvatar = (ImageView) findViewById(R.id.imgOwnerAvatar);
        tvOwnerName = (TextView) findViewById(R.id.tvOwnerName);
        tvStatusContent = (TextView) findViewById(R.id.tvStatusContent);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnCommentSubmit = (ImageButton) findViewById(R.id.btnCommentSubmit);
        lvCommentList = (ListView) findViewById(R.id.lvCommentList);

        fbId = getIntent().getStringExtra("fbId");

        commentList = new CommentList(statusId) {
            @Override
            public void addedFbUserNotify() {
                commentListAdapter.notifyDataSetChanged();
            }
        };
        commentListAdapter = new CommentListAdapter(StatusDetailActivity.this, R.layout.row_comment, commentList);
        lvCommentList.setAdapter(commentListAdapter);

        getStatusInformation();
        addEventForBtnSubmit();
    }

    private void getStatusInformation() {
        firebaseAPI.getMyRef().child("paragraph-node/Status/" + statusId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status = dataSnapshot.getValue(Status.class);
                Picasso.with(getApplicationContext()).load(status.getOwner().getUserAvatar()).into(imgOwnerAvatar);
                tvOwnerName.setText(status.getOwner().getUserName());
                tvStatusContent.setText(status.getContent());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addEventForBtnSubmit() {

        btnCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String commentContent = edtComment.getText().toString().trim();
                if (commentContent.equals("")) {
                    return;
                }
                firebaseAPI.getMyRef().child("user-node/FbUser/" + fbId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User fbUser = dataSnapshot.getValue(FbUser.class);
                                User owner = new UserImp(fbUser.getUserId(), fbUser.getUserName(), fbUser.getUserAvatar());
                                Comment comment = new Comment((UserImp) owner, commentContent, ServerValue.TIMESTAMP);
                                firebaseAPI.pushComment(statusId, comment);
                                edtComment.setText("");
                                try {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }
}
