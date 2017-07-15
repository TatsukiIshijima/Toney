package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * Created by TatsukiIshijima on 2017/07/15.
 */

public class RootAlbumFragment extends Fragment {

    public RootAlbumFragment() {}


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_root_album_layout, viewGroup, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        /*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame_layout, AlbumFragment.newInstance());
        transaction.commit();
        return rootView;
    }
}
