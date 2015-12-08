package com.example.msonasath.instaclient.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.msonasath.instaclient.models.InstaPhoto;
import com.example.msonasath.instaclient.adapters.InstaPhotosAdapter;
import com.example.msonasath.instaclient.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstaPhoto> photos;
    private InstaPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // Send out API request to popular photos
        fetchPopularPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchPopularPhotos() {
        /*
        CLIENT ID: b434e0cc12b04d9197c2863848eaefcd
        URL: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
             https://api.instagram.com/v1/media/popular?client_id=CLIENT_ID
        */
        photos = new ArrayList<>();
        //1. create the adapter linking to the source
        aPhotos = new InstaPhotosAdapter(this, photos);
        //2. find the list view from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //3. set the adapter binding it to the list view
        lvPhotos.setAdapter(aPhotos);

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        //first check if network connection is available
        if(isConnectedToNetwork() == false) {
            CharSequence toastMsg = "Please check your network connection!";
            Toast toast = Toast.makeText(this.getApplicationContext(), toastMsg, Toast.LENGTH_LONG);
            toast.show();
            swipeContainer.setRefreshing(false);
            return;
        }

        //trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {

            //onSuccess (worked, 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // expecting a JSON object
                // iterate each of the photo items and decode the item into java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //array of posts
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get the JSON object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of json into a data model
                        InstaPhoto photo = new InstaPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.userImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.userFullName = photoJSON.getJSONObject("user").getString("full_name");
                        photo.createdTime = photoJSON.getLong("created_time");
                        //Add decoded objects to photos array
                        photos.add(photo);
                        aPhotos.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //onfailure (failed,)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                CharSequence toastMsg = "HTTP Request failed";
                Toast toast = Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG);
                toast.show();
                swipeContainer.setRefreshing(false);
                Log.i("DEBUG", "HTTP Request failed");
            }
        });

    }

    private boolean isConnectedToNetwork()
    {
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            Log.e("NETWORK", "isConnectedToNetwork return true");
            return true;
        }
        else {
            Log.e("NETWORK", "isConnectedToNetwork return false");
            return false;
        }

    }


}
