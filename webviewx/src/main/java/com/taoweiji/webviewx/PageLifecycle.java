package com.taoweiji.webviewx;

import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 参考微信小程序，控制页面的生命周期
 */
class PageLifecycle {
    private final WebViewXBridge webViewXBridge;
    private JSONObject loadOptions = new JSONObject();
    private boolean loaded;
    private boolean showed;
    private boolean notifyOnShow = false;
    private boolean notifyOnLoad;
    private boolean notifyOnHide;
    private boolean notifyOnUnLoad;

    public PageLifecycle(WebViewXBridge webViewXBridge) {
        this.webViewXBridge = webViewXBridge;
    }

    void setLoadOptions(JSONObject options) {
        if (this.loadOptions == null) {
            this.loadOptions = new JSONObject();
        }
        Iterator<String> it = options.keys();
        try {
            while (it.hasNext()) {
                String key = it.next();
                this.loadOptions.put(key, options.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLoadOption(String key, Object value) {
        if (this.loadOptions == null) {
            this.loadOptions = new JSONObject();
        }
        try {
            this.loadOptions.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void onShow() {
        showed = true;
        notifyOnHide = false;
        dispatchOnShow();
    }


    void onHide() {
        showed = false;
        notifyOnShow = false;
        dispatchOnHide();
    }

    void onUnload() {
        dispatchOnUnLoad();
    }

    void onPageStarted(String url) {
        loaded = false;
        // 重置通知状态
        notifyOnHide = false;
        notifyOnLoad = false;
        notifyOnShow = false;
        notifyOnUnLoad = false;
        // TODO dispatchOnLoad();
    }

    void onPageFinished(String url) {
        loaded = true;
        dispatchOnLoad();
        dispatchOnShow();
        dispatchOnHide();
    }

    private void dispatchOnLoad() {
        if (!loaded || notifyOnLoad) {
            return;
        }
        notifyOnLoad = true;
        webViewXBridge.postStickyEvent("PageLifecycle.onLoad", loadOptions);
    }

    private void dispatchOnUnLoad() {
        if (!loaded || notifyOnUnLoad) {
            return;
        }
        notifyOnUnLoad = true;
        webViewXBridge.postEvent("PageLifecycle.onUnLoad", null);
    }

    private void dispatchOnShow() {
        if (!loaded || !showed || notifyOnShow) {
            return;
        }
        notifyOnShow = true;
        webViewXBridge.postEvent("PageLifecycle.onShow", null);
    }

    private void dispatchOnHide() {
        if (!loaded || showed || notifyOnHide) {
            return;
        }
        notifyOnHide = true;
        webViewXBridge.postEvent("PageLifecycle.onHide", null);
    }

    public JSONObject getLoadOptions() {
        return loadOptions;
    }
}