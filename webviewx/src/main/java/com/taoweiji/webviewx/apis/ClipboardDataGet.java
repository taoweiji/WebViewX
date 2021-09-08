package com.taoweiji.webviewx.apis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

public class ClipboardDataGet extends Api {
    @Override
    public String name() {
        return "getClipboardData";
    }

    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        ClipboardManager cm = (ClipboardManager) caller.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cm.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0) {
            caller.successData("");
        } else {
            ClipData.Item item = clip.getItemAt(0);
            if (item == null || item.getText() == null) {
                caller.successData("");
            } else {
                caller.successData(item.getText());
            }
        }
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
