package com.example.hongsonpham.firstgreeting.controller.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.adapter.StatusListAdapter;
import com.example.hongsonpham.firstgreeting.model.entity.text.StatusList;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class NewsfeedTab extends Fragment {

    private Button btnPostStatus;
    private EditText edtStatus;

    private StatusList statusList;
    private ListView lvStatusLis;
    private StatusListAdapter statusListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_newsfeed, container, false);

        btnPostStatus = (Button) rootView.findViewById(R.id.btnPostStatus);
        edtStatus = (EditText) rootView.findViewById(R.id.edtStatus);

        statusList = new StatusList() {
            @Override
            public void addedFbUserNotify() {
                Log.e("testing: ", statusList.get(statusList.size()-1).toString());
            }
        };
        lvStatusLis = (ListView) rootView.findViewById(R.id.lvStatus);
        statusListAdapter = new StatusListAdapter(this, getContext(), R.layout.row_newsfeed, statusList);

        return rootView;
    }
}
