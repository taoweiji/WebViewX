//package com.taoweiji.webviewx;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class WebViewXEventCenter {
//    private static final WebViewXEventCenter instance = new WebViewXEventCenter();
//
//    private final Map<String, List<String>> registry = new HashMap<>();
//
//    public static WebViewXEventCenter getInstance() {
//        return instance;
//    }
//
//    public void registerEvent(String acceptChannelId, String name, String registryId, Callback callback) {
//
//    }
//
//    public void unRegisterEvent(String callbackId) {
//
//    }
//
//    public void unRegisterEventByChannel(String channelId) {
//
//    }
//
//    public static interface Callback {
//        void onEvent(String channelId, String name, JSONObject data, String registryId);
//    }
//}
