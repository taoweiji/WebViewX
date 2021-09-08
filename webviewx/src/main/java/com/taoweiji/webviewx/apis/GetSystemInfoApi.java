package com.taoweiji.webviewx.apis;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Build;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONObject;

import java.util.Locale;

public class GetSystemInfoApi extends Api {
    @Override
    public String name() {
        return "getSystemInfo";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        JSONObject json = new JSONObject();
        json.put("brand", Build.BRAND);
        json.put("model", Build.MODEL);
        json.put("pixelRatio", ViewUtils.pixelRatio(caller.getContext()));
        json.put("screenWidth", ViewUtils.getScreenWidth(caller.getContext()));
        json.put("screenHeight", ViewUtils.getScreenHeight(caller.getContext()));
        json.put("windowWidth", ViewUtils.getScreenWidth(caller.getContext()));
        json.put("windowHeight", ViewUtils.getScreenHeight(caller.getContext()));
        json.put("statusBarHeight", ViewUtils.getStatusBarHeight(caller.getContext()));
        json.put("language", Locale.getDefault());
        PackageInfo packageInfo = caller.getContext().getPackageManager().getPackageInfo(caller.getContext().getPackageName(), 0);
        json.put("version", packageInfo.versionName);
        json.put("system", "android " + Build.VERSION.SDK_INT);
        json.put("platform", "android");
//        json.put("fontSizeSetting", Build.BRAND)
//        json.put("SDKVersion", Build.BRAND)
//        json.put("benchmarkLevel", Build.BRAND)
//        json.put("albumAuthorized", Build.BRAND)
//        json.put("cameraAuthorized", Build.BRAND)
//        json.put("locationAuthorized", Build.BRAND)
//        json.put("microphoneAuthorized", Build.BRAND)
//        json.put("notificationAuthorized", Build.BRAND)
//        json.put("notificationAlertAuthorized", Build.BRAND)
//        json.put("notificationBadgeAuthorized", Build.BRAND)
//        json.put("notificationSoundAuthorized", Build.BRAND)
//        json.put("bluetoothEnabled", Build.BRAND)
//        json.put("locationEnabled", Build.BRAND)
//        json.put("wifiEnabled", Build.BRAND)
//        json.put("safeArea", Build.BRAND)
//        json.put("locationReducedAccuracy", Build.BRAND)
//        json.put("theme", Build.BRAND)
//        json.put("host", Build.BRAND)
//        json.put("enableDebug", Build.BRAND)
        if (caller.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            json.put("deviceOrientation", "landscape");
        } else {
            json.put("deviceOrientation", "portrait");
        }
        caller.success(json);
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }

    static class ViewUtils {

        public static int dp2px(Context context, Number dp) {
            return (int) (context.getResources().getDisplayMetrics().density * dp.floatValue());
        }

        public static int getStatusBarHeight(Context context) {
            int result = 0;
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = context.getResources().getDimensionPixelOffset(resId);
            }
            return result;
        }

        public static int getScreenWidth(Context context) {
            return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        }

        public static int getScreenHeight(Context context) {
            return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        }

        public static float pixelRatio(Context context) {
            return context.getResources().getDisplayMetrics().density;
        }
    }

}
