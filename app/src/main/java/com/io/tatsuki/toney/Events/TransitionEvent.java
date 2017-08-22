package com.io.tatsuki.toney.Events;

import lombok.Getter;

/**
 * 画面遷移用のEventクラス
 */

public class TransitionEvent {

    public static final int ARTIST_TO_SONG_FLAG = 0;
    public static final int ALBUM_TO_SONG_FLAG = 1;

    @Getter
    private int flag;

    @Getter
    private String id;

    public TransitionEvent(int flag, String id) {
        this.flag = flag;
        this.id = id;
    }
}
