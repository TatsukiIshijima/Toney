package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;

import com.io.tatsuki.toney.Models.Song;

/**
 * SongItem ViewModel
 */

public class SongItemViewModel {

    private static final String TAG = SongItemViewModel.class.getSimpleName();

    public ObservableField<String> songName = new ObservableField<>();
    public ObservableField<String> songArtist = new ObservableField<>();

    public SongItemViewModel() {

    }

    public void setSong(Song song) {
        songName.set(song.getSongName());
        songArtist.set(song.getSongArtist());
    }
}
