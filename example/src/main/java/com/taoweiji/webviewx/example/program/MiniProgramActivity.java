package com.taoweiji.webviewx.example.program;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.NavOptions;
import com.taoweiji.navigation.StatusBarHelper;
import com.taoweiji.navigation.ViewUtils;
import com.taoweiji.webviewx.ApiCaller;
import com.taoweiji.webviewx.WebViewX;
import com.taoweiji.webviewx.WebViewXBridge;
import com.taoweiji.webviewx.example.R;

public class MiniProgramActivity extends AppCompatActivity {

    public static void start(Context context, String path, String baseUrl, String url) {
        Intent intent = new Intent(context, MiniProgramActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("baseUrl", baseUrl);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarHelper.openImmerseStyle(this);
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        setContentView(frameLayout);

        FrameLayout layout = new FrameLayout(this);
        frameLayout.addView(layout);
        navController = createNavControllerBuilder().create(layout);

        OptionButton button = new OptionButton(this);
        FrameLayout.LayoutParams buttonLP = new FrameLayout.LayoutParams(ViewUtils.dp2px(this, 88), ViewUtils.dp2px(this, 32));
        buttonLP.gravity = Gravity.RIGHT;
        buttonLP.topMargin = ViewUtils.getStatusBarHeight(this) + ViewUtils.dp2px(this, 10);
        buttonLP.rightMargin = ViewUtils.dp2px(this, 10);
        frameLayout.addView(button, buttonLP);
    }

    @Override
    public void onBackPressed() {
        if (navController.canBack()) {
            navController.dispatcherOnBackPressed();
            return;
        }
        super.onBackPressed();
    }


    public NavController.Builder createNavControllerBuilder() {
        Bundle bundle = new BundleBuilder().putAll(getIntent().getExtras()).build();
        return new NavController.Builder().defaultNavOptions(NavOptions.LEFT_RIGHT).defaultDestination(Destination.with(new WebViewAbility(), bundle));
    }


    static class OptionButton extends LinearLayout {

        public OptionButton(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
            ImageView option = new ImageView(getContext());
            option.setImageResource(R.drawable.wax_ic_option);
            int dp8 = ViewUtils.dp2px(getContext(), 8);
            option.setPadding(dp8, dp8, dp8, dp8);

            View driver = new View(context);
            driver.setBackgroundColor(Color.parseColor("#DDDDDD"));

            ImageView close = new ImageView(getContext());
            close.setImageResource(R.drawable.wax_ic_close);
            close.setPadding(dp8, dp8, dp8, dp8);
            close.setOnClickListener(v -> ((Activity) getContext()).finish());

            addView(option, ViewUtils.dp2px(getContext(), 44), ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(driver, ViewUtils.dp2px(getContext(), 1f), ViewUtils.dp2px(getContext(), 20f));
            addView(close);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#88FFFFFF"));
            drawable.setStroke(ViewUtils.dp2px(getContext(), 1f), Color.parseColor("#DDDDDD"));
            drawable.setCornerRadius(ViewUtils.dp2px(getContext(), 24f));
            setBackground(drawable);
        }

    }

    public static class WebViewAbility extends Ability {
        private WebViewX webView;

        @Override
        protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setTitle("???????????????");
            getToolbar().setElevation(0);
            String path = getArguments().getString("path");
            String baseUrl = getArguments().getString("baseUrl");
            String url = getArguments().getString("url");
            this.webView = new WebViewX(getContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.addInterceptor(new WebViewXBridge.Interceptor() {
                @Override
                public boolean invoke(@NonNull ApiCaller caller) {
                    caller.getExtras().put("ability", WebViewAbility.this);
                    switch (caller.getApiName()) {
                        case "getData":
                            caller.successData("Hello");
                            return true;
                    }
                    return false;
                }

                @Override
                public boolean interrupt(@NonNull ApiCaller caller, @Nullable String url) {
                    return false;
                }
            });
            webView.addLocalResource(baseUrl, path);
            webView.loadUrl(url);
            return webView;
        }

        @Override
        protected void onPause() {
            super.onPause();
            webView.onPause();
        }

        @Override
        protected void onResume() {
            super.onResume();
            webView.onResume();
        }

        @Override
        protected void onDestroy() {
            webView.destroy();
            super.onDestroy();
        }
    }
}
