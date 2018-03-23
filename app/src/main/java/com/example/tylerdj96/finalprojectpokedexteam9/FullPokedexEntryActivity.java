package com.example.tylerdj96.finalprojectpokedexteam9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.tylerdj96.finalprojectpokedexteam9.R;
import com.example.tylerdj96.finalprojectpokedexteam9.utils.NetworkUtils;
import com.example.tylerdj96.finalprojectpokedexteam9.utils.PokemonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FullPokedexEntryActivity extends AppCompatActivity {

    private final static String TAG = "fullpokedexentryactivity";

    private TextView mTVEntryName;
    private ImageView mIVEntryImage;
    private TextView mTVEntryDescription;

    private PokemonUtils.SearchResult mSearchResult;
    private PokemonUtils.DetailResult mDetailResult;

    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pokedex_entry);

        mTVEntryName = (TextView) findViewById(R.id.tv_entry_name);
        mIVEntryImage = (ImageView) findViewById(R.id.iv_entry_image);
        mTVEntryDescription = (TextView) findViewById(R.id.tv_entry_description);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PokemonUtils.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (PokemonUtils.SearchResult) intent.getSerializableExtra(PokemonUtils.EXTRA_SEARCH_RESULT);
            /*try {
                mDetailResult = (PokemonUtils.DetailResult)PokemonUtils.parseDetailResultJson(NetworkUtils.doHTTPGet("https://pokeapi.co/api/v2/pokemon"+mSearchResult.name));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //mTVEntryName.setText(mDetailResult.name);

            bitmap = null;
            /*try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(mDetailResult.sprite).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mIVEntryImage.setImageBitmap(bitmap);
            mTVEntryDescription.setText("Type: "+mDetailResult.type+"\n"+
                    "Height: "+mDetailResult.height+"   Weight:"+mDetailResult.weight+"\n");
            */
        }
        Log.d(TAG, mSearchResult.name);

        String query = Integer.toString(mSearchResult.entry_number);

        PokemonDetailsSearch(query);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_detail, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_pokemon:
                viewPokemonOnWeb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void viewPokemonOnWeb() {
        if (mDetailResult != null) {
            Uri bulbapediaURL = Uri.parse("https://bulbapedia.bulbagarden.net/wiki/" + mDetailResult.name);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, bulbapediaURL);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }

    private void PokemonDetailsSearch(String searchQuery) {
        //String githubSearchURL = PokemonUtils.buildGitHubSearchURL(searchQuery);
        //Log.d(TAG, "querying search URL: " + githubSearchURL);
        String detailsURL = "https://pokeapi.co/api/v2/pokemon/" + searchQuery;
        Log.d(TAG, detailsURL);
        new PokemonDetailTask().execute(detailsURL);
    }


    public class PokemonDetailTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String githubSearchURL = urls[0];

            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(githubSearchURL);
                PokemonUtils.DetailResult DetailsList = PokemonUtils.parseDetailResultJson(searchResults);
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(DetailsList.sprite).getContent());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            //mLoadingProgressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, s);
            if (s != null) {
                PokemonUtils.DetailResult DetailsList = PokemonUtils.parseDetailResultJson(s);
                mTVEntryName.setText(DetailsList.name);
                /*try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(DetailsList.sprite).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                mIVEntryImage.setImageBitmap(bitmap);
                mTVEntryDescription.setText("Type: " + DetailsList.type + "\n" +
                        "Height: " + DetailsList.height + "   Weight:" + DetailsList.weight + "\n");
            }
            //mGitHubSearchAdapter.updateSearchResults(searchResultsList);
            //mLoadingErrorMessage.setVisibility(View.INVISIBLE);
            //mSearchResultsRV.setVisibility(View.VISIBLE);
            else {
                //mSearchResultsRV.setVisibility(View.INVISIBLE);
                //mLoadingErrorMessage.setVisibility(View.VISIBLE);
            }
        }


    }
}