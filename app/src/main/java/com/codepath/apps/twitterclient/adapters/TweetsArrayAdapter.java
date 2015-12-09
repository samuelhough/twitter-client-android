package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.PostActivity;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by samuelhough on 12/7/15.
 * Takes tweet models - returns views
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.TweetsViewHolder>{

    TwitterClient client = RestApplication.getRestClient();
    public class TweetsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvTweet;
        TextView tvCreatedAt;
        TextView tvNameAndUsername;
        public TweetsViewHolder(View view){
            super(view);

            ivProfileImage = (ImageView) view.findViewById(R.id.ivProfilePhoto);
            tvTweet = (TextView) view.findViewById(R.id.tvTweet);
            tvCreatedAt = (TextView) view.findViewById(R.id.tvRelativeTime);
            tvNameAndUsername = (TextView) view.findViewById(R.id.tvNameAndUsername);
        }
    }

    List<TweetModel> tweets;
    public TweetsArrayAdapter(List<TweetModel> newTweets){
        tweets = newTweets;
    }

    @Override
    public void onBindViewHolder(final TweetsViewHolder holder, int position) {
        final TweetModel tweet = tweets.get(position);

        Picasso.with(holder.itemView.getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(Util.getRoundCornerTransform())
                .into(holder.ivProfileImage);

        holder.tvTweet.setText(tweet.getBody());
        holder.tvCreatedAt.setText(Util.getRelativeTimeAgo(tweet.getCreatedAt()));

        setNameAndUsername(holder.tvNameAndUsername,
                tweet.getUser().getName(),
                tweet.getUser().getScreenName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long tweetid = tweet.TweetId();
                Intent i = new Intent(holder.itemView.getContext(), PostActivity.class);
                i.putExtra("replyid", tweetid);
                holder.itemView.getContext().startActivity(i);
                return true;
            }
        });
    }

    private void setNameAndUsername(TextView textView, String name, String username){
        Context c = textView.getContext();

        ForegroundColorSpan grayTextForegroundSpan = new ForegroundColorSpan(
                c.getResources().getColor(R.color.name_gray)
        );

        AbsoluteSizeSpan fontsizeSpan = new AbsoluteSizeSpan(20);

        SpannableStringBuilder ssb = new SpannableStringBuilder(name + " @" + username);

        // Apply the color span
        ssb.setSpan(
                grayTextForegroundSpan,            // the span to add
                name.length() + 1,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder

        ssb.setSpan(
                fontsizeSpan,            // the span to add
                name.length() + 1,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ssb);
    }

    public TweetModel getLastTweet(){
        return tweets.get(tweets.size() - 1);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void setTweets(List<TweetModel> tweetList){
        tweets = tweetList;
        notifyDataSetChanged();
    }


    @Override
    public TweetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_layout, parent, false);
        TweetsViewHolder tweetHolder = new TweetsViewHolder(convertView);
        return tweetHolder;
    }
}
