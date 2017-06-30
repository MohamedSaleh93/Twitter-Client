package com.twitter.client.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.client.R;
import com.twitter.client.adapters.TweetsAdapter;
import com.twitter.client.sharedpref.SharedPreferenceManager;
import com.twitter.client.utilities.Statics;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author MohamedSaleh on 6/30/2017.
 */

public class FollowerInfoActivity extends Activity {

    private ImageView followerBG;
    private CircleImageView followerImage;
    private TextView followerName;
    private User follower;
    private ListView tweetsLV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_info);
        followerBG = (ImageView) findViewById(R.id.followerBG);
        followerImage = (CircleImageView) findViewById(R.id.followerProfileImage);
        followerName = (TextView) findViewById(R.id.followerUserName);
        tweetsLV = (ListView) findViewById(R.id.tweetsList);
        Intent intent = getIntent();
        follower = (User) intent.getSerializableExtra(Statics.FOLLOWER_KEY);
        if (follower.isProfileUseBackgroundImage()) {
            followerBG.setBackgroundResource(R.drawable.default_bg);
        } else {
            Picasso.with(this).load(follower.getProfileBackgroundImageURL()).into(followerBG);
        }
        Picasso.with(this).load(follower.getBiggerProfileImageURL()).into(followerImage);
        followerName.setText(follower.getName());

        new GetTweets().execute();

    }

    private class GetTweets extends AsyncTask<String, Void, ResponseList<Status>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FollowerInfoActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected ResponseList<twitter4j.Status> doInBackground(String... strings) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Statics.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Statics.CONSUMER_SECRET);
            // Access Token
            String access_token = SharedPreferenceManager.getInstance().getString(Statics.AUTH_TOKEN_KEY, "");
            // Access Token Secret
            String access_token_secret = SharedPreferenceManager.getInstance().getString(Statics.AUTH_SECRET_KEY, "");
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            ResponseList<twitter4j.Status> status = null;
            try {
                Paging paging = new Paging();
                paging.setCount(5);
                status = twitter.getUserTimeline(follower.getId(), paging);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(ResponseList<twitter4j.Status> s) {
            super.onPostExecute(s);
            if (s != null) {
                TweetsAdapter tweetsAdapter = new TweetsAdapter(FollowerInfoActivity.this, s);
                tweetsLV.setAdapter(tweetsAdapter);
            }
            progressDialog.dismiss();
        }
    }
}
