package com.taoweiji.webviewx.x5;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.EventCenter;
import com.taoweiji.webviewx.IWebView;
import com.taoweiji.webviewx.WebViewXBridge;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONObject;

public class X5WebViewX extends WebView implements EventCenter.Register, IWebView {
    private WebViewXBridge webViewXBridge;
    //    private WebViewXClient webViewXClient;
    private String eventRegisterId;

    public X5WebViewX(@NonNull Context context) {
        super(context);
        init();
    }

    public X5WebViewX(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public X5WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public X5WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

//    public X5WebViewX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
//        super(context, attrs, defStyleAttr, privateBrowsing);
//        init();
//    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        webViewXBridge = new WebViewXBridge(this);
        setWebViewClient(new WebViewClient());
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
        EventCenter.getInstance().broadcastEvent(name, event);
    }

    public void addInterceptor(WebViewXBridge.Interceptor bridgeHandler) {
        webViewXBridge.addInterceptor(bridgeHandler);
    }

    @Override
    public void setWebViewClient(@NonNull WebViewClient client) {
        super.setWebViewClient(new WebViewXClientWrapper(webViewXBridge, client));
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

    public final void registerApi(Api... api) {
        webViewXBridge.registerApi(api);
    }

    @Override
    public void loadUrl(@NonNull String url) {
        super.loadUrl(url);
    }

}
