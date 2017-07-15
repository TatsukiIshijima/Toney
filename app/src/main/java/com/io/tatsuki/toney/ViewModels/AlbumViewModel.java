package com.io.tatsuki.toney.ViewModels;

import android.databinding.ObservableField;
import android.util.Log;

import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Utils.EventRequestConstants;

import org.greenrobot.eventbus.EventBus;

/**
 * Album ViewModel
 */

public class AlbumViewModel {

    private static final String TAG = AlbumViewModel.class.getSimpleName();

    public ObservableField<String> albumName = new ObservableField<>();
    public ObservableField<String> albumArtist = new ObservableField<>();

    public AlbumViewModel() {

    }

    public void setAlbum(Album album) {
        albumName.set(album.getAlbumName());
        albumArtist.set(album.getAlbumArtist());
    }

    public void onClickAlbum(Album album) {
        Log.d(TAG, "onClick : " + album.getAlbumName());
        // AlbumFragmentに通知
        EventBus.getDefault().post(new ClickEvent(EventRequestConstants.CLICK_ALBUM, album));
    }
}
