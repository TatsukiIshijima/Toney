package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Song;

/**
 * 曲を渡すためのEventクラス
 */

public class SongEvent {

    private Song song;

    public SongEvent(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
