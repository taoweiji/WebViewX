package com.taoweiji.webviewx.apis.storage;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.ApiCaller;

public class RemoveStorage extends BaseStorage{
    @Override
    public String name() {
        return "removeStorage";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        String key = caller.getParams().optString("key");
        getSharedPreferences(caller).edit().clear().remove(key).apply();
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
