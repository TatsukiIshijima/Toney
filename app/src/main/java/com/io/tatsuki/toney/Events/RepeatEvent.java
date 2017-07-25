package com.io.tatsuki.toney.Events;

/**
 * Viewにリピート設定を反映させるためのRepeatEvent
 */

public class RepeatEvent {

    private boolean isRepeat;

    public RepeatEvent(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public boolean isRepeat() {
        return isRepeat;
    }
}
