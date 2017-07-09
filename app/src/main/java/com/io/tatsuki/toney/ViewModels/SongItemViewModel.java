package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;

/**
 * SongItem ViewModel
 */

public class SongItemViewModel {

    private static final String TAG = SongItemViewModel.class.getSimpleName();

    public ObservableField<String> songTitle = new ObservableField<>();
    public ObservableField<String> songArtist = new ObservableField<>();

    public SongItemViewModel() {

    }
}
