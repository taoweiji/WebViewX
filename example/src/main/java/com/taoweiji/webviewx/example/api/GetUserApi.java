package com.taoweiji.webviewx.example.api;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONObject;

public class GetUserApi extends Api {
    @Override
    public String name() {
        return "getUser";
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        int id = caller.getParams().optInt("id");
        if (id == 0){
            caller.fail(new Exception("请求参数异常"));
        }else {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("name", "Wiki");
            caller.success(json);
        }
    }
}
