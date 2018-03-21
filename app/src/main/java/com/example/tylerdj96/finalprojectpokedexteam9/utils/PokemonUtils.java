package com.example.tylerdj96.finalprojectpokedexteam9.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by tylerdj96 on 3/13/2018.
 */

public class PokemonUtils {
    public static final String EXTRA_SEARCH_RESULT = "PokemonUtils.SearchResult";

    final static String POKEDEX_BASE_URL = "https://pokeapi.co/api/v2/pokedex/";
    final static String GITHUB_SEARCH_QUERY_PARAM = "q";
    final static String GITHUB_SEARCH_SORT_PARAM = "sort";

    public static class SearchResult implements Serializable {
        public String pokemonURL;
        public String name;
        public String description;
        public int entry_number;
    }

    /*public static String buildPokemonURL(String searchQuery, String sort, String language,
                                              String user, boolean searchInName, boolean searchInDescription,
                                              boolean searchInReadme) {

        Uri.Builder builder = Uri.parse(GITHUB_SEARCH_BASE_URL).buildUpon();

        if (!TextUtils.isEmpty(sort)) {
            builder.appendQueryParameter(GITHUB_SEARCH_SORT_PARAM, sort);
        }

        String queryValue = new String(searchQuery);
        if (!TextUtils.isEmpty(language)) {
            queryValue += " language:" + language;
        }
        if (!TextUtils.isEmpty(user)) {
            queryValue += " user:" + user;
        }

        ArrayList<String> searchInList = new ArrayList<>();
        if (searchInName) {
            searchInList.add("name");
        }
        if (searchInDescription) {
            searchInList.add("description");
        }
        if (searchInReadme) {
            searchInList.add("readme");
        }
        if (!searchInList.isEmpty()) {
            queryValue += " in:" + TextUtils.join(",", searchInList);
        }

        builder.appendQueryParameter(GITHUB_SEARCH_QUERY_PARAM, queryValue);

        return builder.build().toString();
    }*/

    public static ArrayList<SearchResult> parseSearchResultsJSON(String searchResultsJSON, String searchcriteria) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("pokemon_entries");

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                SearchResult result = new SearchResult();
                JSONObject resultItemElem = searchResultsItems.getJSONObject(i);
                result.entry_number = resultItemElem.getInt("entry_number");

                JSONObject resultItemInner = resultItemElem.getJSONObject("pokemon_species");
                result.name = resultItemInner.getString("name");
                result.pokemonURL = resultItemInner.getString("url");

                Log.d(TAG, result.name);
                Log.d(TAG, searchcriteria);

                if(searchcriteria.equals(result.name)) {

                    searchResultsList.add(result);
                }
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }
}
