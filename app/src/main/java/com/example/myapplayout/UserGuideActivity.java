package com.example.myapplayout;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserGuideActivity extends AppCompatActivity {

    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "UserGuideActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_guide_activity);
        Log.d(TAG, "onCreate: Started");

        WebView webView = findViewById(R.id.userGuideView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }

}
