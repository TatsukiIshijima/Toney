package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.R;

import java.util.ArrayList;

/**
 * ViwePager内での遷移を可能にするためのFragment
 */

public class RootArtistFragment extends Fragment {

    private static final String TAG = RootArtistFragment.class.getSimpleName();
    private static final String ARTIST_LIST_LEY = "ALBUM_LIST_KEY";
    private ArrayList<Artist> artists;

    public static RootArtistFragment newInstance(ArrayList<Artist> artists) {
        RootArtistFragment rootArtistFragment = new RootArtistFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTIST_LIST_LEY, artists);
        rootArtistFragment.setArguments(args);
        return rootArtistFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_root_artist_layout, viewGroup, false);
        getArtistList();
        // アーティスト画面遷移
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_artist_frame_layout, ArtistFragment.newInstance(artists));
        transaction.commit();
        return rootView;
    }

    /**
     * アーティストリストを受け取る
     * @return アーティストリスト
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
