package com.taoweiji.webviewx.x5;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.taoweiji.webviewx.WebViewXBridge;
import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

class WebViewXClientWrapper extends WebViewClient {
    WebViewClient webViewClient;
    private final WebViewXBridge webViewXBridge;

    public WebViewXClientWrapper(WebViewXBridge webViewXBridge, WebViewClient webViewClient) {
        this.webViewXBridge = webViewXBridge;
        this.webViewClient = webViewClient;
    }

    @Override
    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return webViewClient.shouldOverrideUrlLoading(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return webViewClient.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        webViewClient.onPageStarted(view, url, favicon);
        if (webViewXBridge != null) {
            webViewXBridge.onPageStarted(url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        webViewClient.onPageFinished(view, url);
        if (webViewXBridge != null) {
            webViewXBridge.onPageFinished(url);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        webViewClient.onLoadResource(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        webViewClient.onPageCommitVisible(view, url);
    }

    @Nullable
    @Override
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = webViewClient.shouldInterceptRequest(view, url);
        if (response != null) {
            return response;
        }
        android.webkit.WebResourceResponse res = webViewXBridge.localResource.shouldInterceptRequest(view.getContext(), url);
        return convert(res);
    }

    private WebResourceResponse convert(android.webkit.WebResourceResponse res) {
        if (res == null) {
            return null;
        }
        return new WebResourceResponse(res.getMimeType(), res.getEncoding(), res.getData());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse response = webViewClient.shouldInterceptRequest(view, request);
        if (response != null) {
            return response;
        }
        if (request.getUrl() == null) {
            return null;
        }
        android.webkit.WebResourceResponse res = webViewXBridge.localResource.shouldInterceptRequest(view.getContext(), request.getUrl().toString());
        return convert(res);
    }

    @Override
    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        webViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    @Deprecated
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        webViewClient.onReceivedError(view, errorCode, description, failingUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        webViewClient.onReceivedError(view, request, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        webViewClient.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        webViewClient.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        webViewClient.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        webViewClient.onReceivedSslError(view, handler, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        webViewClient.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        webViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return webViewClient.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        webViewClient.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        webViewClient.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        webViewClient.onReceivedLoginRequest(view, realm, account, args);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return webViewClient.onRenderProcessGone(view, detail);
    }
}
