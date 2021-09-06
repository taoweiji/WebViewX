package com.taoweiji.webviewx;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

class WebViewXBridgeInterface {
    WebViewXBridge webViewXBridge;

    public WebViewXBridgeInterface(WebViewXBridge webViewXBridge) {
        this.webViewXBridge = webViewXBridge;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @JavascriptInterface
    public final boolean canIUse(String apiName) {
        if (!WebViewXApiManager.getInstance().canIUse(apiName)) {
            return false;
        }
        return webViewXBridge.hasCallPermission(webViewXBridge.currentUrl, apiName);
    }

    private WebView getWebView() {
        return webViewXBridge.webView;
    }

    @JavascriptInterface
    public final int getJSVersion() {
        return 1;
    }

    @JavascriptInterface
    public final String getSource() {
        try {
            InputStream is = webViewXBridge.webView.getContext().getAssets().open("webviewx/webviewx.js");
            if (is != null) {
                return FileUtils.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JavascriptInterface
    public final void invoke(final String callerId, String apiName, final String paramsStr) {
        ApiCaller caller = new ApiCaller(getWebView().getContext(), false, webViewXBridge.currentUrl, apiName) {
            public void onSuccess(final JSONObject data) {
                runOnUiThread(() -> {
                    try {
                        if (getWebView() == null) {
                            return;
                        }
                        getWebView().loadUrl("javascript:webViewX.invokeCallback('" + callerId + "','success'," + data + ')');
                        getWebView().loadUrl("javascript:webViewX.invokeCallback('" + callerId + "','complete')");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            public void onFail(final Throwable error) {
                runOnUiThread(() -> {
                    try {
                        if (getWebView() == null) {
                            return;
                        }
                        String msg = error.getMessage();
                        if (msg == null || msg.equals("")) {
                            msg = "UNKNOWN ERROR";
                        }
                        msg = msg.replace("\"", "\\\"").replace("'", "\\'").replace("\n", "\\n");
                        getWebView().loadUrl("javascript:webViewX.invokeCallback('" + callerId + "','fail','" + msg + "')");
                        getWebView().loadUrl("javascript:webViewX.invokeCallback('" + callerId + "','complete')");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            public void postMessage(final String name, final JSONObject data) {
                runOnUiThread(() -> {
                    try {
                        if (getWebView() == null) {
                            return;
                        }
                        getWebView().loadUrl("javascript:webViewX.invokeCallback('" + callerId + "','" + name + "'," + data + ')');
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        new Thread(() -> {
            try {
                try {
                    caller.setParams(new JSONObject(paramsStr));
                } catch (JSONException e) {
                    caller.onFail(new Exception("参数异常", e));
                    return;
                }
                // 检查权限
                if (!webViewXBridge.hasCallPermission(webViewXBridge.currentUrl, apiName)) {
                    caller.fail(new Exception("不支持请求"));
                    return;
                }
                // 处理拦截器
                if (webViewXBridge.invoke(caller)) {
                    return;
                }
                // 判断是否存在API
                if (!WebViewXApiManager.getInstance().canIUse(apiName)) {
                    caller.fail(new Exception("不支持请求"));
                    return;
                }
                try {
                    WebViewXApiManager.getInstance().invoke(apiName, caller, false);
                } catch (Exception e) {
                    caller.fail(e);
                }
            } catch (Exception e) {
                caller.onFail(e);
            }
        }).start();
    }

    @JavascriptInterface
    public final String invokeSync(String apiName, final String paramsStr) {
        Log.e("invokeSync", apiName + ":" + paramsStr);
        final Object[] result = new Object[1];
        result[0] = "{}";
        ApiCaller caller = new ApiCaller(getWebView().getContext(), true, webViewXBridge.currentUrl, apiName) {
            public void onSuccess(JSONObject data) {
                result[0] = data;
            }

            public void onFail(Throwable error) {
                result[0] = error.getMessage();
            }
        };
        try {
            caller.setParams(new JSONObject(paramsStr));
        } catch (JSONException e) {
            caller.onFail(new Exception("参数异常", e));
            return result[0].toString();
        }
        // 检查权限
        if (!webViewXBridge.hasCallPermission(webViewXBridge.currentUrl, apiName)) {
            caller.fail(new Exception("不支持请求"));
            return result[0].toString();
        }
        // 处理拦截器
        if (webViewXBridge.invoke(caller)) {
            if (result[0] == null) {
                return null;
            }
            return result[0].toString();
        }
        // 判断是否存在API
        if (!WebViewXApiManager.getInstance().canIUse(apiName)) {
            caller.fail(new Exception("不支持请求"));
            return result[0].toString();
        }
        try {
            WebViewXApiManager.getInstance().invoke(apiName, caller, true);
        } catch (Exception e) {
            caller.fail(e);
        }
        return result[0].toString();
    }

    private void runOnUiThread(Runnable runnable) {
        (new Handler(Looper.getMainLooper())).post(runnable);
    }
}