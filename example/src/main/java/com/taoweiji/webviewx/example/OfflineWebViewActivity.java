package com.taoweiji.webviewx.example;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.webviewx.WebViewXClient;

public class OfflineWebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_web_view);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        WebViewXClient webViewXClient = new WebViewXClient(null);
        webViewXClient.addLocalResource("http://taoweiji.com/", "file:///android_asset/taoweiji.com/");
        webViewXClient.addLocalResource("http://2048.com/", "file:///android_asset/2048/");
        webViewXClient.addLocalResource("http://flappy-bird.com/", "file:///android_asset/flappy-bird/");
        webViewXClient.addLocalResource("http://tetris.com/", "file:///android_asset/tetris/");
        webView.setWebViewClient(webViewXClient);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}