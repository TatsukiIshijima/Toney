package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.ViewModels.SongViewModel;
import com.io.tatsuki.toney.databinding.ItemSongBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Song Adapter
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private Context context;
    private ItemSongBinding binding;
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

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_song, parent, false);
        binding.setSongViewModel(new SongViewModel());
        return new SongViewHolder(binding.getRoot(), binding.getSongViewModel());
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = getItemAt(position);
        // 曲データのセット
        holder.loadModel(song);
        // 画像のセット
        ImageUtil.setDownloadImage(context, song.getSongArtPath(), binding.itemSongImage);
        // クリックイベントのセット
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Serviceに通知
                EventBus.getDefault().post(new ClickEvent(ClickEvent.playCode, songs, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (songs == null) {
            return 0;
        } else {
            return songs.size();
        }
    }

    private Song getItemAt(int position) {
        return songs.get(position);
    }

    /**
     * ViewHolder
     */
    static class SongViewHolder extends RecyclerView.ViewHolder {

        private SongViewModel songViewModel;

        public SongViewHolder(View itemView, SongViewModel songViewModel) {
            super(itemView);
            this.songViewModel = songViewModel;
        }

        /**
         * SongModelの読み込み
         * @param song
         */
        public void loadModel(Song song) {
            songViewModel.setSong(song);
        }
    }
}
