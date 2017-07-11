package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.AlbumAdapter;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.databinding.FragmentAlbumBinding;

/**
 * アルバム Fragment
 */

public class AlbumFragment extends Fragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();

    private LocalAccess localAccess;

    public static AlbumFragment newInstance() {
        AlbumFragment albumFragment = new AlbumFragment();
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

        localAccess = new LocalAccess(getContext());
        binding.fragmentAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), localAccess.getAlbums(null));
        binding.fragmentAlbumRecyclerView.setAdapter(albumAdapter);

        return albumView;
    }
}
