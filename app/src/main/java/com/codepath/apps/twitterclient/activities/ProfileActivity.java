package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.UserModel;
import com.codepath.apps.twitterclient.utils.Util;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements UserTimelineFragment.CommunicationPath {

    FrameLayout mFrameLayout;
    TwitterClient client;
    UserModel user;
    Toolbar toolbar;
    ImageView ivBgPhoto;
    ImageView ivProfileImage;
    TextView tvUsername;
    TextView tvName;
    TextView tvFollowers;
    TextView tvFollowing;
    TextView tvTweetsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screeName = getIntent().getStringExtra("screen_name");
        UserTimelineFragment userTimeline = UserTimelineFragment.newInstance(screeName);
        userTimeline.setParentActivity(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = RestApplication.getRestClient();
        setupViews();

        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.flContent, userTimeline);
            transaction.commit();
        }
    }

    private void setupViews(){
        ivBgPhoto = (ImageView) findViewById(R.id.bg_photo);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfilePhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvTweetsCount = (TextView) findViewById(R.id.tvTweetsCount);
    }

    public void catchUserModel(UserModel user){
        populateView(user);
    }

    public void populateView(UserModel user){
        toolbar.setTitle(user.getScreenName());
        tvFollowers.setText(Integer.toString(user.getFollowersCount()) + " followers");
        tvFollowing.setText(Integer.toString(user.getFollowingCount()) + " following");
        tvUsername.setText("@"+ user.getScreenName());
        tvName.setText(user.getName());
        tvTweetsCount.setText(Integer.toString(user.getTweetCount()) + " tweets");
        loadBgImage(user);
        loadProfileImage(user);
    }

    private void loadBgImage(UserModel user){
        Picasso.with(this)
                .load(user.getProfileBgImageUrl())
                .fit()
                .centerCrop()
                .into(ivBgPhoto);
    }

    private void loadProfileImage(UserModel user){
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .fit()
                .centerCrop()
                .transform(Util.getRoundCornerTransform())
                .into(ivProfileImage);

    }

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}
