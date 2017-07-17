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

        if (intent.getBooleanExtra(ServiceConstant.SERVICE_STOP , false)) {
            Log.d(TAG, "Stop Service");
            stopSelf();
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
        stopIntent.putExtra(ServiceConstant.SERVICE_STOP, true);
        PendingIntent pendStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        return pendStopIntent;
    }
}
