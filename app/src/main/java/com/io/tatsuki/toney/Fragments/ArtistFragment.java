package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.ArtistAdapter;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.databinding.FragmentArtistBinding;

import java.util.ArrayList;

/**
 * アーティスト Fragment
 */

public class ArtistFragment extends Fragment {

    private static final String TAG = ArtistFragment.class.getSimpleName();
    private static final String ARTIST_LIST_LEY = "ARTIST_LIST_KEY";
    private ArrayList<Artist> artists;

    public static ArtistFragment newInstance(ArrayList<Artist> artists) {
        ArtistFragment artistFragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTIST_LIST_LEY, artists);
        artistFragment.setArguments(args);
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
        getArtistList();
        binding.fragmentArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArtistAdapter artistAdapter = new ArtistAdapter(getContext(), artists);
        binding.fragmentArtistRecyclerView.setAdapter(artistAdapter);

        return artistView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * アーティストリストを受け取る
     */
    private void getArtistList() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            artists = (ArrayList<Artist>) bundle.getSerializable(ARTIST_LIST_LEY);
        } else {
            artists = null;
        }
    }
}
