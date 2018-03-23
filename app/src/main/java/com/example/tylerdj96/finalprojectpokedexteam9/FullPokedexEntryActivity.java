package com.example.tylerdj96.finalprojectpokedexteam9;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FullPokedexEntryActivity extends AppCompatActivity {

    private TextView mTVEntryName;
    private ImageView mIVEntryImage;
    private TextView mTVEntryDescription;

    private PokemonUtils.SearchResult mSearchResult;
    private PokemonUtils.DetailResult mDetailResult;
    private String mJSONString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pokedex_entry);

        mTVEntryName = (TextView)findViewById(R.id.tv_entry_name);
        mIVEntryImage = (ImageView)findViewById(R.id.iv_entry_image);
        mTVEntryDescription = (TextView)findViewById(R.id.tv_entry_description);

        EntryTask entry = new EntryTask();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PokemonUtils.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (PokemonUtils.SearchResult) intent.getSerializableExtra(PokemonUtils.EXTRA_SEARCH_RESULT);

            String u = "https://pokeapi.co/api/v2/pokemon/"+mSearchResult.entry_number;
            entry.execute(new String[]{u});//NetworkUtils.doHTTPGet(u);
            mDetailResult = (PokemonUtils.DetailResult)PokemonUtils.parseDetailResultJson(mJSONString);

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

    public void setJSON(String result){
        mJSONString = result;
    }

    private class EntryTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings){
            // we use the OkHttp library from https://github.com/square/okhttp
            OkHttpClient client = new OkHttpClient();
            Request request =
                    new Request.Builder()
                            .url(strings[0])
                            .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                try {
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Download failed";
        }

        protected void onPostExecute (String result){
            setJSON(result);
        }
    }

}