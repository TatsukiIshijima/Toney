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
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;

/**
 * ページ切り替え用のPagerAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    private static final String TAG = HomePagerAdapter.class.getSimpleName();
    private Context context;
    private FragmentManager fm;
    private LocalAccess localAccess;

    public HomePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        this.fm = fragmentManager;
        localAccess = new LocalAccess(context);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // 画面遷移時に重ならないようにRootFragmentを持たせる
                return RootArtistFragment.newInstance(localAccess.getArtists());
            case 1:
                // 画面遷移時に重ならないようにRootFragmentを持たせる
                return RootAlbumFragment.newInstance();
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
