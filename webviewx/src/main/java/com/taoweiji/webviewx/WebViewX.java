package com.taoweiji.webviewx;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONObject;

public class WebViewX extends WebView implements EventCenter.Register {
    private WebViewXBridge webViewXBridge;
    private WebViewXClient webViewXClient;
    private String eventRegisterId;

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
        EventCenter.getInstance().register(this);
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
        EventCenter.getInstance().unregister(this);
        super.destroy();
    }

    public void addLocalResource(String baseUrl, String baseFilePath) {
        webViewXClient.addLocalResource(baseUrl, baseFilePath);
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
     * 粘性事件，在postStickyEvent之后订阅也可以收到消息
     */
    public void postStickyEvent(String name, JSONObject data) {
        webViewXBridge.postStickyEvent(name, data);
    }

    public void removeStickyEvent(String name) {
        webViewXBridge.removeStickyEvent(name);
    }

    public void broadcastEvent(String name, JSONObject event) {
        EventCenter.getInstance().broadcastEvent(name, event);
    }

    public void addInterceptor(WebViewXBridge.Interceptor bridgeHandler) {
        webViewXBridge.addInterceptor(bridgeHandler);
    }

    public void setWebViewClient(WebViewXClient client) {
        super.setWebViewClient(client);
    }

    /**
     * 请使用 {@link #setWebViewClient(WebViewXClient)}
     */
    @Deprecated
    @Override
    public void setWebViewClient(@NonNull WebViewClient client) {
        super.setWebViewClient(client);
    }


    @Override
    public WebViewXBridge getWebViewXBridge() {
        return webViewXBridge;
    }

    public void setEventRegisterId(String eventRegisterId) {
        this.eventRegisterId = eventRegisterId;
    }

    @Override
    public String getEventRegisterId() {
        if (eventRegisterId != null) {
            return eventRegisterId;
        }
        return String.valueOf(this.hashCode());
    }

    @Override
    public void loadUrl(@NonNull String url) {
        super.loadUrl(url);
    }

}
