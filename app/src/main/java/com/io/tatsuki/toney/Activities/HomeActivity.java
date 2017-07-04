package com.io.tatsuki.toney.Activities;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.io.tatsuki.toney.Adapters.HomePagerAdapter;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.ViewModels.HomeViewModel;
import com.io.tatsuki.toney.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        HomeViewModel homeViewModel = new HomeViewModel();
        binding.setHomeViewModel(homeViewModel);

        setViews(binding);
    }

    /**
     * 各Viewの設定
     */
    private void setViews(ActivityHomeBinding binding) {
        // ToolBar
        setSupportActionBar(binding.activityHomeToolbar);
        // ViewPager
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(this, fragmentManager);
        binding.activityHomeViewpager.setAdapter(homePagerAdapter);
        // Tab
        binding.activityHomeTabLayout.setupWithViewPager(binding.activityHomeViewpager);
        // NavigationDrawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                                 binding.activityHomeDrawerLayout,
                                                                 binding.activityHomeToolbar,
                                                                 R.string.app_name,
                                                                 R.string.app_name);
        binding.activityHomeDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.activityHomeNavigation.setNavigationItemSelectedListener(selectedListener);
    }

    /**
     * メニュー項目分岐
     */
    private NavigationView.OnNavigationItemSelectedListener selectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }
    };
}
