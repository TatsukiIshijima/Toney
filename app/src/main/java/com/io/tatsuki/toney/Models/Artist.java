package com.io.tatsuki.toney.Models;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Artist Model
 */

public class Artist implements Serializable {

    @Getter
    @Setter
    private String ArtistId;

    @Getter
    @Setter
    private String ArtistKey;

    @Getter
    @Setter
    private String ArtistName;

    @Getter
    @Setter
    private int albumNum;
}
