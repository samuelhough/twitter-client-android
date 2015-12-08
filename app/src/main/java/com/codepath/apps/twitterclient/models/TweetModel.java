package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "items")
public class TweetModel extends Model {
	// Define table fields
	@Column(name = "name")
	private String name;

	private String body;
	private long tweetId;
	private UserModel user;
	private String createdAt;

	public String getBody() {
		return body;
	}

	public long TweetId() {
		return tweetId;
	}

	public UserModel getUser() {
		return user;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public static TweetModel fromJSON(JSONObject json){
		TweetModel tweet = new TweetModel();
		try {
			tweet.body = json.getString("text");
			tweet.tweetId = json.getLong("id");
			tweet.createdAt = json.getString("created_at");
			tweet.user = UserModel.fromJson(json.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}

	public static List<TweetModel> fromJSONArray(JSONArray jsonArr){
		ArrayList<TweetModel> tweetList = new ArrayList<>();

		for (int i = 0; i < jsonArr.length(); i++){
			try {
				tweetList.add(TweetModel.fromJSON(jsonArr.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
		}

		return tweetList;
	}

	public TweetModel() {
		super();
	}

	// Parse model from JSON
	public TweetModel(JSONObject object){
		super();

		try {
			this.name = object.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	// Record Finders
	public static TweetModel byId(long id) {
		return new Select().from(TweetModel.class).where("id = ?", id).executeSingle();
	}

	public static List<TweetModel> recentItems() {
		return new Select().from(TweetModel.class).orderBy("id DESC").limit("300").execute();
	}
}
