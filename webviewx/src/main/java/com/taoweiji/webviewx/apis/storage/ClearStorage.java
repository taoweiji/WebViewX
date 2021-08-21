package com.taoweiji.webviewx.apis.storage;

import com.taoweiji.webviewx.ApiCaller;

public class ClearStorage extends BaseStorage {
    @Override
    public String name() {
        return "clearStorage";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        getSharedPreferences(caller).edit().clear().apply();
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
