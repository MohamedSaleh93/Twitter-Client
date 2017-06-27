package com.twitter.client.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.client.R;
import com.twitter.client.interfaces.OnLoadMoreListener;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.PagableResponseList;
import twitter4j.User;

/**
 * @author MohamedSaleh on 6/27/2017.
 * all credits goes to
 * http://www.devexchanges.info/2017/02/android-recyclerview-dynamically-load.html
 */

public class FollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnLoadMoreListener onLoadMoreListener;
    private Activity mActivity;
    private PagableResponseList<User> mFollowersList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private final int VIEW_FOLLOWERS = 0;
    private final int VIEW_LOADING = 1;

    public FollowersAdapter(RecyclerView recyclerView, PagableResponseList<User> followersList,
                            Activity activity) {
        mActivity = activity;
        mFollowersList = followersList;

        final LinearLayoutManager screenLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = screenLayoutManager.getItemCount();
                lastVisibleItem = screenLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_FOLLOWERS) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.followers_list_item, parent,
                    false);
            return new FollowersViewHolder(view);
        } else if (viewType == VIEW_LOADING) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.followers_loading, parent,
                    false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FollowersViewHolder) {
            User follower = mFollowersList.get(position);
            FollowersViewHolder followersViewHolder = (FollowersViewHolder) holder;
            followersViewHolder.followerName.setText(follower.getName());
            // credits goes to https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html
            Picasso.with(mActivity).load(follower.getProfileImageURL()).into(followersViewHolder.followerImage);
            if (!follower.getDescription().equals("")) {
                followersViewHolder.followerBio.setText(follower.getDescription());
            } else {
                followersViewHolder.followerBio.setVisibility(View.GONE);
            }
            followersViewHolder.followerHandle.setText(follower.getScreenName());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.loadingProgressBar.setIndeterminate(true);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar loadingProgressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            loadingProgressBar = (ProgressBar) itemView.findViewById(R.id.loadingProgress);
        }
    }

    private class FollowersViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView followerImage;
        private TextView followerName, followerHandle, followerBio;
        public FollowersViewHolder(View itemView) {
            super(itemView);
            followerImage = (CircleImageView) itemView.findViewById(R.id.followerImage);
            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerHandle = (TextView) itemView.findViewById(R.id.followerHandle);
            followerBio = (TextView) itemView.findViewById(R.id.followerBio);
        }
    }

    public void setLoading() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mFollowersList.size();
    }
}
