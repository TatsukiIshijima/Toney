package com.io.tatsuki.toney.Events;

/**
 * Viewにシャッフル設定を反映させるのためのShuffleEvent
 */

public class ShuffleEvent {

    private boolean isShuffle;

    public ShuffleEvent(boolean isShuffle) {
        this.isShuffle = isShuffle;
    }

    public boolean isShuffle() {
        return isShuffle;
    }
}
