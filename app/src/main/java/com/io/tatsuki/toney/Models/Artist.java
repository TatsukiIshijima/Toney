package com.io.tatsuki.toney.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Artist Model
 */

public class Artist implements Serializable {

    private String ArtistId;
    private String ArtistKey;
    private String ArtistName;
    private int albumNum;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    public String getArtistId() {
        return ArtistId;
    }

    public void setArtistId(String artistId) {
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

    public int getAlbumNum() {
        return albumNum;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
