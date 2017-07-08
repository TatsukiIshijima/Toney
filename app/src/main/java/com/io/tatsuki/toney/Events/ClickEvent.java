package com.io.tatsuki.toney.Events;

/**
 * EventBusのよりActivityやFragmentにクリックを通知するためのEvent
 */

public class ClickEvent {

    private String requestCode;

    public ClickEvent(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestCode() {
        return this.requestCode;
    }
}
