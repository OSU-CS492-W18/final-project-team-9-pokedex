package com.example.tylerdj96.finalprojectpokedexteam9.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.tylerdj96.finalprojectpokedexteam9.MainActivity;

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

    private final static String TAG = "pokemonutils";

    final static String POKEDEX_BASE_URL = "https://pokeapi.co/api/v2/pokedex/";
    final static String GITHUB_SEARCH_QUERY_PARAM = "q";
    final static String GITHUB_SEARCH_SORT_PARAM = "sort";

    public static class SearchResult implements Serializable {
        public String pokemonURL;
        public String name;
        public String description;
        public int entry_number;
    }

    public static class DetailResult implements Serializable{
        public String name;
        public String sprite; // should be sprite.front_default, gets a URL
        public String type;
        public int height;
        public int weight;
        public int id;
        public int spd;
        public int atk;
        public int def;
        public int spAtk;
        public int spDef;
        public int hp;
        public int totStats;
    }

    public static DetailResult parseDetailResultJson(String detailResultJSON){
        try{
            JSONObject detailResultObj = new JSONObject(detailResultJSON);
            DetailResult entry = new DetailResult();
            entry.name = detailResultObj.getString("name");
            entry.name = entry.name.substring(0,1).toUpperCase() + entry.name.substring(1);
            entry.id = detailResultObj.getInt("id");
            entry.height = detailResultObj.getInt("height");
            entry.weight = detailResultObj.getInt("weight");



            if(detailResultObj.getJSONArray("types").getJSONObject(0).getInt("slot") == 2 ) {
                String type1 = detailResultObj.getJSONArray("types").getJSONObject(1).getJSONObject("type").getString("name");
                type1 = type1.substring(0, 1).toUpperCase() + type1.substring(1);
                String type2 = detailResultObj.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");
                type2 = type2.substring(0, 1).toUpperCase() + type2.substring(1);
                entry.type = type1+", "+type2;
            } else {
                String type = detailResultObj.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");
                type = type.substring(0, 1).toUpperCase() + type.substring(1);
                entry.type = type;
            }
            entry.sprite = detailResultObj.getJSONObject("sprites").getString("front_default");
            entry.spd = detailResultObj.getJSONArray("stats").getJSONObject(0).getInt("base_stat");
            entry.spDef = detailResultObj.getJSONArray("stats").getJSONObject(1).getInt("base_stat");
            entry.spAtk = detailResultObj.getJSONArray("stats").getJSONObject(2).getInt("base_stat");
            entry.def = detailResultObj.getJSONArray("stats").getJSONObject(3).getInt("base_stat");
            entry.atk = detailResultObj.getJSONArray("stats").getJSONObject(4).getInt("base_stat");
            entry.hp = detailResultObj.getJSONArray("stats").getJSONObject(5).getInt("base_stat");
            entry.totStats = entry.atk+entry.spAtk+entry.spDef+entry.hp+entry.def+entry.spd;


            Log.d(TAG, entry.name);
            Log.d(TAG, Integer.toString(entry.id));
            Log.d(TAG, Integer.toString(entry.weight));
            Log.d(TAG, Integer.toString(entry.height));
            //Log.d(TAG, entry.weight);
            //rLog.d(TAG, entry.height);
            Log.d(TAG, entry.type);
            Log.d(TAG, entry.sprite);
            return entry;
        } catch (JSONException e) {
            return null;
            //e.printStackTrace();
        }
    }

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

                //Log.d(TAG, result.name);
                //Log.d(TAG, searchcriteria);

                if(result.name.contains(searchcriteria)) {
                    result.name = result.name.substring(0, 1).toUpperCase() + result.name.substring(1);
                    searchResultsList.add(result);
                }
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
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
}
