package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.Models.Song;

/**
 * EventBusのよりActivityやFragmentにクリックを通知するためのEvent
 */

public class ClickEvent {

    private String requestCode;
    private Song song;
    private Album album;
    private Artist artist;

    public ClickEvent(String requestCode) {
        this.requestCode = requestCode;
    }

    public ClickEvent(String requestCode, Song song) {
        this.requestCode = requestCode;
        this.song = song;
    }

    public ClickEvent(String requestCode, Artist artist) {
        this.requestCode = requestCode;
        this.artist = artist;
    }

    public String getRequestCode() {
        return this.requestCode;
    }

    public Song getSong() {
        return this.song;
    }

    public Album getAlbum() {
        return this.album;
    }

    public Artist getArtist() {
        return this.artist;
    }
}
