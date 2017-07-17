package com.io.tatsuki.toney.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.io.tatsuki.toney.R;
import com.io.tatsuki.toney.Utils.ServiceConstant;

/**
 * 音楽再生のためのServiceクラス
 */

public class MusicService extends Service {

    private static final String TAG = MusicService.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand Received start id : " + startId + " : " + intent);

        if (intent.getAction().equals(ServiceConstant.SERVICE_START)) {
            Log.d(TAG, "Start Service");
        }

        if (intent.getAction().equals(ServiceConstant.SERVICE_STOP)) {
            Log.d(TAG, "Stop Service");
            stopSelf();
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_PLAY)) {
            // TODO:再生中か停止中かを判断する
            Log.d(TAG, "Music Play or Pause");
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_PREV)) {
            Log.d(TAG, "Music Prev");
        }

        if (intent.getAction().equals(ServiceConstant.MUSIC_NEXT)) {
            Log.d(TAG, "Music Next");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
        views.setOnClickPendingIntent(R.id.notification_play_pause_button, playIntent());
        views.setOnClickPendingIntent(R.id.notification_prev_button, prevIntent());
        views.setOnClickPendingIntent(R.id.notification_next_button, nextIntent());
        views.setOnClickPendingIntent(R.id.notification_collapse_button, stopIntent());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setCustomContentView(views);
        // TODO:アイコンを変更
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        startForeground(ServiceConstant.NOTIFICATION_ID, builder.build());
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

    // 以下のメソッドはbindServiceで呼び出し
    /**
     * 再生、停止のためのIntent
     * @return
     */
    public PendingIntent playIntent() {
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(ServiceConstant.MUSIC_PLAY);
        return PendingIntent.getService(this, 0, playIntent, 0);
    }

    /**
     * 前の曲に戻るためのIntent
     * @return
     */
    public PendingIntent prevIntent() {
        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.setAction(ServiceConstant.MUSIC_PREV);
        return PendingIntent.getService(this, 0, prevIntent, 0);
    }

    /**
     * 次の曲に進むためのIntent
     * @return
     */
    public PendingIntent nextIntent() {
        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(ServiceConstant.MUSIC_NEXT);
        return PendingIntent.getService(this, 0, nextIntent, 0);
    }
}
