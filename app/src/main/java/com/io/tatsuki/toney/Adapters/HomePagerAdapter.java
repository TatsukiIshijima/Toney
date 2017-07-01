package com.io.tatsuki.toney.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.io.tatsuki.toney.Fragments.AlbumFragment;
import com.io.tatsuki.toney.Fragments.ArtistFragment;
import com.io.tatsuki.toney.Fragments.SongFragment;

/**
 * ページ切り替え用のPagerAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ArtistFragment.newInstance();
            case 1:
                return AlbumFragment.newInstance();
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
    public CharSequence getPageTitle(int postion) {
        return "ページ" + (postion + 1);
    }
}
