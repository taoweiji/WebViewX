package com.taoweiji.webviewx.x5;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.IWebView;
import com.taoweiji.webviewx.WebViewXBridge;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONObject;

public class WebViewX5 extends WebView implements IWebView {
    private WebViewXBridge webViewXBridge;


    public WebViewX5(@NonNull Context context) {
        super(context);
        init();
    }

    public WebViewX5(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewX5(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
     * 粘性事件，在postStickyEvent之后订阅也可以收到消息
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
