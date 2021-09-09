package com.taoweiji.webviewx;

import android.content.Context;

public interface IWebView {
    void loadUrl(String url);

    void addJavascriptInterface(Object obj, String name);

    Context getContext();
}
