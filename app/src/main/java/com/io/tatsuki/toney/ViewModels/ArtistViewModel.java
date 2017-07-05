package com.io.tatsuki.toney.ViewModels;

import android.util.Log;
import android.view.View;

/**
 * アーティスト画面 ViewModel
 */

public class ArtistViewModel {

    private static final String TAG = ArtistViewModel.class.getSimpleName();

    public ArtistViewModel() {
    }

    public void onClick(View view) {
        Log.d(TAG, "Click Button");
    }
}
