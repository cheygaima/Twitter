package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets)
    {
        mTweets = tweets;
    }

    //for each row, inflate the layout and pass them into viewholder class

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(tweet.relativeDate);
        holder.tvAt.setText(tweet.user.screenName);

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount()
    {
        return mTweets.size();
    }


    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public EditText etTweet;
        public TextView tvAt;

        public ViewHolder(View itemView)
        {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            etTweet = (EditText) itemView.findViewById(R.id.et_Tweet);
            tvAt = (TextView) itemView.findViewById(R.id.tvAt);


            //store the context

            //attach a click listener to the entire row view

        }


    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
