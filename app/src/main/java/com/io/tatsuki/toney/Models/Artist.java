package com.io.tatsuki.toney.Models;

import java.util.ArrayList;

/**
 * Artist Model
 */

public class Artist {

    private long ArtistId;
    private String ArtistKey;
    private String ArtistName;
    private ArrayList<Album> albums;

    public long getArtistId() {
        return ArtistId;
    }

    public void setArtistId(long artistId) {
        ArtistId = artistId;
    }

    public String getArtistKey() {
        return ArtistKey;
    }

    public void setArtistKey(String artistKey) {
        ArtistKey = artistKey;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
