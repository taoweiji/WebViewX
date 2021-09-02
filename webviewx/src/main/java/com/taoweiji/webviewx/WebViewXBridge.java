package com.taoweiji.webviewx;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        webView.loadUrl("javascript:webViewX.postEvent('" + name + "'," + data + ")");
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

    public void addLoadOption(String key, Object value) {
        getLifecycle().addLoadOption(key, value);
    }

    /**
     * 粘性事件，在postStickyEvent之后订阅也可以收到消息
     */
     void postStickyEvent(String name, JSONObject data) {

    }

    public interface Interceptor {
        boolean invoke(ApiCaller caller);

        boolean interrupt(@Nullable String url, @NonNull String apiName);
    }
}
