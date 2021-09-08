package com.taoweiji.webviewx.example.api;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class GetLoginInfoApi extends Api {
    @Override
    public String name() {
        return "getLoginInfo";
    }

    @Override
    public void invoke(ApiCaller caller) throws JSONException {
        int id = caller.getParams().optInt("id");
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", "Wiki");
        json.put("certificate", UUID.randomUUID().toString());
        caller.success(json);
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}