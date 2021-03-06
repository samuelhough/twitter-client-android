package com.codepath.apps.twitterclient;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class RestApplication extends com.activeandroid.app.Application {
	private static Context context;
	private String consumerKey;
	private String consumerSecret;
	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
	}

	public static TwitterClient getRestClient() {


		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}