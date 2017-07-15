package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

    private static final String TAG = SongFragment.class.getSimpleName();

    private LocalAccess localAccess;
    private String albumId;
    private String artistId;

    public static SongFragment newInstance() {
        SongFragment songFragment = new SongFragment();
        Bundle args = new Bundle();
        songFragment.setArguments(args);
        return songFragment;
    }

    public static SongFragment newInstance(String albumId, String artistId) {
        SongFragment songFragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString("albumId", albumId);
        args.putString("artistId", artistId);
        songFragment.setArguments(args);
        return songFragment;
    }

    public SongFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Bundle bundle = getArguments();
        albumId = bundle.getString("albumId", null);
        Log.d(TAG, "onCreate : albumId : " + albumId);
        //artistId = bundle.getString("artistId", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        FragmentSongBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_song, viewGroup, false);
        View songView = binding.getRoot();

        localAccess = new LocalAccess(getContext());
        binding.fragmentSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter songAdapter = new SongAdapter(getContext(), localAccess.getSongs(albumId, artistId));
        binding.fragmentSongRecyclerView.setAdapter(songAdapter);

        return songView;
    }
}
