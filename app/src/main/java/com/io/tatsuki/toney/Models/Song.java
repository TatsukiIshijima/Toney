package com.io.tatsuki.toney.Models;

import android.net.Uri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Song Model
 */

public class Song implements Serializable {

    @Getter
    @Setter
    private String songId;

    @Getter
    @Setter
    private String songName;

    @Getter
    @Setter
    private String songPath;

    @Getter
    @Setter
    private String songArtPath;

    @Getter
    @Setter
    private String songArtist;

    @Getter
    @Setter
    private long duration;
}
