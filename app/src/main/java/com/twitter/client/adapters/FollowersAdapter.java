package com.twitter.client.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.client.R;
import com.twitter.client.activities.FollowerInfoActivity;
import com.twitter.client.utilities.Statics;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.PagableResponseList;
import twitter4j.User;

/**
 * @author MohamedSaleh on 6/27/2017.
 *         all credits goes to
 *         http://www.devexchanges.info/2017/02/android-recyclerview-dynamically-load.html
 */

public class FollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private List<User> mFollowersList;

    public FollowersAdapter(List<User> followersList, Activity activity) {
        mActivity = activity;
        mFollowersList = followersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.followers_list_item, parent, false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FollowersViewHolder) {
            final User follower = mFollowersList.get(position);
            FollowersViewHolder followersViewHolder = (FollowersViewHolder) holder;
            followersViewHolder.followerName.setText(follower.getName());
            // credits goes to https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html
            Picasso.with(mActivity).load(follower.getProfileImageURL()).into(followersViewHolder.followerImage);
            if (!follower.getDescription().equals("")) {
                followersViewHolder.followerBio.setText(follower.getDescription());
            } else {
                followersViewHolder.followerBio.setVisibility(View.GONE);
            }
            followersViewHolder.followerHandle.setText("@" + follower.getScreenName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, FollowerInfoActivity.class);
                    intent.putExtra(Statics.FOLLOWER_KEY, follower);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    private class FollowersViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView followerImage;
        private TextView followerName, followerHandle, followerBio;

        private FollowersViewHolder(View itemView) {
            super(itemView);
            followerImage = (CircleImageView) itemView.findViewById(R.id.followerImage);
            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerHandle = (TextView) itemView.findViewById(R.id.followerHandle);
            followerBio = (TextView) itemView.findViewById(R.id.followerBio);
        }
    }

    @Override
    public int getItemCount() {
        return mFollowersList.size();
    }
}
