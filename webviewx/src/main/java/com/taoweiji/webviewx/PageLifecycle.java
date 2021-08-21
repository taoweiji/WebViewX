package com.taoweiji.webviewx;

import android.webkit.WebView;

import org.json.JSONObject;

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
        if (options == null) {
            options = new JSONObject();
        }
        this.loadOptions = options;
        dispatchOnLoad();
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
        webViewXBridge.sendEvent("PageLifecycle.onLoad", loadOptions);
    }

    private void dispatchOnUnLoad() {
        if (!loaded || notifyOnUnLoad) {
            return;
        }
        notifyOnUnLoad = true;
        webViewXBridge.sendEvent("PageLifecycle.onUnLoad", null);
    }

    private void dispatchOnShow() {
        if (!loaded || !showed || notifyOnShow) {
            return;
        }
        notifyOnShow = true;
        webViewXBridge.sendEvent("PageLifecycle.onShow", null);
    }

    private void dispatchOnHide() {
        if (!loaded || showed || notifyOnHide) {
            return;
        }
        notifyOnHide = true;
        webViewXBridge.sendEvent("PageLifecycle.onHide", null);
    }
}