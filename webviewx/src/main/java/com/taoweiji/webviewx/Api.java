package com.taoweiji.webviewx;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public abstract class Api {
    public abstract String name();

    public abstract boolean allowInvokeSync();

    public final void handleInvoke(@NonNull ApiCaller caller) {
        if (caller.isInvokeSync() && !this.allowInvokeSync()) {
            caller.fail(new Exception("该接口不允许同步调用，请检查 allowInvokeSync 方法"));
        } else {
            try {
                invoke(caller);
            } catch (Exception e) {
                caller.fail(e);
            }
        }
    }

    protected abstract void invoke(@NonNull ApiCaller caller) throws Exception;

    protected void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }


}