package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.serializers.TweetModelSerializer;
import com.codepath.apps.twitterclient.utils.Util;
import com.squareup.picasso.Picasso;

public class DetailTweet extends AppCompatActivity {

    TweetModel tweet;
    ImageView ivProfileImage;
    TextView tvNameAndUsername;
    TextView tvTweet;
    TextView tvRelativeDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        TweetModelSerializer tweetSerializer = (TweetModelSerializer) getIntent().getSerializableExtra("tweetM");
        tweet = tweetSerializer.getTweet();

        ivProfileImage = (ImageView) findViewById(R.id.ivProfilePhoto);
        tvNameAndUsername = (TextView) findViewById(R.id.tvNameAndUsername);
        tvTweet = (TextView) findViewById(R.id.tvTweet);
        tvRelativeDate = (TextView) findViewById(R.id.tvRelativeTime);

        tvTweet.setText(tweet.getBody());
        tvRelativeDate.setText(Util.getRelativeTimeAgo(tweet.getCreatedAt()));

        setupUserPhoto();
        setupUsername();
    }

    private void setupUserPhoto(){
        Picasso.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .transform(Util.getRoundCornerTransform())
                .into(ivProfileImage);
    }

    private void setupUsername(){
        Util.setNameAndUsername(tvNameAndUsername, tweet.getUser().getName(), tweet.getUser().getScreenName());
    }
}
