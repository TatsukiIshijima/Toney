package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.AlbumAdapter;
import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Views.GridSpacingItemDecoration;
import com.io.tatsuki.toney.databinding.FragmentAlbumBinding;

import java.util.ArrayList;

import static com.io.tatsuki.toney.Views.GridSpacingItemDecoration.dpToPx;

/**
 * アルバム Fragment
 */

public class AlbumFragment extends Fragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();
    private static final String ALBUM_LIST_KEY = "ALBUM_LIST_KEY";
    private ArrayList<Album> albums;

    public static AlbumFragment newInstance(ArrayList<Album> albums) {
        AlbumFragment albumFragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_LIST_KEY, albums);
        albumFragment.setArguments(args);
        return albumFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        FragmentAlbumBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, viewGroup, false);
        View albumView = binding.getRoot();
        getAlbumList();
        binding.fragmentAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.fragmentAlbumRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), albums);
        binding.fragmentAlbumRecyclerView.setAdapter(albumAdapter);

        return albumView;
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
