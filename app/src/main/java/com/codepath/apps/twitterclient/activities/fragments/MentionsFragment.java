package com.codepath.apps.twitterclient.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.List;

public class MentionsFragment extends BaseTimelineFragment implements HomeFragmentStatePagerAdapter.FragmentInfo {


    public MentionsFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "@Mentions";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View createFragView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mentions, container, false);
    }

    @Override
    public int getSwipeContainerId() {
        return R.id.swipeRefreshMentions;
    }

    @Override
    public int getRecyclerId() {
        return R.id.rvTweetsMentions;
    }

    public void makeRequest(long maxId) {
        System.out.println("food");
        mClient.getUserMentions(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<TweetModel> tweets = parseTimelineResponse(response);
            }

        }, maxId);
    }
}
