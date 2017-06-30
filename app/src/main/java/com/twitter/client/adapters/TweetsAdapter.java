package com.twitter.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twitter.client.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * @author MohamedSaleh on 6/30/2017.
 */

public class TweetsAdapter extends BaseAdapter {

    private Context mContext;
    private ResponseList<Status> statusList;

    public TweetsAdapter(Context context, ResponseList<Status> statuses) {
        this.mContext = context;
        this.statusList = statuses;
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    @Override
    public Object getItem(int i) {
        return statusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return statusList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            vi = layoutInflater.inflate(R.layout.tweets_list_item, null);
        }
        final LinearLayout tweetsLayout = (LinearLayout) vi.findViewById(R.id.tweetsLayout);
        TweetUtils.loadTweet(statusList.get(i).getId(), new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                final TweetView tweetView = new TweetView(mContext, result.data);
                tweetsLayout.addView(tweetView);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(mContext, mContext.getString(R.string.failed_loading_tweet), Toast.LENGTH_LONG).show();
            }
        });
        return vi;
    }
}
