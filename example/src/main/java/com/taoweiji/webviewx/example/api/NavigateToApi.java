package com.taoweiji.webviewx.example.api;

import android.os.Bundle;

import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.example.program.CopyMiniProgram;

public class NavigateToApi implements Api {
    @Override
    public String name() {
        return "navigateTo";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        String url = caller.getParams().getString("url");
        CopyMiniProgram.WebViewAbility ability = (CopyMiniProgram.WebViewAbility) caller.getExtras().get("ability");
        Bundle bundle = new BundleBuilder().putAll(ability.getArguments()).put("url", url).build();
        runOnUiThread(() -> ability.findNavController().navigate(new CopyMiniProgram.WebViewAbility(), bundle));
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
