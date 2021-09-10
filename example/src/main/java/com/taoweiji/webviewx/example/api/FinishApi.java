package com.taoweiji.webviewx.example.api;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

public class FinishApi extends Api {
    Activity activity;

    public FinishApi(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String name() {
        return "finish";
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }

    @Override
    protected void invoke(@NonNull ApiCaller caller) throws Exception {
        activity.finish();
        caller.success();
    }
}