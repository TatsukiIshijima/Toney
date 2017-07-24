package com.io.tatsuki.toney.Events;

/**
 * Notificationのイベントを通知するクラス
 */

public class PlayPauseEvent {

    private boolean isPlaying;

    public PlayPauseEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }


    public boolean isPlaying() {
        return isPlaying;
    }
}
