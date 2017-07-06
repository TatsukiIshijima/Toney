package com.io.tatsuki.toney;

/**
 * Created by TatsukiIshijima on 2017/07/06.
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
