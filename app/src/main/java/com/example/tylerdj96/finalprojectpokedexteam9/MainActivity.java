package com.example.tylerdj96.finalprojectpokedexteam9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tylerdj96.finalprojectpokedexteam9.utils.PokemonUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements PokemonAdapter.OnSearchItemClickListener, LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = MainActivity.class.getSimpleName();

    private final static String SEARCH_URL_KEY = "PokemonSearchURL";

    private final static int Pokemon_SEARCH_LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private PokemonAdapter mPokemonSearchAdapter;
    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingErrorMessage;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessage = (TextView)findViewById(R.id.tv_loading_error);

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mPokemonSearchAdapter = new PokemonAdapter(this);
        mSearchResultsRV.setAdapter(mPokemonSearchAdapter);

        if (savedInstanceState != null) {
            mSearchBoxET.setText(savedInstanceState.getString("textKey"));
        }



        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchQuery = mSearchBoxET.getText().toString();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doPokemonSearch(searchQuery);
                }
            }
        });



        getSupportLoaderManager().initLoader(Pokemon_SEARCH_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("textKey", mSearchBoxET.getText().toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }*/

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/

    private void doPokemonSearch(String searchQuery) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String pokedex = sharedPreferences.getString(
                getString(R.string.pref_pokedex_key),
                getString(R.string.pref_pokedex_default)
        );


        Bundle args = new Bundle();
        String temp = "https://pokeapi.co/api/v2/pokedex/" + pokedex + "/";
        args.putString(SEARCH_URL_KEY, temp);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(Pokemon_SEARCH_LOADER_ID, args, this);
    }

    @Override
    public void onSearchItemClick(PokemonUtils.SearchResult searchResult) {
        Intent detailedSearchResultIntent = new Intent(this, FullPokedexEntryActivity.class);
        detailedSearchResultIntent.putExtra(PokemonUtils.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(detailedSearchResultIntent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String PokemonSearchURL = null;
        if (args != null) {
            PokemonSearchURL = args.getString(SEARCH_URL_KEY);
        }
        return new PokemonLoader(this, PokemonSearchURL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        String searchcriteria = searchQuery.toLowerCase();
        Log.d(TAG, "got results from loader");
        if (data != null) {
            //Log.d(TAG, searchcriteria);
            ArrayList<PokemonUtils.SearchResult> searchResults = PokemonUtils.parseSearchResultsJSON(data, searchcriteria);
            mPokemonSearchAdapter.updateSearchResults(searchResults);
            mLoadingErrorMessage.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }


}
