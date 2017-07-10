package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.ViewModels.SongViewModel;
import com.io.tatsuki.toney.databinding.ItemSongBinding;

import java.util.ArrayList;

/**
 * Song Adapter
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private Context context;
    private ArrayList<Song> songs;

    /**
     * コンストラクタ
     * @param context
     * @param songs
     */
    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    private Song getItemAt(int position) {
        return songs.get(position);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSongBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_song, parent, false);
        binding.setSongItemViewModel(new SongViewModel());
        return new SongViewHolder(binding.getRoot(), binding.getSongItemViewModel());
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = getItemAt(position);
        holder.loadModel(song);
    }

    @Override
    public int getItemCount() {
        if (songs == null) {
            return 0;
        } else {
            return songs.size();
        }
    }

    /**
     * ViewHolder
     */
    static class SongViewHolder extends RecyclerView.ViewHolder {

        private SongViewModel songItemViewModel;

        public SongViewHolder(View itemView, SongViewModel songItemViewModel) {
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
