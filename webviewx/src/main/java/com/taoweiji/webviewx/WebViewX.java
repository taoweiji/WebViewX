package com.taoweiji.webviewx;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONObject;

public class WebViewX extends WebView implements IWebView {
    private WebViewXBridge webViewXBridge;


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
        webViewXBridge = new WebViewXBridge((IWebView) this);
        setWebViewClient(new WebViewClient());
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
        webViewXBridge.addLocalResource(baseUrl, baseFilePath);
    }

    public void setLoadOptions(JSONObject options) {
        webViewXBridge.setLoadOptions(options);
    }

    public void setLoadOption(String key, Object value) {
        webViewXBridge.setLoadOption(key, value);
    }

    public void postEvent(String name, JSONObject data) {
        webViewXBridge.postEvent(name, data);
    }

    /**
     * ??????????????????postStickyEvent?????????????????????????????????
     */
    public void postStickyEvent(String name, JSONObject data) {
        webViewXBridge.postStickyEvent(name, data);
    }

    public void removeStickyEvent(String name) {
        webViewXBridge.removeStickyEvent(name);
    }

    public void broadcastEvent(String name, JSONObject event) {
        webViewXBridge.broadcastEvent(name, event);
    }

    public void addInterceptor(WebViewXBridge.Interceptor bridgeHandler) {
        webViewXBridge.addInterceptor(bridgeHandler);
    }

    @Override
    public void setWebViewClient(@NonNull WebViewClient client) {
        super.setWebViewClient(new WebViewXClientWrapper(webViewXBridge, client));
    }

    public WebViewXBridge getWebViewXBridge() {
        return webViewXBridge;
    }

    public void setEventRegisterId(String eventRegisterId) {
        webViewXBridge.setEventRegisterId(eventRegisterId);
    }

    public final void registerApi(Api... api) {
        webViewXBridge.registerApi(api);
    }

    @Override
    public void loadUrl(@NonNull String url) {
        super.loadUrl(url);
    }

    public String getEventRegisterId() {
        return webViewXBridge.getEventRegisterId();
    }
}
