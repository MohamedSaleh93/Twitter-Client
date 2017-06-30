package com.twitter.client.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.client.R;
import com.twitter.client.adapters.FollowersAdapter;
import com.twitter.client.sharedpref.SharedPreferenceManager;
import com.twitter.client.utilities.Statics;
import com.twitter.client.utilities.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author MohamedSaleh on 24/6/2017
 */
public class MainActivity extends AppCompatActivity {

    private FollowersAdapter followersAdapter;
    private RecyclerView followersRecyclerView;
    private PagableResponseList<User> followersList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        followersRecyclerView = (RecyclerView) findViewById(R.id.followersRecycler);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgress);
        followersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new GetUsersFollowersTask().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetUsersFollowersTask().execute();
            }
        });
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
            long cursor = -1;
            try {
                if (Util.isConnectToInternet(MainActivity.this)) {
                    while (cursor != 0) {
                        PagableResponseList<User> followersFromServer = twitter.getFollowersList(SharedPreferenceManager.getInstance().
                                getLong(Statics.USER_ID_KEY, 0), cursor);
                        cursor = followersFromServer.getNextCursor();
                        if (followersList == null) {
                            followersList = followersFromServer;
                        } else {
                            followersList.addAll(followersFromServer);
                        }
                    }
                    saveFollowersInCache(followersList);
                } else {
                    followersList = readFollowersFromCache();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            followersRecyclerView.setVisibility(View.VISIBLE);
            if (followersList != null) {
                followersAdapter = new FollowersAdapter(followersList, MainActivity.this);
                followersRecyclerView.setAdapter(followersAdapter);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.failed_loading_followers), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveFollowersInCache(PagableResponseList<User> object) {
        try {
            FileOutputStream fos = openFileOutput(Statics.FOLLOWER_KEY, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private PagableResponseList<User> readFollowersFromCache() {
        try {
            FileInputStream fis = openFileInput(Statics.FOLLOWER_KEY);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return (PagableResponseList<User>) object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
