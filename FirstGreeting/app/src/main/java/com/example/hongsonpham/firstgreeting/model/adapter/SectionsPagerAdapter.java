package com.example.hongsonpham.firstgreeting.model.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hongsonpham.firstgreeting.controller.message.ContactTab;
import com.example.hongsonpham.firstgreeting.controller.newsfeed.NewsfeedTab;
import com.example.hongsonpham.firstgreeting.controller.profile.ProfileTab;
import com.example.hongsonpham.firstgreeting.controller.videocall.VideoCallTab;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private String fbId;
    private Bundle bundle;

    public SectionsPagerAdapter(FragmentManager fm, String fbId) {
        super(fm);
        this.bundle = new Bundle();
        bundle.putString("fbId in Section", fbId);
        this.fbId = fbId;
    }

    @Override
    public Fragment getItem(int position) {
        bundle.putString("fbId", fbId);
        switch (position) {
            case 0:
                VideoCallTab videoCall = new VideoCallTab();
                videoCall.setArguments(bundle);
                return videoCall;
            case 1:
                NewsfeedTab newsFeed = new NewsfeedTab();
                newsFeed.setArguments(bundle);
                return newsFeed;
            case 2:
                ContactTab contact = new ContactTab();
                contact.setArguments(bundle);
                return contact;
            case 3:
                ProfileTab profile = new ProfileTab();
                profile.setArguments(bundle);
                return profile;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Video Call";
            case 1:
                return "News Feed";
            case 2:
                return "Contact";
            case 3:
                return "Profile";
        }
        return null;
    }
}
