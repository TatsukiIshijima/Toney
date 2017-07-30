package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.AlbumAdapter;
import com.io.tatsuki.toney.Events.AlbumEvent;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.Views.GridSpacingItemDecoration;
import com.io.tatsuki.toney.databinding.FragmentAlbumBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.io.tatsuki.toney.Views.GridSpacingItemDecoration.dpToPx;

/**
 * アルバム Fragment
 */

public class AlbumFragment extends Fragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();

    private LocalAccess localAccess;
    private String artistName;

    public static AlbumFragment newInstance() {
        AlbumFragment albumFragment = new AlbumFragment();
        Bundle args = new Bundle();
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

        localAccess = new LocalAccess(getContext());
        binding.fragmentAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.fragmentAlbumRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), localAccess.getAlbums(artistName));
        binding.fragmentAlbumRecyclerView.setAdapter(albumAdapter);

        return albumView;
    }

    @Override
    public void onResume() {
        // EventBusの登録
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        // EventBusの解除
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onClickAlbum(AlbumEvent event) {
        transitionSongFragment(event.getAlbum().getAlbumId());
    }

    /**
     * 曲リスト画面遷移
     * @param albumId   アルバムID
     */
    private void transitionSongFragment(String albumId) {
        Log.d(TAG, "transitionSongFragment : AlbumID : " +  albumId);
        SongFragment songFragment = SongFragment.newInstance(albumId, null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_album_frame_layout, songFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
