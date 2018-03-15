package com.example.android.githubsearchwithprefs;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tylerdj96.finalprojectpokedexteam9.R;
import com.example.tylerdj96.finalprojectpokedexteam9.utils.PokemonUtils;

public class FullPokedexEntryActivity extends AppCompatActivity {

    private TextView mTVEntryName;
    private ImageView mIVEntryImage;
    private TextView mTVEntryDescription;

    private PokemonUtils.SearchResult mSearchResult;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pokedex_entry);

        mTVEntryName = (TextView)findViewById(R.id.tv_entry_name);
        // mIVEntryImage = (ImageView)findViewById(R.id.iv_entry_image);
        mTVEntryDescription = (TextView)findViewById(R.id.tv_entry_description);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PokemonUtils.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (PokemonUtils.SearchResult) intent.getSerializableExtra(PokemonUtils.EXTRA_SEARCH_RESULT);
            mTVEntryName.setText(mSearchResult.name);
            //? mIVEntryImage.setImageDrawable(String.valueOf(mSearchResult.stars));
            mTVEntryDescription.setText(mSearchResult.description);
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
            case R.id.action_view_repo:
                viewRepoOnWeb();
                return true;
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewRepoOnWeb() {
        if (mSearchResult != null) {
            Uri githubRepoURL = Uri.parse(mSearchResult.htmlURL);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, githubRepoURL);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }

    public void shareRepo() {
        if (mSearchResult != null) {
            String shareText = getString(R.string.share_text_prefix) + ": " +
                    mSearchResult.fullName + ", " + mSearchResult.htmlURL;

            ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle(R.string.share_chooser_title)
                    .setType("text/plain")
                    .setText(shareText)
                    .startChooser();
        }
    }
}