package com.example.hongsonpham.firstgreeting.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.model.adapter.SectionsPagerAdapter;

/**
 * Created by HongSonPham on 3/14/18.
 */

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        createTabIcons();
    }

    private void createTabIcons() {
        RelativeLayout videoCall = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_videocall, null);
        tabLayout.getTabAt(0).setCustomView(videoCall);
        RelativeLayout newsfeed = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_newsfeed, null);
        tabLayout.getTabAt(1).setCustomView(newsfeed);
        RelativeLayout chat = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_contact, null);
        tabLayout.getTabAt(2).setCustomView(chat);
        RelativeLayout profile = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_profile, null);
        tabLayout.getTabAt(3).setCustomView(profile);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }

}
