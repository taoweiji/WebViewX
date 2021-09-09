package com.taoweiji.webviewx;

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

    @JavascriptInterface
    public final boolean canIUse(String apiName) {
        return webViewXBridge.canIUse(apiName);
    }

    @JavascriptInterface
    public final int getJSVersion() {
        return 1;
    }

    @JavascriptInterface
    public final String getSource() {
        try {
            InputStream is = webViewXBridge.getContext().getAssets().open("webviewx/webviewx.js");
            if (is != null) {
                return FileUtils.read(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @JavascriptInterface
    public final void invoke(final String callerId, String apiName, final String paramsStr) {
        ApiCaller caller = new ApiCaller(webViewXBridge.getContext(), false, webViewXBridge.currentUrl, apiName) {
            public void onSuccess(final JSONObject data) {
                callback("success", data);
                callback("complete", null);
            }

            private void callback(String method, Object data) {
                try {
                    JSONObject success = new JSONObject();
                    success.put("callerId", callerId);
                    success.put("method", method);
                    success.put("data", data);
                    webViewXBridge.postEvent(WebViewXBridge.EVENT_INVOKE_CALLBACK, success);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFail(final Throwable error) {
                String msg = error.getMessage();
                if (msg == null || msg.equals("")) {
                    msg = "UNKNOWN ERROR";
                }
                msg = msg.replace("\"", "\\\"").replace("'", "\\'").replace("\n", "\\n");
                callback("fail", msg);
                callback("complete", null);
            }

            public void postMessage(final String name, final JSONObject data) {
                callback(name, data);
            }
        };
        new Thread(() -> webViewXBridge.invoke(caller, apiName, paramsStr, false)).start();
    }

    @JavascriptInterface
    public final String invokeSync(String apiName, final String paramsStr) {
        final String[] result = new String[1];
        ApiCaller caller = new ApiCaller(webViewXBridge.getContext(), true, webViewXBridge.currentUrl, apiName) {
            public void onSuccess(JSONObject data) {
                if (data != null) {
                    result[0] = data.toString();
                }
            }

            public void onFail(Throwable error) {
                result[0] = error.getMessage();
            }
        };
        webViewXBridge.invoke(caller, apiName, paramsStr, true);
        if (!caller.complete) {
            return "ERROR:API没有正常工作";
        }
        return result[0];
    }
}