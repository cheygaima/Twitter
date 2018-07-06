package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    //reference variables

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    MenuItem miActionProgressItem;


    private final int REQUEST_CODE = 20; //can we anything we want

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        Toolbar toptoolbar = (Toolbar) findViewById(R.id.toptoolbar);
        //Toolbar bottomtoolbar = (Toolbar) findViewById(R.id.bottomtoolbar);


        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toptoolbar);
        //setSupportActionBar(bottomtoolbar);


        client = TwitterApp.getRestClient(this);

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the arrayList
        tweets = new ArrayList<>();

        //construct the adapter from arraylist
        tweetAdapter = new TweetAdapter(tweets);

        //RV setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void populateTimeline()
    {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString()); //THIS WASNT ALWAYS UNCOMMENTED!!!!
                //iterate through the json array
                //for each entry, deserialize the json object
                for (int i = 0; i < response.length(); i++)
                {
                    //covert each object to a tweet model
                    //add that tweet model to our arraylist
                    //notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        }); //get data from the twitter api
    }

   public void whenClicked(MenuItem mi) {
       Intent intent = new Intent(this, ComposeActivity.class);
       startActivityForResult(intent, REQUEST_CODE);
   }


    // TA.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            Tweet composedTweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, composedTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

}




