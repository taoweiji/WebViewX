package com.taoweiji.webviewx;

import android.text.TextUtils;

import com.taoweiji.webviewx.apis.ClipboardDataGet;
import com.taoweiji.webviewx.apis.ClipboardDataSet;
import com.taoweiji.webviewx.apis.GetLocationApi;
import com.taoweiji.webviewx.apis.GetNetworkTypeApi;
import com.taoweiji.webviewx.apis.GetSystemInfoApi;
import com.taoweiji.webviewx.apis.OpenLocationApi;
import com.taoweiji.webviewx.apis.ShareApi;
import com.taoweiji.webviewx.apis.ShowModalApi;
import com.taoweiji.webviewx.apis.ShowToastApi;
import com.taoweiji.webviewx.apis.storage.ClearStorage;
import com.taoweiji.webviewx.apis.storage.GetStorage;
import com.taoweiji.webviewx.apis.storage.RemoveStorage;
import com.taoweiji.webviewx.apis.storage.SetStorage;

import java.util.HashMap;
import java.util.Map;

public final class WebViewXApiManager {
    private static final WebViewXApiManager instance = new WebViewXApiManager();
    private final Map<String, Api> apis = new HashMap<>();

    WebViewXApiManager() {
        // 内置的接口
//        register(new MakePhoneCallApi());
        register(new GetSystemInfoApi());
        register(new GetNetworkTypeApi());
        register(new ShareApi());
        register(new ClipboardDataGet());
        register(new ClipboardDataSet());
        register(new ShowToastApi());
        register(new ShowModalApi());
        register(new OpenLocationApi());
        register(new GetLocationApi());

        register(new ClearStorage());
        register(new GetStorage());
        register(new RemoveStorage());
        register(new SetStorage());
    }

    public final void register(Api... api) {
        for (Api it : api) {
            if (TextUtils.isEmpty(it.name())) {
                (new Exception("服务名称异常 " + it.getClass().getName())).printStackTrace();
            }
            this.apis.put(it.name(), it);
        }
    }

    public final void register(String name, Api api) {
        this.apis.put(name, api);
    }

    public final void unregister(String name) {
        this.apis.remove(name);
    }


    public final Api getService(String name) {
        return (Api) this.apis.get(name);
    }

    public final void invoke(String apiName, final ApiCaller caller) {
        final Api api = instance.getService(apiName);
        if (api == null) {
            caller.fail(new Exception("没有找到服务"));
        } else {
            if (caller.isInvokeSync()) {
                api.handleInvoke(caller);
            } else {
                new Thread(() -> api.handleInvoke(caller)).start();
            }
        }
    }

    public static WebViewXApiManager getInstance() {
        return instance;
    }

    public boolean canIUse(String apiName) {
        return apis.containsKey(apiName);
    }
}
