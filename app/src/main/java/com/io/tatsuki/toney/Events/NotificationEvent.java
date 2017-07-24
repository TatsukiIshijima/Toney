package com.io.tatsuki.toney.Events;

/**
 * Notificationのイベントを通知するクラス
 */

public class NotificationEvent {

    private boolean isPlaying;

    public NotificationEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }


    public boolean isPlaying() {
        return isPlaying;
    }
}
