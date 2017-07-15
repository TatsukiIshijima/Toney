package com.io.tatsuki.toney.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.Adapters.ArtistAdapter;
import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.ViewModels.ArtistViewModel;
import com.io.tatsuki.toney.databinding.FragmentArtistBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * アーティスト Fragment
 */

public class ArtistFragment extends Fragment {

    private static final String TAG = ArtistFragment.class.getSimpleName();
    private LocalAccess localAccess;

    public static ArtistFragment newInstance() {
        ArtistFragment artistFragment = new ArtistFragment();
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

        localAccess = new LocalAccess(getContext());
        binding.fragmentArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArtistAdapter artistAdapter = new ArtistAdapter(getContext(), localAccess.getArtists());
        binding.fragmentArtistRecyclerView.setAdapter(artistAdapter);

        return artistView;
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
    public void onClickArtist(ClickEvent event) {
        Log.d(TAG, "onClickArtist : " + event.getArtist().getArtistId() + " ," + event.getArtist().getArtistName());
        transitionSongFragment(event.getArtist().getArtistId());
    }

    /**
     * 曲リスト画面遷移
     * @param artistId
     */
    private void transitionSongFragment(String artistId) {
        Log.d(TAG, "transitionSongFragment : ArtistID : " +  artistId);
        SongFragment songFragment = SongFragment.newInstance(null, artistId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_artist_frame_layout, songFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
