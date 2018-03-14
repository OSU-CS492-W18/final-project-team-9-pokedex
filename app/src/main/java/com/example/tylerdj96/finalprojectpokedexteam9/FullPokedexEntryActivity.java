package com.example.tylerdj96.finalprojectpokedexteam9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tylerdj96.finalprojectpokedexteam9.utils.PokemonUtils;

public class FullPokedexEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pokedex_entry);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PokemonUtils.EXTRA_SEARCH_RESULT)) {
            mForecastItem = (PokemonUtils.SearchResult)intent.getSerializableExtra(
                    PokemonUtils.EXTRA_SEARCH_RESULT
            );
            fillInLayoutText(mForecastItem);
        }
    }
}
