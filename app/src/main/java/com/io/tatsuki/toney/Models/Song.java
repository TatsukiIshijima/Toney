package com.io.tatsuki.toney.Models;

import android.net.Uri;

/**
 * Song Model
 */

public class Song {

    private String songId;
    private String songName;
    private Uri songUri;
    private String songArtPath;

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Uri getSongUri() {
        return songUri;
    }

    public void setSongUri(Uri songUri) {
        this.songUri = songUri;
    }

    public String getSongArtPath() {
        return songArtPath;
    }

    public void setSongArtPath(String songArtPath) {
        this.songArtPath = songArtPath;
    }
}
