package com.codepath.apps.twitterclient.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.PostActivity;
import com.codepath.apps.twitterclient.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseTimelineFragment extends Fragment implements HomeFragmentStatePagerAdapter.FragmentInfo {

    private TwitterClient mClient;
    private TweetsArrayAdapter mTweetsAdapeter;
    private SwipeRefreshLayout mSwipeContainer;
    private ArrayList<TweetModel> mTweets;
    private RecyclerView mRecyclerView;
    private View mView;

    public BaseTimelineFragment() {}
    public abstract View createFragView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    public abstract String getFragmentTitle();
    public abstract int getRecyclerId();
    public abstract int getSwipeContainerId();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = createFragView(inflater, container, savedInstanceState);
        setupStuff();
        setHasOptionsMenu(true);
        return mView;
    }

    private void setupStuff(){
        mClient = RestApplication.getRestClient();

        mTweets = new ArrayList<>();
        mTweetsAdapeter = new TweetsArrayAdapter(mTweets);
        mSwipeContainer = (SwipeRefreshLayout) mView.findViewById(getSwipeContainerId());

        setupRecyclerView();
        setupRefreshTimeline();


        List<TweetModel> savedTweets = TweetModel.getAll();
        if (savedTweets.size() > 0){
            System.out.println(Integer.toString(savedTweets.size())+ " loaded");
            mTweets.addAll(savedTweets);
            mTweetsAdapeter.notifyDataSetChanged();
            System.out.println(Integer.toString(mTweets.size()));
        } //else {
//        fetchData(true);
//        }

    }

    private void setupRefreshTimeline(){
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(true);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logged_in_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.post_timeline){
            Intent i = new Intent(mView.getContext(), PostActivity.class);
            startActivity(i);
        }
        return true;
    }

    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) mView.findViewById(getRecyclerId());
        mRecyclerView.setAdapter(mTweetsAdapeter);
        LinearLayoutManager manager = new LinearLayoutManager(mView.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {

            public void onLoadMore(int page, int totalItemsCount) {
                fetchData(false);
            }
        });
    }

    private void fetchData(boolean clear){
        if(clear){
            mTweets.clear();
            mTweetsAdapeter.notifyDataSetChanged();
        }

        long maxId;
        TweetModel tweet = mTweetsAdapeter.getLastTweet();
        if (tweet != null) {
            maxId = tweet.TweetId();
        } else {
            maxId = -1;
        }

        mClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<TweetModel> newTweets = TweetModel.fromJSONArray(response);
                mTweets.addAll(newTweets);

                saveTweets(newTweets);

                mTweetsAdapeter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);
                System.out.println("fetched");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mSwipeContainer.setRefreshing(false);
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
