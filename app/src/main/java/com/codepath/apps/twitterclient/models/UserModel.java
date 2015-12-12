package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by samuelhough on 12/7/15.
 */
@Table(name = "users")
public class UserModel extends Model implements Serializable {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "name")
    private String name;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    private String profileBgImageUrl;

    private int followingCount;

    private int followersCount;

    private int tweetCount;

    public int getFollowingCount() {
        return followingCount;
    }

    public int getTweetCount(){
        return tweetCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBgImageUrl() {
        return profileBgImageUrl;
    }

    public static UserModel fromJson(JSONObject json){
        UserModel user = new UserModel();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.profileBgImageUrl = json.getString("profile_background_image_url");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.tweetCount = json.getInt("statuses_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
