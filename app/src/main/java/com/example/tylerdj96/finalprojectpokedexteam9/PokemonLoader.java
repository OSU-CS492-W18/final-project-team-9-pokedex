package com.example.tylerdj96.finalprojectpokedexteam9;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.tylerdj96.finalprojectpokedexteam9.utils.NetworkUtils;

import java.io.IOException;

/**
 * Created by tylerdj96 on 3/13/2018.
 */

public class PokemonLoader extends AsyncTaskLoader<String> {
    private final static String TAG = PokemonLoader.class.getSimpleName();

    private String mSearchResultsJSON;
    private String mGitHubSearchURL;

    PokemonLoader(Context context, String url) {
        super(context);
        mGitHubSearchURL = url;
    }

    @Override
    protected void onStartLoading() {
        if (mGitHubSearchURL != null) {
            if (mSearchResultsJSON != null) {
                Log.d(TAG, "loader returning cached results");
                deliverResult(mSearchResultsJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Override
    public String loadInBackground() {
        if (mGitHubSearchURL != null) {
            Log.d(TAG, "loading results from Pokemon with URL: " + mGitHubSearchURL);
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(mGitHubSearchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        } else {
            return null;
        }
    }

    @Override
    public void deliverResult(String data) {
        mSearchResultsJSON = data;
        super.deliverResult(data);
    }
}
