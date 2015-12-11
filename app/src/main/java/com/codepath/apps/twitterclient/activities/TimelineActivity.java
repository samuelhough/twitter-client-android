package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.TweetModel;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter tweetsAdapeter;
    private ArrayList<TweetModel> tweets;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;

    private HomeFragmentStatePagerAdapter homeAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeAdapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.vpPickTimeline);
        viewPager.setAdapter(homeAdapter);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewPager);

    }

}
