package com.taoweiji.webviewx.example;

import android.app.Application;

import com.taoweiji.webviewx.WebViewXApiManager;
import com.taoweiji.webviewx.example.api.GetUserApi;
import com.taoweiji.webviewx.example.api.MiniProgramNavigateBackApi;
import com.taoweiji.webviewx.example.api.NavigateToApi;
import com.taoweiji.webviewx.example.api.NavigateToMiniProgramApi;
import com.taoweiji.webviewx.example.api.GetLoginInfoApi;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WebViewXApiManager.getInstance().register(new NavigateToMiniProgramApi());
        WebViewXApiManager.getInstance().register(new NavigateToApi());
        WebViewXApiManager.getInstance().register(new GetLoginInfoApi());
        WebViewXApiManager.getInstance().register(new GetUserApi());
        WebViewXApiManager.getInstance().register(new MiniProgramNavigateBackApi());
    }
}
