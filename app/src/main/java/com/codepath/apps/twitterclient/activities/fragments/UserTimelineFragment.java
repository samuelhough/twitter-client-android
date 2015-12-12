package com.codepath.apps.twitterclient.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by samuelhough on 12/11/15.
 */
public class UserTimelineFragment extends BaseTimelineFragment {

    public interface CommunicationPath {
        public void catchUserModel(UserModel user);
    }

    public static UserTimelineFragment newInstance(String screenName) {
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    CommunicationPath mActivity;
    public void setParentActivity(CommunicationPath activity){
        mActivity = activity;
    }

    @Override
    public int getSwipeContainerId() {
        return R.id.swipeRefreshTimeline;
    }

    @Override
    public int getRecyclerId() {
        return R.id.rvTweetsTimeline;
    }

    @Override
    public View createFragView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void makeRequest(long maxId) {
        mClient.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<TweetModel> tweets = parseTimelineResponse(response);

                if (mActivity != null){
                    mActivity.catchUserModel(tweets.get(0).getUser());
                }

            }
        }, getArguments().getString("screen_name",null));
    }
}
