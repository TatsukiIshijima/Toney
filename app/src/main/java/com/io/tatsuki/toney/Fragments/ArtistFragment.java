package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.ViewModels.ArtistViewModel;
import com.io.tatsuki.toney.databinding.FragmentArtistBinding;

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
        FragmentArtistBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, viewGroup, false);
        View artistView = binding.getRoot();
        ArtistViewModel artistViewModel = new ArtistViewModel();
        binding.setArtistViewModel(artistViewModel);
        return artistView;
    }
}
