package com.taoweiji.webviewx;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONObject;

public class WebViewX extends WebView {
    private WebViewXBridge webViewXBridge;
    private WebViewXClient webViewXClient;

    public WebViewX(@NonNull Context context) {
        super(context);
        init();
    }

    public WebViewX(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        webViewXBridge = new WebViewXBridge(this);
        webViewXClient = new WebViewXClient(webViewXBridge);
        setWebViewClient(webViewXClient);
    }

    @Override
    public void onResume() {
        super.onResume();
        webViewXBridge.onResume();
    }

    @Override
    public void onPause() {
        webViewXBridge.onPause();
        super.onPause();
    }

    @Override
    public void destroy() {
        webViewXBridge.destroy();
        super.destroy();
    }

    public void addLocalResource(String baseUrl, String baseFilePath) {
        webViewXClient.addLocalResource(baseUrl, baseFilePath);
    }

    public void setLoadOptions(JSONObject options) {
        webViewXBridge.setLoadOptions(options);
    }

    public void addLoadOption(String key, Object value) {
        webViewXBridge.addLoadOption(key, value);
    }

    public WebViewXBridge getWebViewXBridge() {
        return webViewXBridge;
    }

    public void postEvent(String name, JSONObject data) {
        webViewXBridge.postEvent(name, data);
    }

    /**
     * 粘性事件，在postStickyEvent之后订阅也可以收到消息
     */
     void postStickyEvent(String name, JSONObject data) {
        webViewXBridge.postStickyEvent(name, data);
    }

    public void addInterceptor(WebViewXBridge.Interceptor bridgeHandler) {
        webViewXBridge.addInterceptor(bridgeHandler);
    }
}
