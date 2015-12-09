package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {

    Button btnPostTweet;
    EditText etEditTweet;
    TwitterClient client;
    TextView tvCharCount;
    long replyId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        client = RestApplication.getRestClient();
        btnPostTweet = (Button) findViewById(R.id.btnPostTweet);
        etEditTweet = (EditText) findViewById(R.id.etEditTweet);

        replyId = getIntent().getLongExtra("replyId", -1);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        setupButton();
        setupTypeHandler();
    }

    private void setupTypeHandler(){
        etEditTweet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                int val = 140 - etEditTweet.getText().toString().length();

                tvCharCount.setText(Integer.toString(val));

                return false;
            }
        });


    }

    private void setupButton(){
        btnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = etEditTweet.getText().toString();
                btnPostTweet.setVisibility(View.INVISIBLE);
                client.postTweet(tweet, replyId, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("Success");
                        super.onSuccess(statusCode, headers, response);
                        finish();
                    }

                    @Override
                    public void onFailure(
                            int statusCode,
                            Header[] headers,
                            String responseString,
                            Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }
}
