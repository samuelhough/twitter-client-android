package com.codepath.apps.twitterclient.models.serializers;

import com.codepath.apps.twitterclient.models.TweetModel;

import java.io.Serializable;

/**
 * Created by samuelhough on 12/3/15.
 */
public class TweetModelSerializer implements Serializable {
    private TweetModel tweet;

    public TweetModelSerializer(TweetModel newTweet){
        tweet = newTweet;
    }

    public TweetModel getTweet(){
         return tweet;
    }
}
