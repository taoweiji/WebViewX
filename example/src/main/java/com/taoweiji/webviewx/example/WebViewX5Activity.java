package com.taoweiji.webviewx.example;

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
import com.taoweiji.webviewx.example.api.FinishApi;
import com.taoweiji.webviewx.x5.WebViewX5;
import com.tencent.smtt.sdk.WebChromeClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class WebViewX5Activity extends AppCompatActivity {
    WebViewX5 webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebViewX5(this);
        setContentView(webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addInterceptor(new WebViewXBridge.Interceptor() {
            @Override
            public boolean invoke(@NonNull ApiCaller caller) {
                switch (caller.getApiName()) {
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
        // ????????????API
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
        menu.add(1, 1, 1, "??????X5??????").setShowAsAction(SHOW_AS_ACTION_NEVER);
        menu.add(1, 2, 1, "??????H5??????").setShowAsAction(SHOW_AS_ACTION_NEVER);
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


}
