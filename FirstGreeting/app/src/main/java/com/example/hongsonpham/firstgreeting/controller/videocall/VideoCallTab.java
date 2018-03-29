package com.example.hongsonpham.firstgreeting.controller.videocall;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.entity.user.Caller;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class VideoCallTab extends Fragment {

    private FirebaseAPI firebaseAPI;

    private ImageButton btnAttend;
    private TextView tvTime;

    private String callerId;
    private Caller caller;
    private FbUser fbUser;
    private List<String> clubNames;

    private com.github.nkzawa.socketio.client.Socket mSocket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_videocall, container, false);

        btnAttend = (ImageButton) rootView.findViewById(R.id.btnAttend);
        tvTime = (TextView) rootView.findViewById(R.id.tv2);

        init();

        return rootView;
    }

    private void init() {
        callerId = getArguments().getString("fbId");
        firebaseAPI = new FirebaseAPI();
        clubNames = new ArrayList<>();

        firebaseAPI.getMyRef().child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String time = dataSnapshot.getValue(String.class);
                tvTime.setText("Will start at " + time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getCaller();
        getClubNames();
        setBtnAttendListener();
        stateListener();

        connectToServer();

    }

    private void getCaller() {
        firebaseAPI.getMyRef().child("user-node/Caller").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(callerId).exists()) {
                    caller = dataSnapshot.child(callerId).getValue(Caller.class);
                } else {
                    firebaseAPI.getMyRef().child("user-node/FbUser/" + callerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            fbUser = dataSnapshot.getValue(FbUser.class);
                            User user = new UserImp(fbUser);
                            caller = new Caller(user, "none", "notReady");
                            firebaseAPI.pushCaller(caller);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getClubNames() {
        firebaseAPI.getMyRef().child("club-node").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String clubName = dataSnapshot1.getKey();
                    clubNames.add(clubName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setBtnAttendListener() {
        btnAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String state = dataSnapshot.getValue(String.class);
                                if (state.equals("notReady")) {
                                    chooseClub();
                                } else if (state.equals("Attended")) {
                                    firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state").setValue("notReady");
                                    Log.e("Sfdsfa", "dsf");
                                    mSocket.emit("cancel-attendant", callerId);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }
        });


    }

    private void stateListener() {
        firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String state = dataSnapshot.getValue(String.class);
                    if (state.equals("Attended")) {
                        btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.leave));
                    } else {
                        btnAttend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.attend));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void chooseClub() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Choose club");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_choose_club);

        final RadioGroup rgClubNames = dialog.findViewById(R.id.rgClubNames);
        if (rgClubNames.getChildCount() == 0) {
            for (String clubName : clubNames) {
                RadioButton rbClubName = new RadioButton(getContext());
                rbClubName.setText(clubName);
                rgClubNames.addView(rbClubName);
            }
        }

        Button btnChoose = (Button) dialog.findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgClubNames.getCheckedRadioButtonId() != -1) {
                    RadioButton radioButton = (RadioButton) dialog.findViewById(rgClubNames.getCheckedRadioButtonId());
                    String clubName = radioButton.getText().toString();
                    firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/clubName").setValue(clubName);
                    if (clubName.equals("Anonymous")) {
                        firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state").setValue("Attended");
                        registerToServer();
                    } else {
                        chooseRole();
                    }
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }

    private void chooseRole() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Choose role");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_choose_role);

        final RadioGroup rgRoles = dialog.findViewById(R.id.rgRoles);
        if (rgRoles.getChildCount() == 0) {
            RadioButton rbNewbie = new RadioButton(getContext());
            rbNewbie.setText("Newbie");
            rgRoles.addView(rbNewbie);

            RadioButton rbOldbie = new RadioButton(getContext());
            rbOldbie.setText("Oldbie");
            rgRoles.addView(rbOldbie);
        }

        Button btnChooseRole = (Button) dialog.findViewById(R.id.btnChooseRole);
        btnChooseRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = (RadioButton) dialog.findViewById(rgRoles.getCheckedRadioButtonId());
                String roleName = radioButton.getText().toString().equals("Oldbie") ? "Member" : "Newbie";
                firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/role").setValue(roleName);
                firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state").setValue("Attended");
                registerToServer();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void connectToServer() {
        try {
            if (mSocket == null) {

                mSocket = IO.socket("https://firstgreeting.herokuapp.com/");

                mSocket.connect();
            }
        } catch (URISyntaxException e) {
        }

        mSocket.emit("client-send", "Successful!");
    }

    private void registerToServer() {
        JSONObject user = new JSONObject();
        try {
            user.put("fbId", caller.getUserId());
            user.put("role", caller.getRole());
            user.put("name", caller.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("get-session-id", user);

        mSocket.on("return-session-id", returnSessionId);
    }

    private Emitter.Listener returnSessionId = new Emitter.Listener() {
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("data nhan duoc", ": ngon");
                    JSONObject data = (JSONObject) args[0];
                    String session;
                    String token;
                    String callerId2;
                    String callerId3;
                    String roomIndex;

                    try {
                        session = data.getString("sessionId");
                        token = data.getString("token");
                        callerId2 = data.getString("id2");
                        callerId3 = data.getString("id3");
                        roomIndex = data.getString("indexSession");

                        Intent intent = new Intent(getActivity(), VideoCallActivity.class);
                        intent.putExtra("sessionId", session);
                        intent.putExtra("token", token);
                        intent.putExtra("callerId", callerId);
                        intent.putExtra("callerName", caller.getUserName());
                        intent.putExtra("callerAvatar", caller.getUserAvatar());
                        intent.putExtra("callerId2", callerId2);
                        intent.putExtra("callerId3", callerId3);
                        intent.putExtra("roomIndex", roomIndex);
                        startActivity(intent);

                        firebaseAPI.getMyRef().child("user-node/Caller/" + callerId + "/state").setValue("notReady");

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
}