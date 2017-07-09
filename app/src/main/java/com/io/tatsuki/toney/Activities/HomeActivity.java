package com.io.tatsuki.toney.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.io.tatsuki.toney.Adapters.HomePagerAdapter;
import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Fragments.DummyFragment;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.ViewModels.HomeViewModel;
import com.io.tatsuki.toney.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PERMISSION_READ_EX_STORAGE_CODE = 0;
    private LocalAccess localAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissionExStorage();
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        HomeViewModel homeViewModel = new HomeViewModel();
        binding.setHomeViewModel(homeViewModel);
        setViews(binding);
        setBottomSheetBehavior(binding);

        localAccess = new LocalAccess(this);
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

    /**
     * BottomSheet挙動
     * @param binding
     */
    private void setBottomSheetBehavior(final ActivityHomeBinding binding) {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheetRelativeLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    // レイアウトの切り替え
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        binding.bottomSheetLayout.bottomSheetRelativeLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        binding.bottomSheetLayout.bottomSheetRelativeLayout.setBackgroundColor(getColor(R.color.colorAccent));
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    protected void onResume() {
        // イベントの登録
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // イベントの解除
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    /**
     * EventBusによる通知受け取り
     * @param event
     */
    @Subscribe
    public void onClickEvent(ClickEvent event) {
        Log.d(TAG, event.getRequestCode());
        // 画面切り替えテスト
        DummyFragment dummyFragment = new DummyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_home_frame_layout, dummyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 外部ストレージアクセスのパーミッションチェック
     */
    private void checkPermissionExStorage() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        // パーミッションが許可されていない場合
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // パーミッションの要求
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EX_STORAGE_CODE);
        }
    }

    /**
     * パーミッションダイアログの結果を受け取る
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EX_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // パーミッションが得られた場合
                } else {
                    // パーミッションが得られない場合、終了
                    finish();
                }
                break;
            default:
                break;
        }
    }
}