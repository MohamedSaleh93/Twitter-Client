package com.twitter.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.twitter.client.utilities.Statics;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * @author MohamedSaleh on 5/27/2017.
 */

public class Program extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // initialize twitter kit
        TwitterConfig config = new TwitterConfig.Builder(this).
                twitterAuthConfig(new TwitterAuthConfig(Statics.CONSUMER_KEY,
                        Statics.CONSUMER_SECRET)).debug(true).logger(new DefaultLogger(Log.DEBUG))
                .build();
        Twitter.initialize(config);
    }

    public static Context getContext() {
        return context;
    }
}
