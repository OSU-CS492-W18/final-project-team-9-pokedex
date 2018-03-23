package com.example.tylerdj96.finalprojectpokedexteam9;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class FullPokedexEntryActivity extends AppCompatActivity {

    private TextView mTVEntryName;
    private ImageView mIVEntryImage;
    private TextView mTVEntryDescription;

    private PokemonUtils.SearchResult mSearchResult;
    private PokemonUtils.DetailResult mDetailResult;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pokedex_entry);

        mTVEntryName = (TextView)findViewById(R.id.tv_entry_name);
        mIVEntryImage = (ImageView)findViewById(R.id.iv_entry_image);
        mTVEntryDescription = (TextView)findViewById(R.id.tv_entry_description);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PokemonUtils.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (PokemonUtils.SearchResult) intent.getSerializableExtra(PokemonUtils.EXTRA_SEARCH_RESULT);
            try {
                mDetailResult = (PokemonUtils.DetailResult)PokemonUtils.parseDetailResultJson(NetworkUtils.doHTTPGet("https://pokeapi.co/api/v2/pokemon"+mSearchResult.name));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mTVEntryName.setText(mDetailResult.name);

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(mDetailResult.sprite).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mIVEntryImage.setImageBitmap(bitmap);
            mTVEntryDescription.setText("Type: "+mDetailResult.type+"\n"+
                    "Height: "+mDetailResult.height+"   Weight:"+mDetailResult.weight+"\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_pokemon:
                viewPokemonOnWeb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewPokemonOnWeb() {
        if (mDetailResult != null) {
            Uri bulbapediaURL = Uri.parse("https://bulbapedia.bulbagarden.net/wiki/"+mDetailResult.name);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, bulbapediaURL);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }



}