package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.SongAdapter;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.ViewModels.SongViewModel;
import com.io.tatsuki.toney.databinding.FragmentSongBinding;

/**
 * 曲 Framgment
 */

public class SongFragment extends Fragment {

    private LocalAccess localAccess;
    private String albumId;

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    public SongFragment() {}

    public SongFragment(String albumId) {
        this.albumId = albumId;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        FragmentSongBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_song, viewGroup, false);
        View songView = binding.getRoot();

        localAccess = new LocalAccess(getContext());
        binding.fragmentSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter songAdapter = new SongAdapter(getContext(), localAccess.getSongs(albumId));
        binding.fragmentSongRecyclerView.setAdapter(songAdapter);

        return songView;
    }
}
