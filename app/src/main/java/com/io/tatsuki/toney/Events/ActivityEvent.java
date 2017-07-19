package com.io.tatsuki.toney.Events;

/**
 * Activityの状態を通知するクラス
 */

public class ActivityEvent {

    private boolean isDestroy;

    public ActivityEvent(boolean isDestroy) {
        this.isDestroy = isDestroy;
    }

    public boolean isDestroy() {
        return isDestroy;
    }
}
