package com.taoweiji.webviewx.example.api;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.example.program.CopyMiniProgram;

public class NavigateToMiniProgramApi implements Api {
    @Override
    public String name() {
        return "navigateToMiniProgram";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        String baseUrl = caller.getParams().getString("baseUrl");
        String url = caller.getParams().getString("url");
        String path = caller.getParams().getString("path");
        CopyMiniProgram.start(caller.getContext(), path, baseUrl, url);
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
