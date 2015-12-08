package com.codepath.apps.twitterclient.utils;

import android.content.Context;
import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by samuelhough on 12/7/15.
 */
public class Util {
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public JSONObject loadJSONFromAsset(Context context) {
        String json = null;
        JSONObject jsonBlob = null;
        try {

            InputStream is = context.getAssets().open("config.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

            try {
                jsonBlob = new JSONObject(json);
            } catch(JSONException e){
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonBlob;

    }
}
