package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "tweets")
public class TweetModel extends Model implements Serializable {

	@Column(name = "tweetId",  unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long tweetId;

	@Column(name = "body")
	private String body;

	@Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	private UserModel user;

	@Column(name = "createdAt")
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

	public static List<TweetModel> getAll() {
		return new Select("*")
				.from(TweetModel.class)
				.execute();
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


	// Record Finders
	public static TweetModel byId(long id) {
		return new Select().from(TweetModel.class).where("id = ?", id).executeSingle();
	}

	public static List<TweetModel> recentItems() {
		return new Select().from(TweetModel.class).orderBy("id DESC").limit("300").execute();
	}

	public void saveWithDependencies(){
		user.save();
		save();
	}
}
