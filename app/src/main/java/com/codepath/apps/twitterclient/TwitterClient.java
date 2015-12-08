package com.codepath.apps.twitterclient;

import android.content.Context;

import com.codepath.apps.twitterclient.utils.Util;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static String REST_CONSUMER_KEY = "";
	public static String REST_CONSUMER_SECRET = "";
	public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)
	public static final String HOME_TIMELINE = "statuses/home_timeline.json";
	public static final String POST_TWEET = "statuses/update.json";
	public static int MAX_TWEET_LENGTH = 140;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, getConsumerKey(context), getConsumerSecret(context),
				REST_CALLBACK_URL);
	}

	public static String getConsumerKey(Context context) {
		JSONObject json = getConfigJson(context);
		try {
			return json.getString("CONSUMER_KEY");
		} catch(JSONException e){
			e.printStackTrace();
		}
		return "";
	}

	public static String getConsumerSecret(Context context){
		JSONObject json = getConfigJson(context);
		try {
			return json.getString("CONSUMER_SECRET");
		} catch(JSONException e){
			e.printStackTrace();
		}
		return "";
	}

	public static JSONObject getConfigJson(Context context){
		Util util = new Util();
		JSONObject json = util.loadJSONFromAsset(context);
		return json;
	}


	public void getHomeTimeline(JsonHttpResponseHandler handler, long max_id){
		System.out.println("Fetching data");
		String apiurl = getApiUrl(HOME_TIMELINE);
		System.out.println(apiurl);
		RequestParams params = new RequestParams();
		params.put("count", 25);

		if (max_id != -1) {
			params.put("max_id", max_id);
		}
		getClient().get(apiurl, params, handler);
	}

	public boolean postTweet(String tweet, long replyId, JsonHttpResponseHandler handler){
		if (tweet.length() <= MAX_TWEET_LENGTH){
			String postUrl = getApiUrl(POST_TWEET);
			System.out.println(postUrl);
			RequestParams params = new RequestParams();
			params.put("status", tweet);
			if (replyId != -1){
				System.out.println(Long.toString(replyId));
				params.put("in_reply_to_status_id", replyId);
			}

			getClient().post(postUrl, params, handler);
			return true;
		} else {
			return false;
		}
	}
}