package com.io.tatsuki.toney.Events;

import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.Models.Song;

import java.util.ArrayList;

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
    private ArrayList<Song> songs;
    private int position;

    public ClickEvent(int requestCode) {
        this.requestCode = requestCode;
    }

    public ClickEvent(int requestCode, ArrayList<Song> songs, int position) {
        this.requestCode = requestCode;
        this.songs = songs;
        this.position = position;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getPosition() {
        return position;
    }
}
