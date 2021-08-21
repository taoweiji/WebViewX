package com.taoweiji.webviewx;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public interface Api {
    String name();

    boolean allowInvokeSync();

    void invoke(@NonNull ApiCaller caller) throws Exception;

    default void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }


}