package com.example.myapplayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteActivity extends AppCompatActivity {

    private WebView webView;
    private String mbookID;

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        mbookID = (String) getIntent().getStringExtra("BookID");

        String url = "https://books.google.co.uk/books?id=" + mbookID;
        webView =  (WebView) findViewById(R.id.webView);
        webView.setWebViewClient((new WebViewClient()));
        webView.loadUrl(url);


    }
}