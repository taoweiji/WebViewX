package com.taoweiji.webviewx.example.api;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.example.program.MiniProgramActivity;

public class NavigateToApi extends Api {
    @Override
    public String name() {
        return "navigateTo";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        String url = caller.getParams().getString("url");
        MiniProgramActivity.WebViewAbility ability = (MiniProgramActivity.WebViewAbility) caller.getExtras().get("ability");
        Bundle bundle = new BundleBuilder().putAll(ability.getArguments()).put("url", url).build();
        if (ability.findNavController().getStackCount() >= 9) {
            caller.fail(new Exception("最多可以打开9个页面"));
            return;
        }
        runOnUiThread(() -> ability.findNavController().navigate(new MiniProgramActivity.WebViewAbility(), bundle));
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
