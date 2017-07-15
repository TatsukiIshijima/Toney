package com.io.tatsuki.toney.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.io.tatsuki.toney.Fragments.ArtistFragment;
import com.io.tatsuki.toney.Fragments.RootAlbumFragment;
import com.io.tatsuki.toney.Fragments.SongFragment;
import com.io.tatsuki.toney.R;

/**
 * ページ切り替え用のPagerAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public HomePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ArtistFragment.newInstance();
            case 1:
                // 画面遷移時に重ならないようにRootFragmentを持たせる
                return new RootAlbumFragment();
            case 2:
                return SongFragment.newInstance();
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
}
