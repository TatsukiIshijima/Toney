package com.io.tatsuki.toney.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.io.tatsuki.toney.Adapters.HomePagerAdapter;
import com.io.tatsuki.toney.Events.ActivityEvent;
import com.io.tatsuki.toney.Events.SongEvent;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Services.MusicService;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.Utils.ServiceConstant;
import com.io.tatsuki.toney.ViewModels.HomeViewModel;
import com.io.tatsuki.toney.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PERMISSION_READ_EX_STORAGE_CODE = 0;
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissionExStorage();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeViewModel = new HomeViewModel();
        binding.setHomeViewModel(homeViewModel);
        binding.activityHomeBottomSheet.fragmentPlaying.setHomeViewModel(homeViewModel);
        binding.activityHomeBottomSheet.fragmentController.setHomeViewModel(homeViewModel);
        setViews(binding);
        setBottomSheetBehavior(binding);
        startService();
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
        binding.activityHomeViewpager.addOnPageChangeListener(homePagerAdapter);
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
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(binding.activityHomeBottomSheet.bottomSheetLayout);
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
                        showPlayingScreen();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        hidePlayingScreen();
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

    /**
     * 再生中画面の表示
     */
    private void showPlayingScreen() {
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerLinearLayout.setVisibility(View.INVISIBLE);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 再生中画面の非表示
     * コントロール画面を表示
     */
    private void hidePlayingScreen() {
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingLinearLayout.setVisibility(View.INVISIBLE);
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * BottomSheetに曲名、アーティスト名、アルバムアートを表示
     * @param song
     */
    private void showSongAndArtist(Song song) {
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerArtistText.setText(song.getSongArtist());
        ImageUtil.setDownloadImage(this, song.getSongArtPath(), binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageView);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingArtistText.setText(song.getSongArtist());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setCoverURL(String.valueOf(Uri.fromFile(new File(song.getSongArtPath()))));
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

    @Override
    public void onDestroy() {
        // Activityが破棄されたことをServiceに通知
        EventBus.getDefault().post(new ActivityEvent(true));
        super.onDestroy();
    }

    /**
     * EventBusによる選択された曲の受け取り
     * @param event
     */
    @Subscribe
    public void SongEvent(SongEvent event) {
        showSongAndArtist(event.getSong());
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

    /**
     * Serviceの開始
     */
    private void startService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(ServiceConstant.SERVICE_START);
        startService(intent);
    }
}