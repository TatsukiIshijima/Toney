package com.io.tatsuki.toney.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.io.tatsuki.toney.Events.ActivityEvent;
import com.io.tatsuki.toney.Events.ClickEvent;
import com.io.tatsuki.toney.Events.SongEvent;
import com.io.tatsuki.toney.Models.Song;
import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Utils.ImageUtil;
import com.io.tatsuki.toney.Utils.ServiceConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 音楽再生のためのServiceクラス
 */

public class MusicService extends Service {

    private static final String TAG = MusicService.class.getSimpleName();
    private boolean isActivityDestroy;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private ArrayList<Song> songs;

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        showNotification("SongTitle", "ArtistName", null);
        // EventBusの登録
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand Received start id : " + startId + " : " + intent);

        if (intent.getAction().equals(ServiceConstant.SERVICE_START)) {
            Log.d(TAG, "Start Service");
        }

        if (intent.getAction().equals(ServiceConstant.SERVICE_STOP)) {
            // Activityが起動中はServiceをStopさせないようにする
            if (isActivityDestroy) {
                Log.d(TAG, "Stop Service");
                stopSelf();
            }
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_PLAY)) {
            // TODO:再生中か停止中かを判断する
            Log.d(TAG, "Music Play or Pause");
            play();
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
    }

    @Subscribe
    public void onClickEvent(ClickEvent event) {
        Log.d(TAG, "onClickEvent : " + event.getRequestCode());
        switch (event.getRequestCode()) {
            case ClickEvent.prevCode:
                prev();
                break;
            case ClickEvent.playCode:
                // 曲リストのセット
                setSongs(event.getSongs());
                Song song = event.getSongs().get(event.getPosition());
                showNotification(song.getSongName(), song.getSongArtist(), song.getSongArtPath());
                play();
                // Activityに選択された曲を通知
                EventBus.getDefault().post(new SongEvent(song));
                break;
            case ClickEvent.nextCode:
                next();
                break;
            case ClickEvent.repeatCode:
                break;
            case ClickEvent.shuffleCode:
                break;
            default:
                break;
        }
    }

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setCustomContentView(views);
        // TODO:アイコンを変更
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
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
     * 前の曲再生
     */
    public void prev() {
        Log.d(TAG, "prev");
    }

    /**
     * 再生・停止
     */
    public void play() {
        Log.d(TAG, "play");
    }

    /**
     * 次の曲再生
     */
    public void next() {
        Log.d(TAG, "next");
    }

    /**
     * シャッフル設定
     */
    public void setShuffle() {
        Log.d(TAG, "setShuffle");
    }

    /**
     * リピート設定
     */
    public void setRepeat() {
        Log.d(TAG, "setRepeat");
    }
}
