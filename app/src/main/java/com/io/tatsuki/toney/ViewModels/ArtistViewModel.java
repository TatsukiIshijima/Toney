package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Models.Artist;

import org.greenrobot.eventbus.EventBus;

/**
 * Artist ViewModel
 */

public class ArtistViewModel {

    private static final String TAG = ArtistViewModel.class.getSimpleName();

    public ObservableField<String> artistName = new ObservableField<>();

    public ArtistViewModel() {
    }

    public void setArtist(Artist artist) {
        artistName.set(artist.getArtistName());
    }

    public void onClick(View view) {
        Log.d(TAG, "Click Button");
        EventBus.getDefault().post(new ClickEvent("test"));
    }
}
