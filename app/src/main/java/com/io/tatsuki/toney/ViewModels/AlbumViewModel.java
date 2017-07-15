package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Models.Album;

/**
 * Album ViewModel
 */

public class AlbumViewModel {

    private static final String TAG = AlbumViewModel.class.getSimpleName();

    public ObservableField<String> albumName = new ObservableField<>();
    public ObservableField<String> albumArtist = new ObservableField<>();

    public AlbumViewModel() {

    }

    public void setAlbum(Album album) {
        albumName.set(album.getAlbumName());
        albumArtist.set(album.getAlbumArtist());
    }

    public void onClickAlbum(Album album) {
        Log.d(TAG, "onClick : " + album.getAlbumName());
        // Activityに通知
    }
}
