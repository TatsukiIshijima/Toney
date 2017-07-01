package com.io.tatsuki.toney.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.io.tatsuki.toney.Adapters.HomePagerAdapter;
import com.io.tatsuki.toney.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setViews();
    }

    /**
     * 各Viewの設定
     */
    private void setViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_home_viewpager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(fragmentManager);
        viewPager.setAdapter(homePagerAdapter);
    }
}
