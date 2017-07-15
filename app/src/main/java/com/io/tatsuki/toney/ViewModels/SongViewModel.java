package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Models.Song;

/**
 * Song ViewModel
 */

public class SongViewModel {

    private static final String TAG = SongViewModel.class.getSimpleName();

    public ObservableField<String> songName = new ObservableField<>();
    public ObservableField<String> songArtist = new ObservableField<>();

    public SongViewModel() {

    }

    public void setSong(Song song) {
        songName.set(song.getSongName());
        songArtist.set(song.getSongArtist());
    }

    public void onClickSong(Song song) {
        Log.d(TAG, "onClick :" + song.getSongName());
        // Activityに通知
        //　通知を受けたらBottomSheetに反映
    }
}
