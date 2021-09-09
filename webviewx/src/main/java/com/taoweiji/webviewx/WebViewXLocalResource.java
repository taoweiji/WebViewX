package com.taoweiji.webviewx;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WebViewXLocalResource {
    private final Map<String, String> mapper = new HashMap<>();

    public void addLocalResource(String baseUrl, String filePath) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        mapper.put(baseUrl, filePath);
    }

    protected String getMapperFilePath(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        for (String baseUrl : mapper.keySet()) {
            if (url.startsWith(baseUrl)) {
                return mapper.get(baseUrl) + url.replace(baseUrl, "");
            }
        }
        return null;
    }

    protected InputStream getInputStream(Context context, String path) {
        try {
            if (path.startsWith("file:///android_asset/")) {
                path = path.replace("file:///android_asset/", "");
                return context.getResources().getAssets().open(path);
            }
            return new FileInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getMimeType(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        String mime = url.substring(url.lastIndexOf(".") + 1);
        return MimeTypeUtils.getMimeType(mime);
    }

    public WebResourceResponse shouldInterceptRequest(Context context, String url) {
        String basePath = getMapperFilePath(url);
        if (url.endsWith("webviewx/webviewx.js")) {
            return new WebResourceResponse(getMimeType(url), "utf-8", getInputStream(context, "file:///android_asset/webviewx/webviewx.js"));
        }
        if (basePath != null) {
            return new WebResourceResponse(getMimeType(url), "utf-8", getInputStream(context, basePath));
        }
        return null;
    }
}
