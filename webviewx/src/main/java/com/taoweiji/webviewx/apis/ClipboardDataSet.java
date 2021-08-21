package com.taoweiji.webviewx.apis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

public class ClipboardDataSet implements Api {
    @Override
    public String name() {
        return "setClipboardData";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        String data = caller.getParams().getString("data");
        ClipboardManager cm = (ClipboardManager) caller.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", data);
        cm.setPrimaryClip(mClipData);
        caller.success();
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
