package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter tweetsAdapeter;
    private ArrayList<TweetModel> tweets;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = RestApplication.getRestClient();

        tweets = new ArrayList<>();
        tweetsAdapeter = new TweetsArrayAdapter(tweets);
        setupRecyclerView();
        fetchData(-1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
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
                TweetModel tweet = tweetsAdapeter.getLastTweet();
                if (tweet != null) {
                    fetchData(tweet.TweetId());
                }
            }
        });
    }

    private void fetchData(long maxId){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                tweets.addAll(TweetModel.fromJSONArray(response));
                tweetsAdapeter.notifyDataSetChanged();
            }
        }, maxId);
    }
}
