package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Song;

/**
 * 曲を渡すためのEventクラス
 */

public class SelectSongEvent {

    private Song song;

    public SelectSongEvent(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
