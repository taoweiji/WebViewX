package com.taoweiji.webviewx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

class WebViewUtils {
    public static void commonConfig(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    public static void openBrowser(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
    }
}
