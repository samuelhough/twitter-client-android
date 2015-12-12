package com.codepath.apps.twitterclient.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.DetailTweet;
import com.codepath.apps.twitterclient.activities.PostActivity;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.serializers.TweetModelSerializer;
import com.codepath.apps.twitterclient.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by samuelhough on 12/7/15.
 * Takes tweet models - returns views
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.TweetsViewHolder>{

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

        Util.setNameAndUsername(holder.tvNameAndUsername,
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), DetailTweet.class);
                i.putExtra("tweetM", new TweetModelSerializer(tweet));
                holder.itemView.getContext().startActivity(i);
            }
        });

        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                holder.itemView.getContext().startActivity(i);
            }
        });

    }


    public TweetModel getLastTweet(){
        if (tweets.size() > 0){
            return tweets.get(tweets.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        System.out.println(Integer.toString(tweets.size()));
        return tweets.size();
    }

    @Override
    public TweetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_layout, parent, false);
        return new TweetsViewHolder(convertView);
    }
}
