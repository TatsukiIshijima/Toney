package com.io.tatsuki.toney.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.io.tatsuki.toney.R;

/**
 * スプラッシュ画面
 */

public class SplashActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int PERMISSION_READ_EX_STORAGE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onCreate");
        checkPermissionExStorage();
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
        } else {
            // ホーム画面遷移
            startActivity(HomeActivity.startIntent(this));
            finish();
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
        Log.d(TAG, "RequestCode : " + requestCode);
        switch (requestCode) {
            case PERMISSION_READ_EX_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // パーミッションが得られた場合
                    // ホーム画面に遷移
                    startActivity(HomeActivity.startIntent(this));
                    finish();
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
