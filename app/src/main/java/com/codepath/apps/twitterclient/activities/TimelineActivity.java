package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter tweetsAdapeter;
    private ArrayList<TweetModel> tweets;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = RestApplication.getRestClient();

        tweets = new ArrayList<>();
        tweetsAdapeter = new TweetsArrayAdapter(tweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshTimeline);

        setupRecyclerView();
        setupRefreshTimeline();


        List<TweetModel> savedTweets = TweetModel.getAll();
        if (savedTweets.size() > 0){
            System.out.println(Integer.toString(savedTweets.size())+ " loaded");
            tweets.addAll(savedTweets);
            tweetsAdapeter.notifyDataSetChanged();
        } else {
            fetchData();
        }

    }

    private void setupRefreshTimeline(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logged_in_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.post_timeline){
            Intent i = new Intent(this, PostActivity.class);
            startActivity(i);
        }
        return true;
    }

    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rvTweets);
        mRecyclerView.setAdapter(tweetsAdapeter);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {

            public void onLoadMore(int page, int totalItemsCount) {
                fetchData();
            }
        });
    }

    private void fetchData(){
        long maxId;
        TweetModel tweet = tweetsAdapeter.getLastTweet();
        if (tweet != null) {
            maxId = tweet.TweetId();
        } else {
            maxId = -1;
        }

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<TweetModel> newTweets = TweetModel.fromJSONArray(response);
                tweets.addAll(newTweets);

                saveTweets(newTweets);

                tweetsAdapeter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeContainer.setRefreshing(false);
            }
        }, maxId);
    }

    public void saveTweets(List<TweetModel> newTweets){
        ActiveAndroid.beginTransaction();

        try {
            int count = newTweets.size();
            for (int cur = 0; cur < count; cur++) {
                newTweets.get(cur).saveWithDependencies();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }


    }
}
