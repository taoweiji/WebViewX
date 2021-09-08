package com.taoweiji.webviewx.apis.storage;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.ApiCaller;

public class GetStorage extends BaseStorage {
    @Override
    public String name() {
        return "getStorage";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        String key = caller.getParams().optString("key");
        String value = getSharedPreferences(caller).getString(key, "");
        caller.successData(value);
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
