package com.taoweiji.webviewx.apis.storage;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.ApiCaller;

public class SetStorage extends BaseStorage {
    @Override
    public String name() {
        return "setStorage";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        String key = caller.getParams().optString("key");
        String value = caller.getParams().optString("value");
        getSharedPreferences(caller).edit().clear().putString(key, value).apply();
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
