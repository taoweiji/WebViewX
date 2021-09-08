package com.taoweiji.webviewx;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiCaller {
    private final Map<String, Object> extras;
    private final Context context;
    private final String url;
    private final Thread syncExecuteThread;
    private final String apiName;
    private JSONObject params;
    boolean complete;

    public ApiCaller(Context context, boolean isInvokeSync, String url, String apiName) {
        this.apiName = apiName;
        this.url = url;
        this.context = context;
        this.extras = new HashMap<>();
        this.syncExecuteThread = isInvokeSync ? Thread.currentThread() : null;
    }

    public boolean isInvokeSync() {
        return syncExecuteThread != null;
    }

    void setParams(JSONObject params) {
        this.params = params;
    }

    public Map<String, Object> getExtras() {
        return this.extras;
    }

    public void putExtra(String key, Object value) {
        this.extras.put(key, value);
    }

    public void success() {
        this.success(new JSONObject());
    }

    public void successData(Object value) {
        JSONObject json = new JSONObject();
        try {
            json.put("data", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.success(json);
    }

    public void success(JSONObject data) {
        this.onSuccess(data);
        this.complete = true;
        if (syncExecuteThread != null && syncExecuteThread != Thread.currentThread()) {
            new Exception("同步请求，不允许异步回调").printStackTrace();
        }
    }

    public void fail(Throwable error) {
        this.onFail(error);
        this.complete = true;
        if (syncExecuteThread != null && syncExecuteThread != Thread.currentThread()) {
            new Exception("同步请求，不允许异步回调").printStackTrace();
        }
    }

    protected abstract void onSuccess(JSONObject object);

    protected abstract void onFail(Throwable throwable);

    public void postMessage(String name, JSONObject data) {
    }

    @Override
    protected void finalize() {
        if (!this.complete) {
            Exception error = new Exception(apiName + ": 没有正常执行 success() 或 fail()");
            error.printStackTrace();
            this.fail(error);
        }
    }

    public String getApiName() {
        return apiName;
    }

    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return this.context;
    }

    public JSONObject getParams() {
        return this.params;
    }
}