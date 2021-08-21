package com.taoweiji.webviewx.apis;

import android.widget.Toast;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

public class ShowToastApi implements Api {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        runOnUiThread(() -> {
            String title = caller.getParams().optString("title");
            String icon = caller.getParams().optString("icon");
            String image = caller.getParams().optString("image");
            int duration = caller.getParams().optInt("duration", Toast.LENGTH_SHORT);
            boolean mask = caller.getParams().optBoolean("mask");
            Toast.makeText(caller.getContext(), title, duration).show();
            caller.success();
        });
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
