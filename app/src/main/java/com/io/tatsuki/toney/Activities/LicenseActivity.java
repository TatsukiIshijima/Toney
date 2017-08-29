package com.io.tatsuki.toney.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.io.tatsuki.toney.R;

/**
 * ライセンス画面
 */
public class LicenseActivity extends AppCompatActivity {

    public static Intent startIntent(@NonNull Activity activity) {
        Intent intent = new Intent(activity, LicenseActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_license_toolbar);
        toolbar.setTitle("ライセンス");
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.activity_license_webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/license.html");
    }
}
