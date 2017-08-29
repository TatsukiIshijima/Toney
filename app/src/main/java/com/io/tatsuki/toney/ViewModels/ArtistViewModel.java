package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Models.Artist;


/**
 * Artist ViewModel
 */

public class ArtistViewModel {

    private static final String TAG = ArtistViewModel.class.getSimpleName();

    public ObservableField<String> artistName = new ObservableField<>();
    public ObservableField<String> artistAlbums = new ObservableField<>();

    public ArtistViewModel() {
    }

    public void setArtist(Artist artist) {
        artistName.set(artist.getArtistName());
        artistAlbums.set(artist.getAlbumNum() + " Albums");
    }
}
