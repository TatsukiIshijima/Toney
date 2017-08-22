package com.io.tatsuki.toney.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.io.tatsuki.toney.Activities.HomeActivity;
import com.io.tatsuki.toney.Events.ActivityEvent;
import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Events.PlayPauseEvent;
import com.io.tatsuki.toney.Events.SelectSongEvent;
import com.io.tatsuki.toney.Events.RepeatEvent;
import com.io.tatsuki.toney.Events.ShuffleEvent;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.Utils.ServiceConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Random;

/**
 * 音楽再生のためのServiceクラス
 */

public class MusicService extends Service implements ExoPlayer.EventListener{

    private static final String TAG = MusicService.class.getSimpleName();
    public static final String POSITION_KEY = "POSITION";
    public static final String SONGS_KEY = "SONGS";
    private final IBinder binder = new MusicServiceBinder();
    private boolean isActivityDestroy;
    private boolean isRepeat;
    private boolean isShuffle;
    private ArrayList<Song> songs;
    private int position;
    private SimpleExoPlayer simpleExoPlayer;
    private int repeatPosition;
    private Random random;

    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        // EventBusの登録
        EventBus.getDefault().register(this);
        // Playerの初期化
        initPlayer();
        random = new Random();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand Received start id : " + startId + " : " + intent);

        if (intent.getAction().equals(ServiceConstant.SERVICE_START)) {
            Log.d(TAG, "Start Service");
            if (intent != null) {
                Log.d(TAG, "Start Service : Not Null");

                // SongAdapterでのタップされた位置とリストを受け取る
                Bundle bundle = intent.getExtras();
                position = bundle.getInt(POSITION_KEY);
                songs = (ArrayList<Song>) bundle.getSerializable(SONGS_KEY);
                // 再生
                play(position);
            }
        }

        // 以下のメソッドはNotificationのイベント

        if (intent.getAction().equals(ServiceConstant.SERVICE_STOP)) {
            // Activityが起動中はServiceをStopさせないようにする
            if (isActivityDestroy) {
                Log.d(TAG, "Stop Service");
                stopSelf();
            }
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_PLAY)) {
            pause();
            // NotificationのイベントをActivityに通知
            EventBus.getDefault().post(new PlayPauseEvent(simpleExoPlayer.getPlayWhenReady()));
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_PREV)) {
            Log.d(TAG, "Music Prev");
            prev();
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_NEXT)) {
            Log.d(TAG, "Music Next");
            next();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // EventBusの解除
        EventBus.getDefault().unregister(this);
        // playerの停止
        simpleExoPlayer.setPlayWhenReady(false);
    }

    /**
     * ホーム画面のコントローラーや再生画面のイベントを受け取る
     * @param event
     */
    @Subscribe
    public void onClickEvent(ClickEvent event) {
        Log.d(TAG, "onClickEvent : " + event.getRequestCode());
        switch (event.getRequestCode()) {
            case ClickEvent.prevCode:
                prev();
                break;
            case ClickEvent.playCode:
                pause();
                // コントローラーの更新のためEventで通知
                EventBus.getDefault().post(new PlayPauseEvent(simpleExoPlayer.getPlayWhenReady()));
                break;
            case ClickEvent.nextCode:
                next();
                break;
            case ClickEvent.repeatCode:
                if (getRepeat()) {
                    setRepeat(false);
                } else {
                    setRepeat(true);
                }
                EventBus.getDefault().post(new RepeatEvent(isRepeat));
                break;
            case ClickEvent.shuffleCode:
                if (getShuffle()) {
                    setShuffle(false);
                } else {
                    setShuffle(true);
                }
                EventBus.getDefault().post(new ShuffleEvent(isShuffle));
                break;
            default:
                break;
        }
    }

    /**
     * Activityの状態を受け取る
     * @param event
     */
    @Subscribe
    public void onActivityEvent(ActivityEvent event) {
        isActivityDestroy = event.isDestroy();
    }

    /**
     * Notificationの表示
     * @param songTitle
     * @param artistName
     * @param albumArtPath
     */
    private void showNotification(String songTitle, String artistName, String albumArtPath) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
        views.setOnClickPendingIntent(R.id.notification_play_pause_button, playIntent());
        views.setOnClickPendingIntent(R.id.notification_prev_button, prevIntent());
        views.setOnClickPendingIntent(R.id.notification_next_button, nextIntent());
        views.setOnClickPendingIntent(R.id.notification_collapse_button, stopIntent());

        views.setTextViewText(R.id.notification_song_title_text, songTitle);
        views.setTextViewText(R.id.notification_artist_name_text, artistName);
        if (albumArtPath != null) {
            Bitmap bitmap = ImageUtil.decodeBitmap(albumArtPath, 100, 100);
            views.setImageViewBitmap(R.id.notification_album_image_view, bitmap);
        } else {
            views.setImageViewResource(R.id.notification_album_image_view, R.drawable.ic_default_album);
        }
        if (simpleExoPlayer.getPlayWhenReady()) {
            views.setImageViewResource(R.id.notification_play_pause_button, R.drawable.notification_pause);
        } else {
            views.setImageViewResource(R.id.notification_play_pause_button, R.drawable.notification_play);
        }

        // NotificationからActivity起動
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // 第２引数に注意　デフォルトの数字ではActivityが起動しない可能性あり
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                                                777,
                                                                notificationIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setCustomContentView(views);
        builder.setSmallIcon(R.mipmap.ic_stat_ic_tone);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(ServiceConstant.NOTIFICATION_ID, notification);
    }

    /**
     * Serviceを止めるIntent
     * @return
     */
    private PendingIntent stopIntent() {
        Intent stopIntent = new Intent(this, MusicService.class);
        stopIntent.setAction(ServiceConstant.SERVICE_STOP);
        return PendingIntent.getService(this, 0, stopIntent, 0);
    }

    /**
     * 再生、停止のためのIntent
     * @return
     */
    private PendingIntent playIntent() {
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(ServiceConstant.MUSIC_PLAY);
        return PendingIntent.getService(this, 0, playIntent, 0);
    }

    /**
     * 前の曲に戻るためのIntent
     * @return
     */
    private PendingIntent prevIntent() {
        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.setAction(ServiceConstant.MUSIC_PREV);
        return PendingIntent.getService(this, 0, prevIntent, 0);
    }

    /**
     * 次の曲に進むためのIntent
     * @return
     */
    private PendingIntent nextIntent() {
        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(ServiceConstant.MUSIC_NEXT);
        return PendingIntent.getService(this, 0, nextIntent, 0);
    }

    /**
     * ExoPlayerの初期化
     */
    private void initPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayer.addListener(this);
    }

    /**
     * ExoPlayerの準備（曲のセット）
     * @param songPath  曲のパス
     */
    private void prepare(String songPath) {
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(songPath),
                                                                     dataSourceFactory,
                                                                     extractorsFactory,
                                                                     null,
                                                                     null);
        simpleExoPlayer.prepare(mediaSource);
    }

    /**
     * 前の曲再生
     */
    public void prev() {
        // リピート設定時
        // リピート設定とシャッフル設定が同時の時はリピートを優先させる
        if (getRepeat()) {
            repeatPosition = position;
            position = repeatPosition;
        } else {
            // シャッフル設定時
            if (getShuffle()) {
                int newPotion = position;
                while (newPotion == position) {
                    newPotion = random.nextInt(songs.size());
                }
                position = newPotion;
            } else {
                if (position != 0) {
                    position -= 1;
                }
            }
        }
        play(position);
    }

    /**
     * 再生
     * @param position  ポジション
     */
    public void play(int position) {
        // 再生中なら停止させる
        if (simpleExoPlayer.getPlayWhenReady()) simpleExoPlayer.setPlayWhenReady(false);
        prepare(songs.get(position).getSongPath());
        simpleExoPlayer.setPlayWhenReady(true);
        showNotification(songs.get(position).getSongName(),
                         songs.get(position).getSongArtist(),
                         songs.get(position).getSongArtPath());
        // Activityに通知
        EventBus.getDefault().post(new SelectSongEvent(songs.get(position)));
    }

    /**
     * 一時停止
     */
    public void pause() {
        if (!simpleExoPlayer.getPlayWhenReady()) {
            simpleExoPlayer.setPlayWhenReady(true);
        } else {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        // Notification更新
        showNotification(songs.get(position).getSongName(),
                         songs.get(position).getSongArtist(),
                         songs.get(position).getSongArtPath());
    }

    /**
     * 次の曲再生
     */
    public void next() {
        // リピート設定時
        // リピート設定とシャッフル設定が同時の時はリピートを優先させる
        if (getRepeat()) {
            repeatPosition = position;
            position = repeatPosition;
        } else {
            // シャッフル設定時
            if (getShuffle()) {
                int newPotion = position;
                while (newPotion == position) {
                    newPotion = random.nextInt(songs.size());
                }
                position = newPotion;
            } else {
                if (position != songs.size() - 1) {
                    position += 1;
                }
            }
        }
        play(position);
    }

    /**
     * シャッフル設定状態の取得
     * @return
     */
    public boolean getShuffle() {
        return isShuffle;
    }

    /**
     * シャッフル設定
     */
    public void setShuffle(boolean isShuffle) {
        Log.d(TAG, "setShuffle");
        this.isShuffle = isShuffle;
    }

    /**
     * リピート設定状態の取得
     * @return
     */
    public boolean getRepeat() {
        return isRepeat;
    }

    /**
     * リピート設定
     */
    public void setRepeat(boolean isRepeat) {
        Log.d(TAG, "setRepeat");
        this.isRepeat = isRepeat;
    }

    /**
     * 再生状態を取得
     * @return
     */
    public boolean getPlayState() {
        return simpleExoPlayer.getPlayWhenReady();
    }

    /**
     * 再生中の位置の取得
     * @return
     */
    public long getCurrentPosition() {
        return simpleExoPlayer.getCurrentPosition();
    }

    /**
     * 再生中の曲の取得
     * @return
     */
    public Song getSong() {
        if (songs != null) {
            return songs.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "onLoadingChanged : " + isLoading);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                Log.d(TAG, "onPlayerStateChanged : State buffering");
                break;
            case ExoPlayer.STATE_READY:
                Log.d(TAG, "onPlayerStateChanged : State ready");
                break;
            case ExoPlayer.STATE_ENDED:
                Log.d(TAG, "onPlayerStateChanged : State ended");
                next();
                break;
            case ExoPlayer.STATE_IDLE:
                Log.d(TAG, "onPlayerStateChanged : State IDLE");
                break;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "onPlayerError : " + error);
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
