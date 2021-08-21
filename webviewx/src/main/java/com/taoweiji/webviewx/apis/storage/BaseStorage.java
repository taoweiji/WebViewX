package com.taoweiji.webviewx.apis.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

abstract class BaseStorage implements Api {

    SharedPreferences getSharedPreferences(ApiCaller caller) {
        return caller.getContext().getSharedPreferences("webViewX", Context.MODE_PRIVATE);
    }
}
