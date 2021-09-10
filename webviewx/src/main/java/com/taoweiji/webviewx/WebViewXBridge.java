package com.taoweiji.webviewx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebViewXBridge implements EventCenter.Register{
    static final String EVENT_INVOKE_CALLBACK = "WebViewX.invokeCallback";

    IWebView webView;
    String currentUrl;
    private final PageLifecycle pageLifecycle;
    private final List<Interceptor> interceptors = new ArrayList<>();
    final Map<String, Api> apis = new HashMap<>();
    public final WebViewXLocalResource localResource = new WebViewXLocalResource();
    private String eventRegisterId;


    @SuppressLint("AddJavascriptInterface")
    public WebViewXBridge(IWebView webView) {
        this.webView = webView;
        WebViewXBridgeInterface webViewXBridgeInterface = new WebViewXBridgeInterface(this);
        this.webView.addJavascriptInterface(webViewXBridgeInterface, "webViewXBridge");
        this.pageLifecycle = new PageLifecycle(this);
        EventCenter.getInstance().register(this);
    }

    public WebViewXBridge(WebView webView) {
        this(new IWebView() {
            @Override
            public void loadUrl(String url) {
                webView.loadUrl(url);
            }

            @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
            @Override
            public void addJavascriptInterface(Object obj, String name) {
                webView.addJavascriptInterface(obj, name);
            }

            @Override
            public Context getContext() {
                return webView.getContext();
            }
        });
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(0, interceptor);
    }


    PageLifecycle getLifecycle() {
        return pageLifecycle;
    }


    public final void destroy() {
        getLifecycle().onUnload();
        EventCenter.getInstance().unregister(this);
        this.webView = null;
    }

    public void onPageStarted(String url) {
        this.currentUrl = url;
        getLifecycle().onPageStarted(url);
    }

    public void onPageFinished(String url) {
        getLifecycle().onPageFinished(url);
    }

    boolean invokeInnerInterceptor(ApiCaller caller) {
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

        return false;
    }

    boolean hasCallPermission(ApiCaller caller) {
        for (Interceptor interceptor : interceptors) {
            if (interceptor.interrupt(caller, currentUrl)) {
                return false;
            }
        }
        return true;
    }

    public void postEvent(String name, JSONObject data) {
        if (webView == null) {
            Log.e("WebViewX", "WebView 已经被销毁");
            return;
        }
        new Handler(Looper.getMainLooper()).post(() -> {
            if (webView != null) {
                webView.loadUrl("javascript:if (window['webViewX'] == undefined) {console.error('postEvent " + name + " 失败，webViewX 未初始化完毕')} else {webViewX.receiveEvent('" + name + "'," + data + ")}");
            }
        });
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

    public final void registerApi(Api... api) {
        for (Api it : api) {
            if (TextUtils.isEmpty(it.name())) {
                (new Exception("服务名称异常 " + it.getClass().getName())).printStackTrace();
            }
            this.apis.put(it.name(), it);
        }
    }

    public boolean canIUse(String apiName) {
        if (this.apis.containsKey(apiName)) {
            return true;
        }
        return WebViewXApiManager.getInstance().canIUse(apiName);
    }

    final void invoke(final ApiCaller caller, String apiName, String paramsStr, boolean isInvokeSync) {
        try {
            try {
                caller.setParams(new JSONObject(paramsStr));
            } catch (JSONException e) {
                throw new Exception("参数异常", e);
            }
            // 检查权限
            if (!this.hasCallPermission(caller)) {
                throw new Exception("不支持请求");
            }
            // 处理内部拦截器
            if (this.invokeInnerInterceptor(caller)) {
                return;
            }
            // 拦截器处理
            for (Interceptor interceptor : interceptors) {
                if (interceptor.invoke(caller)) {
                    return;
                }
            }
            // 判断是否存在API
            if (!canIUse(apiName)) {
                throw new Exception("不支持请求");
            }
            Api api = apis.get(apiName);
            if (api == null) {
                api = WebViewXApiManager.getInstance().getService(apiName);
            }
            if (api == null) {
                throw new Exception("没有找到服务");
            }
            api.handleInvoke(caller);
        } catch (Exception e) {
            caller.fail(e);
        }
    }

    /**
     * @param baseUrl      映射的url路径 https://game.com/2048
     * @param baseFilePath 支持本地文件及assets文件路径，格式如下：/data/data/com.xx/files/2048 or file:///android_asset/2048
     */
    public void addLocalResource(String baseUrl, String baseFilePath) {
        this.localResource.addLocalResource(baseUrl, baseFilePath);
    }

    public Context getContext() {
        return webView.getContext();
    }

    @Override
    public WebViewXBridge getWebViewXBridge() {
        return this;
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

    public void broadcastEvent(String name, JSONObject event) {
        EventCenter.getInstance().broadcastEvent(name, event);
    }

    public interface Interceptor {
        boolean invoke(@NonNull ApiCaller caller);

        boolean interrupt(@NonNull ApiCaller caller, @Nullable String url);
    }
}
