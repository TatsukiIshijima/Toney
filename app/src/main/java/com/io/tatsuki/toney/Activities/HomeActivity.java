package com.io.tatsuki.toney.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
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
import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Services.MusicService;
import com.io.tatsuki.toney.Utils.ServiceConstant;
import com.io.tatsuki.toney.ViewModels.HomeViewModel;
import com.io.tatsuki.toney.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PERMISSION_READ_EX_STORAGE_CODE = 0;
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private MusicService musicBound;
    private boolean isBound;
    private boolean isStartService = false;

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
     * BottomSheetに曲名、アーティスト名を表示
     * @param song
     */
    private void showSongAndArtist(Song song) {
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerArtistText.setText(song.getSongArtist());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingArtistText.setText(song.getSongArtist());
    }

    @Override
    protected void onResume() {
        // イベントの登録
        EventBus.getDefault().register(this);
        super.onResume();
        // Serviceをbindする
        if (!isBound) {
            doBindService();
        }
        // TODO:Serviceが終了していたら再起動
        // TODO:Test必須
        if (!isStartService) {
            Log.d(TAG, "ReStartService");
            startService();
        }
    }

    @Override
    protected void onPause() {
        // イベントの解除
        EventBus.getDefault().unregister(this);
        super.onPause();
        // Serviceをunbindする
        doUnbindService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * EventBusによる通知受け取り
     * @param event
     */
    @Subscribe
    public void onClickEvent(ClickEvent event) {
        Log.d(TAG, "onClickEvent : " + event.getRequestCode());
        switch (event.getRequestCode()) {
            case ClickEvent.prevCode:
                musicBound.prev();
                break;
            case ClickEvent.playCode:
                musicBound.play();
                if (event.getSong() != null) {
                    showSongAndArtist(event.getSong());
                }
                break;
            case ClickEvent.nextCode:
                musicBound.next();
                break;
            case ClickEvent.repeatCode:
                break;
            case ClickEvent.shuffleCode:
                break;
            default:
                break;
        }
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
        isStartService = true;
        startService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            musicBound = ((MusicService.MusicServiceBinder) iBinder).getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisConnected");
            musicBound = null;
        }
    };

    /**
     * Serviceとの接続を確立
     */
    private void doBindService() {
        bindService(new Intent(this, MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    /**
     * Connectionの解除
     */
    private void doUnbindService() {
        unbindService(serviceConnection);
        isBound = false;
    }
}