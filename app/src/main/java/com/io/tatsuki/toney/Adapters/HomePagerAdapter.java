package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.io.tatsuki.toney.Fragments.RootAlbumFragment;
import com.io.tatsuki.toney.Fragments.RootArtistFragment;
import com.io.tatsuki.toney.Fragments.SongFragment;
import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;

import java.util.ArrayList;

/**
 * ページ切り替え用のPagerAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    private static final String TAG = HomePagerAdapter.class.getSimpleName();
    private Context context;
    private FragmentManager fm;
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    public HomePagerAdapter(Context context,
                            FragmentManager fragmentManager,
                            ArrayList<Artist> artists,
                            ArrayList<Album> albums,
                            ArrayList<Song> songs) {
        super(fragmentManager);
        this.context = context;
        this.fm = fragmentManager;
        this.artists = artists;
        this.albums = albums;
        this.songs = songs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // 画面遷移時に重ならないようにRootFragmentを持たせる
                return RootArtistFragment.newInstance(artists);
            case 1:
                // 画面遷移時に重ならないようにRootFragmentを持たせる
                return RootAlbumFragment.newInstance(albums);
            case 2:
                return SongFragment.newInstance(songs);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_title_artist);
            case 1:
                return context.getString(R.string.tab_title_album);
            case 2:
                return context.getString(R.string.tab_title_song);
            default:
                return null;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged");
        // Fragmentのスタックを消す
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
