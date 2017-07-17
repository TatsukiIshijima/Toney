package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Models.Song;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Song ViewModel
 */

public class SongViewModel {

    private static final String TAG = SongViewModel.class.getSimpleName();
    private ArrayList<Song> songs;

    public ObservableField<String> songName = new ObservableField<>();
    public ObservableField<String> songArtist = new ObservableField<>();

    public SongViewModel() {

    }

    public SongViewModel(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setSong(Song song) {
        songName.set(song.getSongName());
        songArtist.set(song.getSongArtist());
    }

    public void onClickSong(Song song) {
        Log.d(TAG, "onClick :" + song.getSongName());
        // Activityに通知
        EventBus.getDefault().post(new ClickEvent(ClickEvent.playCode, song));
    }
}
