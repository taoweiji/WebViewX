package com.taoweiji.webviewx;

import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.CallSuper;

public class WebViewXClient extends WebViewClient {
    private final WebViewXBridge webViewXBridge;
    private final WebViewXLocalResource localResource;

    public WebViewXClient(WebViewXBridge webViewXBridge) {
        this.webViewXBridge = webViewXBridge;
        this.localResource = new WebViewXLocalResource();
    }

    @CallSuper
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (webViewXBridge != null) {
            webViewXBridge.onPageStarted(url);
        }
    }

    @CallSuper
    @Override
    public void onPageFinished(WebView view, String url) {
        if (webViewXBridge != null) {
            webViewXBridge.onPageFinished(url);
        }
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = localResource.shouldInterceptRequest(view, url);
        if (response != null) {
            return response;
        }
        return super.shouldInterceptRequest(view, url);
    }

    /**
     * @param baseUrl      映射的url路径 https://game.com/2048
     * @param baseFilePath 支持本地文件及assets文件路径，格式如下：/data/data/com.xx/files/2048 or file:///android_asset/2048
     */
    public void addLocalResource(String baseUrl, String baseFilePath) {
        localResource.addLocalResource(baseUrl, baseFilePath);
    }
}
