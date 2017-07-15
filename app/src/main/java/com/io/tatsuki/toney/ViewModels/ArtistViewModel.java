package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Events.ArtistEvent;
import com.io.tatsuki.toney.Models.Artist;

import org.greenrobot.eventbus.EventBus;

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

    public void onClickArtist(Artist artist) {
        Log.d(TAG, "onClick : " + artist.getArtistId());
        // ArtistFragmentに通知
        EventBus.getDefault().post(new ArtistEvent(artist));
    }
}
