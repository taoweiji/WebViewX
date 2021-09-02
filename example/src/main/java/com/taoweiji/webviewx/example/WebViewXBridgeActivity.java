package com.taoweiji.webviewx.example;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.WebViewX;
import com.taoweiji.webviewx.WebViewXBridge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class WebViewXBridgeActivity extends AppCompatActivity {
    WebViewX webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebViewX(this);
        setContentView(webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addInterceptor(new WebViewXBridge.Interceptor() {
            @Override
            public boolean invoke(ApiCaller caller) {
                switch (caller.getApiName()) {
                    case "close":
                        finish();
                        caller.success();
                        return true;
                    case "getTestData":
                        caller.successData("Hello World");
                        return true;
                    case "testError":
                        caller.fail(new Exception("Unknown"));
                        return true;
                }
                return false;
            }

            @Override
            public boolean interrupt(@Nullable String url, @NonNull String apiName) {
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
//        webView.addLocalResource("https://2048.com", "file:///android_asset/2048");
//        webView.loadUrl("https://2048.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "发送事件").setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            webView.postEvent("event", new JSONObject(Collections.singletonMap("data", "hello world")));
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
}
