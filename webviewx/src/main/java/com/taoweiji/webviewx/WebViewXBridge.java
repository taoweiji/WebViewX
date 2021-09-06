package com.taoweiji.webviewx;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebViewXBridge {
    WebView webView;
    String currentUrl;
    private final PageLifecycle pageLifecycle;
    private final List<Interceptor> interceptors = new ArrayList<>();


    @SuppressLint("AddJavascriptInterface")
    public WebViewXBridge(WebView webView) {
        this.webView = webView;
        WebViewXBridgeInterface webViewXBridgeInterface = new WebViewXBridgeInterface(this);
        this.webView.addJavascriptInterface(webViewXBridgeInterface, "webViewXBridge");
        this.pageLifecycle = new PageLifecycle(this);
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(0, interceptor);
    }


    private PageLifecycle getLifecycle() {
        return pageLifecycle;
    }


    public final void destroy() {
        getLifecycle().onUnload();
        this.webView = null;
    }

    public void onPageStarted(String url) {
        this.currentUrl = url;
        getLifecycle().onPageStarted(url);
    }

    public void onPageFinished(String url) {
        getLifecycle().onPageFinished(url);
    }

    @CallSuper
    boolean invoke(ApiCaller caller) {
        switch (caller.getApiName()) {
            case "WebViewX.getLoadOptions":
                caller.success(getLifecycle().getLoadOptions());
                return true;
            case "WebViewX.getStickyEvent":
                caller.success(stickyEvents.get(caller.getParams().optString("name")));
                return true;
            case "WebViewX.broadcastEvent":
                String name = caller.getParams().optString("name");
                JSONObject data = caller.getParams().optJSONObject("data");
                EventCenter.getInstance().broadcastEvent(name, data);
                caller.success();
                return true;
            case "WebViewX.postEvent":
                String name1 = caller.getParams().optString("name");
                JSONObject data1 = caller.getParams().optJSONObject("data");
                postEvent(name1, data1);
                caller.success();
                return true;
            case "WebViewX.postStickyEvent":
                String name2 = caller.getParams().optString("name");
                JSONObject data2 = caller.getParams().optJSONObject("data");
                postStickyEvent(name2, data2);
                caller.success();
                return true;
            case "WebViewX.removeStickyEvent":
                String name3 = caller.getParams().optString("name");
                removeStickyEvent(name3);
                caller.success();
                return true;
            case "WebViewX.isShowed":
                caller.successData(getLifecycle().isShowed());
                return true;
        }
        for (Interceptor interceptor : interceptors) {
            if (interceptor.invoke(caller)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 可以用于白名单拦截，仅让部分
     *
     * @param url     网页链接
     * @param apiName 访问的函数
     * @return 是否允许访问
     */
    boolean hasCallPermission(@Nullable String url, @NonNull String apiName) {
        for (Interceptor interceptor : interceptors) {
            if (interceptor.interrupt(url, apiName)) {
                return false;
            }
        }
        return true;
    }

    public void postEvent(String name, JSONObject data) {
        if (webView == null) {
            new Exception("webView == null").printStackTrace();
            return;
        }
        new Handler(Looper.getMainLooper()).post(() -> webView.loadUrl("javascript:if (window['webViewX'] != undefined) webViewX.receiveEvent('" + name + "'," + data + ")"));
    }

    public void onResume() {
        getLifecycle().onShow();
    }

    public void onPause() {
        getLifecycle().onHide();
    }

    public void setLoadOptions(JSONObject options) {
        getLifecycle().setLoadOptions(options);
    }

    public void setLoadOption(String key, Object value) {
        getLifecycle().setLoadOption(key, value);
    }

    Map<String, JSONObject> stickyEvents = new HashMap<>();

    /**
     * 粘性事件，在postStickyEvent之后订阅也可以收到消息
     */
    public void postStickyEvent(String name, JSONObject data) {
        stickyEvents.put(name, data);
        postEvent(name, data);
    }

    /**
     * 移除粘性事件
     */
    public void removeStickyEvent(String name) {
        stickyEvents.remove(name);
    }

    public interface Interceptor {
        boolean invoke(ApiCaller caller);

        boolean interrupt(@Nullable String url, @NonNull String apiName);
    }
}
