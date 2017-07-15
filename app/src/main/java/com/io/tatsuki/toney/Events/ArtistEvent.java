package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Artist;

/**
 * アーティスト画面でのEventクラス
 */

public class ArtistEvent {

    private Artist artist;

    public ArtistEvent(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }
}
