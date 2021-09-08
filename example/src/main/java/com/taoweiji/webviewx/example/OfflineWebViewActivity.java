package com.taoweiji.webviewx.example;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.WebViewX;
import com.taoweiji.webviewx.WebViewXBridge;
import com.taoweiji.webviewx.WebViewXClient;

public class OfflineWebViewActivity extends AppCompatActivity {
    private WebViewX webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_web_view);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addLocalResource("http://taoweiji.com/", "file:///android_asset/taoweiji.com/");
        webView.addLocalResource("http://2048.com/", "file:///android_asset/2048/");
        webView.addLocalResource("http://flappy-bird.com/", "file:///android_asset/flappy-bird/");
        webView.addLocalResource("http://tetris.com/", "file:///android_asset/tetris/");
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        webView.addInterceptor(new WebViewXBridge.Interceptor() {
            @Override
            public boolean invoke(@NonNull ApiCaller caller) {
                caller.putExtra("from", "OfflineWebViewActivity");
                return false;
            }

            @Override
            public boolean interrupt(@NonNull ApiCaller caller, @Nullable String url) {
                return false;
            }
        });
        webView.setLoadOption("load_from", "OfflineWebViewActivity");
        webView.setWebViewClient(new WebViewXClient(webView.getWebViewXBridge()) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }
        });
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