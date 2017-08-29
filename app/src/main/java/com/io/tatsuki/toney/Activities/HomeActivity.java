package com.io.tatsuki.toney.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.ads.AdRequest;
import com.io.tatsuki.toney.Adapters.HomePagerAdapter;
import com.io.tatsuki.toney.Events.ActivityEvent;
import com.io.tatsuki.toney.Events.PlayPauseEvent;
import com.io.tatsuki.toney.Events.SelectSongEvent;
import com.io.tatsuki.toney.Events.RepeatEvent;
import com.io.tatsuki.toney.Events.ShuffleEvent;
import com.io.tatsuki.toney.Events.TransitionEvent;
import com.io.tatsuki.toney.Fragments.SongFragment;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Repositories.LocalAccess;
import com.io.tatsuki.toney.Services.MusicService;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.ViewModels.HomeViewModel;
import com.io.tatsuki.toney.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private MusicService musicService;
    private boolean isBound;
    private LocalAccess localAccess;

    /**
     * 画面遷移
     * @param   activity
     * @return  intent
     */
    public static Intent startIntent(@NonNull Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activityが開始されたことをServiceに通知
        EventBus.getDefault().post(new ActivityEvent(false));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.activityHomeBottomSheet.bottomSheetLayout);
        homeViewModel = new HomeViewModel();
        binding.setHomeViewModel(homeViewModel);
        binding.activityHomeBottomSheet.fragmentPlaying.setHomeViewModel(homeViewModel);
        binding.activityHomeBottomSheet.fragmentController.setHomeViewModel(homeViewModel);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setOnClickListener(this);
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerLinearLayout.setOnClickListener(this);
        setViews(binding);
        setBottomSheetBehavior();
        initAdMod();
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

        // NavigationDrawer内のShuffleボタン
        SwitchCompat shuffleSwitch = (SwitchCompat) binding.activityHomeNavigation.getMenu().getItem(0).getActionView();
        shuffleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Shuffle : onCheckedChanged" + isChecked);
                musicService.setShuffle(isChecked);
                updateShuffle(isChecked);
            }
        });
        // NavigationDrawerのRepeatボタン
        SwitchCompat repeatSwitch = (SwitchCompat) binding.activityHomeNavigation.getMenu().getItem(1).getActionView();
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "Repeat : onCheckedChanged" + isChecked);
                musicService.setRepeat(isChecked);
                updateRepeat(isChecked);
            }
        });
    }

    /**
     * 広告表示の初期化
     */
    private void initAdMod() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.activityAdView.loadAd(adRequest);
    }

    /**
     * 各Viewのクリックイベント
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // MusicPlayerViewのクリックイベント
            case R.id.fragment_playing_mpv:
                if (musicService.getSong() != null) {
                    musicService.pause();
                    updateControllerAndPlaying(musicService.getPlayState());
                }
                break;
            // コントローラー画面のクリックイベント
            case R.id.fragment_controller:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            default:
                break;
        }
    }

    /**
     * メニュー項目分岐
     */
    private NavigationView.OnNavigationItemSelectedListener selectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_license:
                    Log.d(TAG, "License");
                    startActivity(LicenseActivity.startIntent(HomeActivity.this));
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * BottomSheet挙動
     */
    private void setBottomSheetBehavior() {
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
                        // タップが下に伝搬してしまうのを防ぐ措置
                        binding.activityHomeViewpager.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        hidePlayingScreen();
                        binding.activityHomeViewpager.setVisibility(View.VISIBLE);
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
    private void showSongAndArtist(Song song, int currentProgress) {
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerArtistText.setText(song.getSongArtist());
        ImageUtil.setDownloadImage(this, song.getSongArtPath(), binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageView);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingSongText.setText(song.getSongName());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingArtistText.setText(song.getSongArtist());
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setMax(calcSongDuration(song.getDuration()));
        if (song.getSongArtPath() != null) {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setCoverURL(String.valueOf(Uri.fromFile(new File(song.getSongArtPath()))));
        } else {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setCoverDrawable(R.drawable.ic_default_album);
        }
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setProgress(currentProgress);
    }

    /**
     * 曲の長さを秒単位に変換
     * @param duration
     * @return
     */
    private int calcSongDuration(long duration) {
        long minute = duration / 60000;
        long second = (duration - (minute * 60000)) / 1000;
        long time = minute * 60 + second;
        return (int)time;
    }

    /**
     * Serviceから再生状態を取得し、ボタン等のViewを変える
     */
    private void updateControllerAndPlaying(boolean isPlaying) {
        if (isPlaying) {
            // バックグラウンドからの復帰でボタンが変更しない場合があるため
            // 再生中であれば一度停止させてから再生させる処理にする
            // 停止中はその逆
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.stop();
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.start();
            binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonPlay.setBackground(getDrawable(R.drawable.ic_pause_white));
        } else {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.start();
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.stop();
            binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonPlay.setBackground(getDrawable(R.drawable.ic_play_white));
        }
    }

    /**
     * シャッフルボタンの表示更新
     */
    private void updateShuffle(boolean isShuffle) {
        Log.d(TAG, "updateShuffle : " + isShuffle);
        SwitchCompat shuffleSwitch = (SwitchCompat) binding.activityHomeNavigation.getMenu().getItem(0).getActionView();
        shuffleSwitch.setChecked(isShuffle);
        if (isShuffle) {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingShuffleButton.setBackground(getDrawable(R.drawable.ic_shuffle_white));
        } else {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingShuffleButton.setBackground(getDrawable(R.drawable.ic_shuffle_gray));
        }
    }

    /**
     * リピートボタンの表示更新
     */
    private void updateRepeat(boolean isRepeat) {
        Log.d(TAG, "updateRepeat : " + isRepeat);
        SwitchCompat repeatSwitch = (SwitchCompat)binding.activityHomeNavigation.getMenu().getItem(1).getActionView();
        repeatSwitch.setChecked(isRepeat);
        if (isRepeat) {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingRepeatButton.setBackground(getDrawable(R.drawable.ic_repeat_white));
        } else {
            binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingRepeatButton.setBackground(getDrawable(R.drawable.ic_repeat_gray));
        }
    }

    /**
     * 再生ボタン等のタップの可否変更
     * @param enable
     */
    private void enablePlayerButton(boolean enable) {
        // コントローラー画面
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonPrev.setEnabled(enable);
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonPlay.setEnabled(enable);
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonNext.setEnabled(enable);
        // 再生画面
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.setEnabled(enable);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingPrevButton.setEnabled(enable);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingNextButton.setEnabled(enable);
    }

    @Override
    protected void onResume() {
        // イベントの登録
        EventBus.getDefault().register(this);
        doBindService();
        super.onResume();
        if (binding.activityAdView != null) {
            binding.activityAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        // イベントの解除
        EventBus.getDefault().unregister(this);
        // 再生停止状態であればサービスを停止する
        if (musicService != null) {
            if (!musicService.getPlayState()) {
                musicService.stopSelf();
            }
        }
        doUnbindService();
        super.onPause();
        if (binding.activityAdView != null) {
            binding.activityAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        // Activityが破棄されたことをServiceに通知
        EventBus.getDefault().post(new ActivityEvent(true));
        super.onDestroy();
        if (binding.activityAdView != null) {
            binding.activityAdView.destroy();
        }
    }

    /**
     * バックキーが押された時
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // バックキーが押された場合
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // BottomSheetがフルの状態であれば戻す
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return false;
            }
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * EventBusによる選択された曲の受け取り
     * 画面更新のため
     * @param event
     */
    @Subscribe
    public void SelectSongEvent(SelectSongEvent event) {
        showSongAndArtist(event.getSong(), 0);
        binding.activityHomeBottomSheet.fragmentPlaying.fragmentPlayingMpv.start();
        binding.activityHomeBottomSheet.fragmentController.fragmentControllerImageButtonPlay.setBackground(getDrawable(R.drawable.ic_pause_white));
        // 各ボタンを押せるよう設定
        enablePlayerButton(true);
    }

    /**
     * 再生・停止ボタンのイベントを受け取る
     * 画面更新のため
     * @param event
     */
    @Subscribe
    public void PlayPauseEvent(PlayPauseEvent event) {
        updateControllerAndPlaying(event.isPlaying());
    }

    /**
     * シャッフルボタンのクリックイベントを受け取る
     * 画面更新のため
     * @param event
     */
    @Subscribe
    public void ShuffleEvent(ShuffleEvent event) {
        updateShuffle(event.isShuffle());
    }

    /**
     * リピートボタンのクリックイベントを受け取る
     * 画面更新のため
     */
    @Subscribe
    public void RepeatEvent(RepeatEvent event) {
        updateRepeat(event.isRepeat());
    }

    /**
     * 画面遷移のイベントを受け取る
     * @param event
     */
    @Subscribe
    public void TransitionEvent(TransitionEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SongFragment songFragment;
        switch (event.getFlag()) {
            case TransitionEvent.ALBUM_TO_SONG_FLAG:
                songFragment = SongFragment.newInstance(localAccess.getSongs(event.getId(), null));
                transaction.replace(R.id.root_album_frame_layout, songFragment);
                break;
            case TransitionEvent.ARTIST_TO_SONG_FLAG:
                songFragment = SongFragment.newInstance(localAccess.getSongs(null, event.getId()));
                transaction.replace(R.id.root_artist_frame_layout, songFragment);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Serviceと接続
     */
    public void doBindService() {
        bindService(new Intent(this, MusicService.class), connection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    /**
     * Serviceと切断
     */
    public void doUnbindService() {
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MusicServiceBinder) iBinder).getService();
            // 初回起動時にはsongsがセットされていないのでNull判定を行う
            if (musicService.getSong() != null) {
                showSongAndArtist(musicService.getSong(), calcSongDuration(musicService.getCurrentPosition()));
                updateControllerAndPlaying(musicService.getPlayState());
                updateShuffle(musicService.getShuffle());
                updateRepeat(musicService.getRepeat());
                // 各ボタンを押せるよう設定
                enablePlayerButton(true);
            } else {
                // 各ボタンを押せないよう設定
                enablePlayerButton(false);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };
}