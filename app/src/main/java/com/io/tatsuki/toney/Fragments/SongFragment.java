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
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.ViewModels.SongViewModel;
import com.io.tatsuki.toney.databinding.FragmentSongBinding;

import java.util.ArrayList;

/**
 * 曲 Framgment
 */

public class SongFragment extends Fragment {

    private static final String TAG = SongFragment.class.getSimpleName();
    private static final String SONG_LIST_KEY = "SONG_LIST_KEY";
    private String albumId;
    private String artistId;
    private ArrayList<Song> songs;

    public static SongFragment newInstance(ArrayList<Song> songs) {
        SongFragment songFragment = new SongFragment();
        Bundle args = new Bundle();
        args.putSerializable(SONG_LIST_KEY, songs);
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

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Bundle bundle = getArguments();
        albumId = bundle.getString("albumId", null);
        artistId = bundle.getString("artistId", null);
        Log.d(TAG, "onCreate : albumId : " + albumId);
        Log.d(TAG, "onCreate : artistId : " + artistId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        FragmentSongBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_song, viewGroup, false);
        View songView = binding.getRoot();
        getSongList();
        binding.fragmentSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter songAdapter = new SongAdapter(getContext(), songs);
        binding.fragmentSongRecyclerView.setAdapter(songAdapter);

        return songView;
    }

    /**
     * 曲リストを受け取る
     */
    private void getSongList() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            songs = (ArrayList<Song>) bundle.getSerializable(SONG_LIST_KEY);
        } else {
            songs = null;
        }
    }
}
