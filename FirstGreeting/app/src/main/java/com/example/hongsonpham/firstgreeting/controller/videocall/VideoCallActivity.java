package com.example.hongsonpham.firstgreeting.controller.videocall;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.adapter.ChatRoomListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.Room.ChatRoom;
import com.example.hongsonpham.firstgreeting.model.entity.user.Caller;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by HongSonPham on 3/29/18.
 */

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    private final String API_KEY = "45961352";
    private String SESSION_ID;
    private String TOKEN;

    private final int RC_SETTINGS_SCREEN_PERM = 123;
    private final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber1;
    private Subscriber mSubscriber2;

    private FirebaseAPI firebaseAPI;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer1;
    private FrameLayout mSubscriberViewContainer2;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView roomNumber;
    private ImageButton btnRoomInfo;
    private Button btnBack;

    private User user1;
    private User user2;
    private User user3;
    private String roomIndex;
    private List<ChatRoom> roomList;
    private ChatRoomListAdapter roomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_videocall);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_videocall);

        roomNumber = (TextView) findViewById(R.id.tvRoomNumber);
        name1 = (TextView) findViewById(R.id.tvCallerName1);
        name2 = (TextView) findViewById(R.id.tvCallerName2);
        name3 = (TextView) findViewById(R.id.tvCallerName3);

        mPublisherViewContainer = (FrameLayout) findViewById(R.id.frUser1);
        mSubscriberViewContainer1 = (FrameLayout) findViewById(R.id.frUser2);
        mSubscriberViewContainer2 = (FrameLayout) findViewById(R.id.frUser3);

        btnRoomInfo = (ImageButton) findViewById(R.id.btnRoomInfo);
        btnRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDialog();
            }
        });

        btnBack = (Button) findViewById(R.id.btnLeave);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPublisher != null) {
                    mPublisher.destroy();
                }
                if (mSubscriber1 != null) {
                    mSubscriber1.destroy();
                }
                if (mSubscriber2 != null) {
                    mSubscriber2.destroy();
                }
                if (mSession != null) {
                    mSession.disconnect();
                }
                finish();
            }
        });

        init();
    }

    private void init() {
        firebaseAPI = new FirebaseAPI();

        user1 = getOwner();
        getOtherUser();
        name1.setText(user1.getUserName());

        SESSION_ID = getIntent().getStringExtra("sessionId");
        TOKEN = getIntent().getStringExtra("token");

        roomIndex = getIntent().getStringExtra("roomIndex");
        roomNumber.setText("Room " + roomIndex);

        requestPermissions();
    }

    private User getOwner() {
        String callerId = getIntent().getStringExtra("callerId");
        String callerName = getIntent().getStringExtra("callerName");
        String callAvatar = getIntent().getStringExtra("callerAvatar");
        return new UserImp(callerId, callerName, callAvatar);
    }

    private void getOtherUser() {
        String callerId2 = getIntent().getStringExtra("callerId2");
        firebaseAPI.getMyRef().child("user-node/Caller/" + callerId2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Caller caller = dataSnapshot.getValue(Caller.class);
                user2 = caller;
                name2.setText(user2.getUserName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String callerId3 = getIntent().getStringExtra("callerId3");
        firebaseAPI.getMyRef().child("user-node/Caller/" + callerId3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Caller caller = dataSnapshot.getValue(Caller.class);
                user3 = caller;
                name3.setText(user3.getUserName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void actionDialog() {
        Dialog dialog = new Dialog(VideoCallActivity.this);
        dialog.setTitle("Rooms Information");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_room_detail);

        ListView lvRoom = dialog.findViewById(R.id.lvRoomList);
        roomList = new ArrayList<>();
        roomAdapter = new ChatRoomListAdapter(dialog.getContext(), R.layout.row_room_detail, roomList);
        lvRoom.setAdapter(roomAdapter);
        Socket mSocket = null;
        try {
            mSocket = IO.socket("https://firstgreeting.herokuapp.com/");

            mSocket.connect();
        } catch (URISyntaxException e) {
        }

        mSocket.emit("get-info-rooms", "đã nhận req");
        mSocket.on("return-info", returnInfoRooms);

        dialog.show();
    }

    private Emitter.Listener returnInfoRooms = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    roomList.clear();
                    int index = 0;
                    String roomNumber = "";
                    String userId1 = "";
                    String userId2 = "";
                    String userId3 = "";
                    final JSONArray data = (JSONArray) args[0];
                    JSONObject aRoomObj;
                    try {
                        while (true) {
                            aRoomObj = null;
                            try {
                                aRoomObj = data.getJSONObject(index);
                            } catch (Exception ex) {
                            }
                            if (aRoomObj == null) {
                                break;
                            }

                            roomNumber = aRoomObj.getString("roomNumber");

                            try {
                                userId1 = aRoomObj.getString("userId1");
                            } catch (Exception e) {
                            }

                            try {
                                userId2 = aRoomObj.getString("userId2");
                            } catch (Exception e) {
                            }

                            try {
                                userId3 = aRoomObj.getString("userId3");
                            } catch (Exception e) {
                            }

                            User mem1 = new UserImp();
                            User mem2 = new UserImp();
                            User mem3 = new UserImp();

                            if (userId1.equals(user1.getUserId())) {
                                mem1 = new UserImp(user1);
                            } else if (userId1.equals(user2.getUserId())) {
                                mem1 = new UserImp(user2);
                            } else if (userId1.equals(user3.getUserId())) {
                                mem1 = new UserImp(user3);
                            }

                            if (userId2.equals(user1.getUserId())) {
                                mem2 = new UserImp(user1);
                            } else if (userId2.equals(user2.getUserId())) {
                                mem2 = new UserImp(user2);
                            } else if (userId2.equals(user3.getUserId())) {
                                mem2 = new UserImp(user3);
                            }

                            if (userId3.equals(user1.getUserId())) {
                                mem3 = new UserImp(user1);
                            } else if (userId3.equals(user2.getUserId())) {
                                mem3 = new UserImp(user2);
                            } else if (userId3.equals(user3.getUserId())) {
                                mem3 = new UserImp(user3);
                            }

                            ChatRoom chatRoom = new ChatRoom(mem1, mem2, mem3);
                            roomList.add(chatRoom);
                            index++;
                        }
                    } catch (JSONException e)

                    {
                        Log.e("data", "No more room");
                    }

                    roomAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public void fetchSessionConnectionData() {
        mPublisher = null;
        mSubscriber1 = null;
        mSubscriber2 = null;
        mSession = new Session.Builder(VideoCallActivity.this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(VideoCallActivity.this);
        mSession.connect(TOKEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize and connect to the session
            fetchSessionConnectionData();

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        mPublisher = null;
        System.gc();
        mPublisher = new Publisher.Builder(this)
//                .audioTrack(false)
//                .frameRate(Publisher.CameraCaptureFrameRate.FPS_30)
//                .resolution(Publisher.CameraCaptureResolution.MEDIUM)
//                .videoTrack(true)
                .build();
        mPublisher.setPublisherListener(this);
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        View view = mPublisher.getView();
        view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
        mPublisherViewContainer.addView(view);
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (mSubscriber1 == null) {

            Log.e("data: ", "stream 1: " + stream.getName() + " | " + stream.getStreamId());

            mSubscriber1 = new Subscriber.Builder(this, stream).build();
            mSubscriber1.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber1);
            mSubscriber1.setSubscriberListener(new SubscriberKit.SubscriberListener() {
                @Override
                public void onConnected(SubscriberKit subscriberKit) {
                    View view = mSubscriber1.getView();
                    view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
                    mSubscriberViewContainer1.addView(view);
                }

                @Override
                public void onDisconnected(SubscriberKit subscriberKit) {

                }

                @Override
                public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
                    Log.e("Data: ", "Can't subscribe for view 1!");
                }
            });


        } else if (mSubscriber2 == null) {

            Log.e("data: ", "stream 2: " + stream.getName() + " | " + stream.getStreamId());

            mSubscriber2 = new Subscriber.Builder(this, stream).build();
            mSubscriber2.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber2);
            mSubscriber2.setSubscriberListener(new SubscriberKit.SubscriberListener() {
                @Override
                public void onConnected(SubscriberKit subscriberKit) {
                    View view = mSubscriber2.getView();
                    view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
                    mSubscriberViewContainer2.addView(view);
                }

                @Override
                public void onDisconnected(SubscriberKit subscriberKit) {

                }

                @Override
                public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
                    Log.e("Data: ", "Can't subscribe for view 2!");
                }
            });
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (mSubscriber1 != null && mSubscriber1.getStream() == stream) {
            mSubscriber1 = null;
            mSubscriberViewContainer1.removeAllViews();
        }
        if (mSubscriber2 != null && mSubscriber2.getStream() == stream) {
            mSubscriber2 = null;
            mSubscriberViewContainer2.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }
}
