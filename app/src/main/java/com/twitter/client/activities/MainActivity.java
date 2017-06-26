package com.twitter.client.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.twitter.client.R;
import com.twitter.client.sharedpref.SharedPreferenceManager;
import com.twitter.client.utilities.Statics;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author MohamedSaleh on 24/6/2017
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetUsersFollowersTask().execute();
    }

    private class GetUsersFollowersTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... searchTerms) {
            String result = null;
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Statics.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Statics.CONSUMER_SECRET);
            // Access Token
            String access_token = SharedPreferenceManager.getInstance().getString(Statics.AUTH_TOKEN_KEY, "");
            // Access Token Secret
            String access_token_secret = SharedPreferenceManager.getInstance().getString(Statics.AUTH_SECRET_KEY, "");
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            try {
                PagableResponseList<twitter4j.User> ids = twitter.getFollowersList(SharedPreferenceManager.getInstance().getLong(Statics.USER_ID_KEY, 0), -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
