package com.example.tylerdj96.finalprojectpokedexteam9;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tylerdj96.finalprojectpokedexteam9.utils.PokemonUtils;

import java.util.ArrayList;
/**
 * Created by tylerdj96 on 3/13/2018.
 */

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.SearchResultViewHolder> {
    private ArrayList<PokemonUtils.SearchResult> mSearchResultsList;
    OnSearchItemClickListener mSeachItemClickListener;

    PokemonAdapter(OnSearchItemClickListener searchItemClickListener) {
        mSeachItemClickListener = searchItemClickListener;
    }

    public void updateSearchResults(ArrayList<PokemonUtils.SearchResult> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(PokemonUtils.SearchResult searchResult);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView) itemView.findViewById(R.id.tv_search_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PokemonUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
                    mSeachItemClickListener.onSearchItemClick(searchResult);
                }
            });
        }

        public void bind(PokemonUtils.SearchResult searchResult) {
            mSearchResultTV.setText(searchResult.name);
        }

    }
}
