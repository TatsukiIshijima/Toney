package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * 曲 Framgment
 */

public class SongFragment extends Fragment {

    public static SongFragment newInstance() {
        SongFragment songFragment = new SongFragment();
        return songFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View songView = inflater.inflate(R.layout.fragment_song, null);
        return songView;
    }
}
