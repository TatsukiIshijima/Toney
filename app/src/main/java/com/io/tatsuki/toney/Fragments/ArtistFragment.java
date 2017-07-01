package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * アーティスト Fragment
 */

public class ArtistFragment extends Fragment {

    public static ArtistFragment newInstance() {
        ArtistFragment artistFragment = new ArtistFragment();
        return artistFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View artistView = inflater.inflate(R.layout.fragment_artist, null);
        return artistView;
    }
}
