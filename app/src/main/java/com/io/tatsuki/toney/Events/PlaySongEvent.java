package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Song;

/**
 * 曲を渡すためのEventクラス
 */

public class PlaySongEvent {

    private Song song;

    public PlaySongEvent(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
