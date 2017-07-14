package com.io.tatsuki.toney.ViewModels;

import android.util.Log;
import android.view.View;

/**
 * ホーム画面 ViewModel
 */

public class HomeViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    public HomeViewModel() {

    }

    public void onClickPlay(View view) {
        Log.d(TAG, "onClickPlay");
    }

    public void onClickNext(View view) {
        Log.d(TAG, "onClickNext");
    }

    public void onClickPrev(View view) {
        Log.d(TAG, "onClickPrev");
    }

    public void onClickRepeat(View view) {
        Log.d(TAG, "onClickRepeat");
    }

    public void onClickShuffle(View view) {
        Log.d(TAG, "onClickShuffle");
    }
}
