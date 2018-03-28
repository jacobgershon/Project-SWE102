package com.example.hongsonpham.firstgreeting.controller.profile;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongsonpham.firstgreeting.R;
import com.example.hongsonpham.firstgreeting.controller.extended_services.FirebaseAPI;
import com.example.hongsonpham.firstgreeting.controller.main.LoginActivity;
import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HongSonPham on 3/16/18.
 */

public class ProfileTab extends android.support.v4.app.Fragment {

    FirebaseAPI firebaseAPI;

    Button btnLogOut;
    ImageView imgCover;
    ImageButton imgAvatar;
    ImageButton imgZoomAvatar;
    TextView tvUserName;
    TextView tvDOB;
    TextView tvGender;
    TextView tvMail;

    String userId;
    FbUser user;

    private Rect startBounds;
    private Rect finalBounds;
    private Point globalOffset;
    private int mShortAnimationDuration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_profile, container, false);

        btnLogOut = (Button) rootView.findViewById(R.id.btnLogOut);
        imgAvatar = (ImageButton) rootView.findViewById(R.id.imgAvatar);
        imgZoomAvatar = (ImageButton) rootView.findViewById(R.id.imgZoomAvatar);
        imgCover = (ImageView) rootView.findViewById(R.id.imgCover);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvDOB = (TextView) rootView.findViewById(R.id.tvDOB);
        tvGender = (TextView) rootView.findViewById(R.id.tvGender);
        tvMail = (TextView) rootView.findViewById(R.id.tvMail);

        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(800);

        startBounds = new Rect();
        finalBounds = new Rect();
        globalOffset = new Point();


        init();


        imgCover.startAnimation(anim);

        setListener();

        return rootView;
    }

    private void init() {

        firebaseAPI = new FirebaseAPI();
        userId = getArguments().getString("fbId");
        getInformation();
        displayAvatar();
    }

    private void setData() {
        tvUserName.setText(user.getUserName());
        Picasso.with(getApplicationContext()).load(user.getUserAvatar()).into(imgAvatar);
        Picasso.with(getApplicationContext()).load(user.getUserAvatar()).into(imgZoomAvatar);
        Picasso.with(getApplicationContext()).load(user.getUserCover()).into(imgCover);
        tvDOB.setText("Date of birth: " + (!user.getUserDOB().isEmpty() ? user.getUserDOB() : "Unkown"));
        tvGender.setText("Gender: " + user.getUserGender());
        tvMail.setText("Email: " + user.getUserEmail());

    }

    private void getInformation() {
        firebaseAPI.getMyRef().child("user-node/FbUser/" + userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(FbUser.class);
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setListener() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                try {
                    zoomInImage();
                } catch (Exception e) {

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void displayAvatar() {
        imgAvatar.setClipToOutline(true);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.avatar_in);
        imgAvatar.startAnimation(animation);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private synchronized void zoomInImage() {

        imgAvatar.getGlobalVisibleRect(startBounds);
        imgZoomAvatar.getGlobalVisibleRect(finalBounds, globalOffset);
        float deltaWidth = (finalBounds.left + finalBounds.right) / 2 - (startBounds.right + startBounds.left) / 2;
        float deltaHeight = (finalBounds.top - startBounds.top) / 2;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgAvatar, View.SCALE_X, finalBounds.height() / startBounds.height());
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgAvatar, View.SCALE_Y, finalBounds.height() / startBounds.height());
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgAvatar, View.ROTATION, 0f, 360f);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(imgAvatar, "translationX", deltaWidth);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(imgAvatar, "translationY", deltaHeight);
        set.play(scaleDownX).with(scaleDownY).with(rotate).with(translateX).with(translateY);

        Handler handler = new Handler();

        if (imgAvatar.getClipToOutline()) {
            set.setDuration(700);
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    imgAvatar.setClipToOutline(false);
                }
            }, 300);
        } else {
            set.setDuration(500);
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    imgAvatar.setClipToOutline(true);
                }
            }, 250);
        }

        set.setInterpolator(new DecelerateInterpolator());
        set.start();
        imgAvatar.bringToFront();
    }

    private void goLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
