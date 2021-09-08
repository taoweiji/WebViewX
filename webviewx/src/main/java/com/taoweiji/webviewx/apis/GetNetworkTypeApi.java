package com.taoweiji.webviewx.apis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONException;
import org.json.JSONObject;

public final class GetNetworkTypeApi extends Api {
    public String name() {
        return "getNetworkType";
    }

    public void invoke(ApiCaller caller) {
        ConnectivityManager manager = (ConnectivityManager) caller.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        JSONObject object = new JSONObject();
        try {
            object.put("networkType", networkInfo.getTypeName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        caller.success(object);
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}