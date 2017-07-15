package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Album;

/**
 * アルバム画面でのEventクラス
 */

public class AlbumEvent {

    private Album album;

    public AlbumEvent(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
