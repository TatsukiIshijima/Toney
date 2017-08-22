package com.io.tatsuki.toney.Models;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

/**
 * Album Model
 */

public class Album implements Serializable {

    @Getter
    @Setter
    private String albumId;

    @Getter
    @Setter
    private String albumKey;

    @Getter
    @Setter
    private String albumName;

    @Getter
    @Setter
    private String albumArtPath;

    @Getter
    @Setter
    private String albumArtist;
}
