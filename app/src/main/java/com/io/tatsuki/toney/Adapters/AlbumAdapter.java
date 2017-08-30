package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Events.TransitionEvent;
import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.ViewModels.AlbumViewModel;
import com.io.tatsuki.toney.databinding.ItemAlbumBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Album Adapter
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private ItemAlbumBinding binding;
    private ArrayList<Album> albums;

    /**
     * コンストラクタ
     * @param context
     * @param albums
     */
    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_album, parent, false);
        binding.setAlbumViewModel(new AlbumViewModel());
        return new AlbumViewHolder(binding.getRoot(), binding.getAlbumViewModel());
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = getItemAt(position);
        // アルバムのセット
        holder.loadModel(album);
        // 画像のセット
        ImageUtil.setDownloadImage(context, album.getAlbumArtPath(), binding.itemAlbumImage);
        // クリックイベントのセット
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new TransitionEvent(TransitionEvent.ALBUM_TO_SONG_FLAG, album.getAlbumId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (albums == null) {
            return 0;
        } else {
            return albums.size();
        }
    }

    private Album getItemAt(int position) {
        return albums.get(position);
    }

    /**
     * ViewHolder
     */
    static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private AlbumViewModel albumViewModel;

        public AlbumViewHolder(View itemView, AlbumViewModel albumViewModel) {
            super(itemView);
            this.albumViewModel = albumViewModel;
        }

        /**
         * AlbumModelの読み込み
         * @param album
         */
        public void loadModel(Album album) {
            albumViewModel.setAlbum(album);
        }
    }
}
