package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.R;

import java.util.ArrayList;

/**
 * ViwePager内での遷移を可能にするためのFragment
 */

public class RootAlbumFragment extends Fragment {

    private static final String TAG = RootAlbumFragment.class.getSimpleName();
    private static final String ALBUM_LIST_KEY = "ALBUM_LIST_KEY";
    private ArrayList<Album> albums;

    public static RootAlbumFragment newInstance(ArrayList<Album> albums) {
        RootAlbumFragment rootAlbumFragment = new RootAlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_LIST_KEY, albums);
        rootAlbumFragment.setArguments(args);
        return rootAlbumFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_root_album_layout, viewGroup, false);
        getAlbumList();
        // アルバムリスト画面遷移
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_album_frame_layout, AlbumFragment.newInstance(albums));
        transaction.commit();
        return rootView;
    }

    /**
     * アルバムリストを受け取る
     */
    private void getAlbumList() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            albums = (ArrayList<Album>) bundle.getSerializable(ALBUM_LIST_KEY);
        } else {
            albums = null;
        }
    }
}
