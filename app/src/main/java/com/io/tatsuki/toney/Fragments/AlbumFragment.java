package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * アルバム Fragment
 */

public class AlbumFragment extends Fragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();

    public static AlbumFragment newInstance() {
        AlbumFragment albumFragment = new AlbumFragment();
        return albumFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View albumView = inflater.inflate(R.layout.fragment_album, viewGroup, false);
        return albumView;
    }
}
