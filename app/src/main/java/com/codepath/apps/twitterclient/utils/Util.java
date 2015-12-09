package com.codepath.apps.twitterclient.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

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

    public static Transformation getRoundCornerTransform(){
         Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .oval(false)
                .build();
        return transformation;
    }

    public static void setNameAndUsername(TextView textView, String name, String username){
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

}
