package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Services.MusicService;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.Utils.ServiceConstant;
import com.io.tatsuki.toney.ViewModels.SongViewModel;
import com.io.tatsuki.toney.databinding.ItemSongBinding;

import java.util.ArrayList;

/**
 * Song Adapter
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private static final String TAG = SongAdapter.class.getSimpleName();
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
        // スクロール時に位置が変更されてしまうのを防止
        // TODO:RecyclerViewの効果を発揮できないので代替案が必要かも
        holder.setIsRecyclable(false);
        Song song = getItemAt(holder.getLayoutPosition());
        // 曲データのセット
        holder.loadModel(song);
        // 画像のセット
        ImageUtil.setDownloadImage(context, song.getSongArtPath(), binding.itemSongImage);
        // クリックイベントのセット
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Service開始
                startService(position);
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
     * Serviceの開始
     */
    private void startService(int position) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(ServiceConstant.SERVICE_START);
        Bundle args = new Bundle();
        args.putInt(MusicService.POSITION_KEY, position);
        args.putSerializable(MusicService.SONGS_KEY, songs);
        intent.putExtras(args);
        // Serializeは基本的な型しか送れない
        // Song Class にはUriが含まれているため送れない
        context.startService(intent);
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
