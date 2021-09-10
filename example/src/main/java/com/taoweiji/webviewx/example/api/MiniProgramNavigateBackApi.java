package com.taoweiji.webviewx.example.api;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.example.program.MiniProgramActivity;

public class MiniProgramNavigateBackApi extends Api {
    @Override
    public String name() {
        return "navigateBack";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        MiniProgramActivity.WebViewAbility ability = (MiniProgramActivity.WebViewAbility) caller.getExtras().get("ability");
        if (ability.findNavController().canBack()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ability.findNavController().pop();
                    caller.success();
                }
            });
        } else {
            caller.fail(new Exception("最后一个页面，无法关闭"));
        }

    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
