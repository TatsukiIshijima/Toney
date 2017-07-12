package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.ViewModels.ArtistViewModel;
import com.io.tatsuki.toney.databinding.ItemArtistBinding;

import java.util.ArrayList;

/**
 * Artist Adapter
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private Context context;
    private ItemArtistBinding binding;
    private ArrayList<Artist> artists;

    /**
     * コンストラクタ
     * @param context
     * @param artists
     */
    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_artist, parent, false);
        binding.setArtistViewModel(new ArtistViewModel());
        return new ArtistViewHolder(binding.getRoot(), binding.getArtistViewModel());
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist artist = getItemAt(position);
        //  アーティストデータのセット
        holder.loadModel(artist);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private Artist getItemAt(int position) {
        return artists.get(position);
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {

        private ArtistViewModel artistViewModel;


        public ArtistViewHolder(View itemView, ArtistViewModel artistViewModel) {
            super(itemView);
            this.artistViewModel = artistViewModel;
        }

        /**
         * ArtistModelの読み込み
         * @param artist
         */
        public void loadModel(Artist artist) {
            artistViewModel.setArtist(artist);
        }
    }
}
