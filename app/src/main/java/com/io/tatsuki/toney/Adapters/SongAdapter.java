package com.io.tatsuki.toney.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.ViewModels.SongItemViewModel;
import com.io.tatsuki.toney.ViewModels.SongViewModel;

/**
 * Song Adapter
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * ViewHolder
     */
    static class SongViewHolder extends RecyclerView.ViewHolder {

        private SongItemViewModel songItemViewModel;

        public SongViewHolder(View itemView, SongItemViewModel songItemViewModel) {
            super(itemView);
            this.songItemViewModel = songItemViewModel;
        }

        /**
         * SongModelの読み込み
         * @param song
         */
        public void loadModel(Song song) {
            songItemViewModel.setSong(song);
        }
    }
}
