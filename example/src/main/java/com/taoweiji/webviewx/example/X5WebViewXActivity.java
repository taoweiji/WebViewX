package com.taoweiji.webviewx.example;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;
import static android.view.MenuItem.SHOW_AS_ACTION_NEVER;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.WebViewXBridge;
import com.taoweiji.webviewx.x5.X5WebViewX;
import com.tencent.smtt.sdk.WebChromeClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class X5WebViewXActivity extends AppCompatActivity {
    X5WebViewX webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new X5WebViewX(this);
        setContentView(webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addInterceptor(new WebViewXBridge.Interceptor() {
            @Override
            public boolean invoke(@NonNull ApiCaller caller) {
                switch (caller.getApiName()) {
                    case "close":
                        finish();
                        caller.success();
                        return true;
                    case "getTestData":
                        caller.successData("Hello World");
                        return true;
                    case "testError":
                        caller.fail(new Exception("testError Unknown"));
                        return true;
                }
                caller.putExtra("webView", webView);
                return false;
            }

            @Override
            public boolean interrupt(@NonNull ApiCaller caller, @Nullable String url) {
                if (url == null || url.contains("abc.com")) {
                    return false;
                }
                return false;
            }
        });
        try {
            JSONObject options = new JSONObject();
            new JSONObject().put("page", "WebViewXBridgeActivity");
            webView.setLoadOptions(options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl("file:///android_asset/test.html");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        webView.postStickyEvent("testStickyEvent", new JSONObject(Collections.singletonMap("data", "hello world")));
        // 临时注册API
        webView.registerApi(new FinishApi(this));

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "检查X5版本").setShowAsAction(SHOW_AS_ACTION_NEVER);
        menu.add(1, 2, 1, "播放H5视频").setShowAsAction(SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            webView.loadUrl("https://soft.imtt.qq.com/browser/tes/feedback.html");
        } else if (item.getItemId() == 2) {
            webView.loadUrl("https://yongling8808.github.io/test/video_demo/video_gesture.html");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public static class FinishApi extends Api {
        Activity activity;

        public FinishApi(Activity activity) {
            this.activity = activity;
        }

        @Override
        public String name() {
            return "finish";
        }

        @Override
        public boolean allowInvokeSync() {
            return true;
        }

        @Override
        protected void invoke(@NonNull ApiCaller caller) throws Exception {
            activity.finish();
            caller.success();
        }
    }
}
