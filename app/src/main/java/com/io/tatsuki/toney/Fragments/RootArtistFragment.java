package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * ViwePager内での遷移を可能にするためのFragment
 */

public class RootArtistFragment extends Fragment {

    public RootArtistFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_root_artist_layout, viewGroup, false);
        // アーティスト画面遷移
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_artist_frame_layout, ArtistFragment.newInstance());
        transaction.commit();
        return rootView;
    }
}
