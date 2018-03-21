package com.example.hongsonpham.firstgreeting.controller.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FacebookAPI;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.model.adapter.StatusListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.text.Status;
import com.example.hongsonpham.firstgreeting.model.entity.text.StatusList;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.example.hongsonpham.firstgreeting.model.entity.user.User;
import com.example.hongsonpham.firstgreeting.model.entity.user.UserImp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class NewsfeedTab extends Fragment {

    private FirebaseAPI firebaseAPI;

    private Button btnPostStatus;
    private EditText edtStatus;

    private StatusList statusList;
    private ListView lvStatusList;
    private StatusListAdapter statusListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_newsfeed, container, false);

        firebaseAPI = new FirebaseAPI();

        btnPostStatus = (Button) rootView.findViewById(R.id.btnPostStatus);
        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);
        lvStatusList = (ListView) rootView.findViewById(R.id.lvStatusList);

        statusList = new StatusList() {
            @Override
            public void addedFbUserNotify() {
                statusListAdapter.notifyDataSetChanged();
            }
        };
        statusListAdapter = new StatusListAdapter(this, getContext(), R.layout.row_status, statusList);
        lvStatusList.setAdapter(statusListAdapter);

        btnPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String statusContent = edtStatus.getText().toString().trim();
                if (statusContent.equals("")) {
                    return;
                }
                firebaseAPI.getMyRef().child("user-node/FbUser/" + FacebookAPI.fbId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User fbUser = dataSnapshot.getValue(FbUser.class);
                        User owner = new UserImp(fbUser.getUserId(), fbUser.getUserName(), fbUser.getUserAvatar());
                        Status status = new Status((UserImp) owner, statusContent, ServerValue.TIMESTAMP);
                        firebaseAPI.pushStatus(status);
                        edtStatus.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return rootView;
    }
}
