package com.io.tatsuki.toney.ViewModels;

import android.util.Log;
import android.view.View;

import com.io.tatsuki.toney.Events.ClickEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * ホーム画面 ViewModel
 */

public class HomeViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    public HomeViewModel() {

    }

    public void onClickPlay(View view) {
        Log.d(TAG, "onClickPlay");
        EventBus.getDefault().post(new ClickEvent(ClickEvent.playCode));
    }

    public void onClickNext(View view) {
        Log.d(TAG, "onClickNext");
        EventBus.getDefault().post(new ClickEvent(ClickEvent.nextCode));
    }

    public void onClickPrev(View view) {
        Log.d(TAG, "onClickPrev");
        EventBus.getDefault().post(new ClickEvent(ClickEvent.prevCode));
    }

    public void onClickRepeat(View view) {
        Log.d(TAG, "onClickRepeat");
        EventBus.getDefault().post(new ClickEvent(ClickEvent.repeatCode));
    }

    public void onClickShuffle(View view) {
        Log.d(TAG, "onClickShuffle");
        EventBus.getDefault().post(new ClickEvent(ClickEvent.shuffleCode));
    }
}
