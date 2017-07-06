package com.io.tatsuki.toney.ViewModels;

import android.util.Log;
import android.view.View;

import com.io.tatsuki.toney.ClickEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * アーティスト画面 ViewModel
 */

public class ArtistViewModel {

    private static final String TAG = ArtistViewModel.class.getSimpleName();

    public ArtistViewModel() {
    }

    public void onClick(View view) {
        Log.d(TAG, "Click Button");
        EventBus.getDefault().post(new ClickEvent("test"));
    }
}
