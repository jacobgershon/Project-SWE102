package com.example.hongsonpham.firstgreeting.controller.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.adapter.MessageListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.text.Message;
import com.example.hongsonpham.firstgreeting.model.entity.text.MessageList;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by HongSonPham on 3/26/18.
 */

public class ConversationActivity extends AppCompatActivity {

    private FirebaseAPI firebaseAPI;

    private LinearLayout lnLayout;
    private ListView lvMessageList;
    private ImageButton btnBackToContactView;
    private ImageView recieveUserAvatar;
    private ImageButton btnSend;
    private EditText etInput;


    private MessageListAdapter messageListadApter;
    private MessageList messageList;

    private TextView tvRecieveName;
    private String recieveUserId;
    private String sentUserId;
    private User sender;
    private User reciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_conversation);

        init();
        getUsersAndSetAvatarAndName();
        setListView();
        addBtnSendListener();
        setShowTimeListener();
        setReadMessage();
    }

    private void init() {
        firebaseAPI = new FirebaseAPI();
        recieveUserId = getIntent().getStringExtra("RecivedUserID");
        sentUserId = getIntent().getStringExtra("SentUserID");

        lnLayout = (LinearLayout) findViewById(R.id.lnLayout);
        btnBackToContactView = (ImageButton) findViewById(R.id.backToChat);
        recieveUserAvatar = (ImageView) findViewById(R.id.imgRecieveAvatar);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        etInput = (EditText) findViewById(R.id.etInput);
        tvRecieveName = (TextView) findViewById(R.id.txtMyTitle);
        lvMessageList = (ListView) findViewById(R.id.lvMessageList);

        lnLayout.bringToFront();

        btnBackToContactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReadMessage();
                finish();
            }
        });
    }

    private void setListView() {
        messageList = new MessageList(sentUserId, recieveUserId) {
            @Override
            public void addedMessageNotify() {
                if (messageList.size() > 0) {
                    lnLayout.setVisibility(View.INVISIBLE);
                }
                messageListadApter.notifyDataSetChanged();
            }
        };
        messageListadApter = new MessageListAdapter(ConversationActivity.this,
                R.layout.row_message_sender, R.layout.row_message_recieve, messageList, sentUserId);
        lvMessageList.setAdapter(messageListadApter);
    }

    private void getUsersAndSetAvatarAndName() {
        firebaseAPI.getMyRef().child("user-node/FbUser/" + sentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbUser fbUser = dataSnapshot.getValue(FbUser.class);
                sender = new UserImp(fbUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firebaseAPI.getMyRef().child("user-node/FbUser/" + recieveUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbUser fbUser = dataSnapshot.getValue(FbUser.class);
                reciever = new UserImp(fbUser);
                Picasso.with(ConversationActivity.this).load(reciever.getUserAvatar()).into(recieveUserAvatar);
                tvRecieveName.setText(reciever.getUserName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addBtnSendListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etInput.getText().toString().trim();
                if (content.isEmpty()) {
                    return;
                }
                etInput.setText("");
                Message message = new Message((UserImp) sender, content, ServerValue.TIMESTAMP, (UserImp) reciever, false);
                firebaseAPI.pushMessage(message);
            }
        });
    }

    private void setShowTimeListener() {
        lvMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final TextView time = view.findViewById(R.id.tvTime);
                if (time.getVisibility() != View.VISIBLE) {
                    time.setVisibility(View.VISIBLE);
                } else {
                    time.setVisibility(View.GONE);
                }
                time.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        time.setVisibility(View.GONE);
                    }
                }, 2000);

            }
        });

    }

    private void setReadMessage() {
        firebaseAPI.getMyRef().child("paragraph-node/Message").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for( DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            Log.e("sdf: ", dataSnapshot1.child("owner/userId").getValue(String.class));
                            if (dataSnapshot1.child("owner/userId").getValue(String.class).equals(recieveUserId)
                                    && dataSnapshot1.child("toUser/userId").getValue(String.class).equals(sentUserId)) {
                                firebaseAPI.getMyRef().child("paragraph-node/Message/" + dataSnapshot1.getKey()
                                        + "/read").setValue(new Boolean(true));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
