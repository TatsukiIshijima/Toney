package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.Models.Song;

/**
 * EventBusのよりActivityやFragmentにクリックを通知するためのEvent
 */

public class ClickEvent {

    // クリックイベントでの分岐に使用するコード
    public static final int prevCode = 0;
    public static final int playCode = 1;
    public static final int nextCode = 2;
    public static final int repeatCode = 3;
    public static final int shuffleCode = 4;

    private int requestCode;
    private Song song;

    public ClickEvent(int requestCode) {
        this.requestCode = requestCode;
    }

    public ClickEvent(int requestCode, Song song) {
        this.requestCode = requestCode;
        this.song = song;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public Song getSong() {
        return this.song;
    }
}
