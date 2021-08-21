package com.taoweiji.webviewx.apis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONException;
import org.json.JSONObject;

public class GetLocationApi implements Api {
    private static final long MAX_EXPIRE_TIME = 5 * 60 * 1000;

    @Override
    public String name() {
        return "getLocation";
    }

    @SuppressLint("MissingPermission")
    @Override
    public void invoke(ApiCaller caller) throws Exception {
// 默认 wgs84，wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标
        String type = caller.getParams().optString("type", "wgs84");
        // 传入 true 会返回高度信息，由于获取高度需要较高精确度，会减慢接口返回速度
        boolean altitude = caller.getParams().optBoolean("altitude", false);
        boolean isHighAccuracy = caller.getParams().optBoolean("isHighAccuracy");
//        long highAccuracyExpireTime = caller.getParams().optLong("highAccuracyExpireTime", Long.MAX_VALUE);

        LocationManager locationManager = (LocationManager) caller.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (isHighAccuracy) {
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                JSONObject json = new JSONObject();
                try {
                    if ("gcj02".equals(type)) {
                        OpenLocationApi.JZLocationConverter.LatLng result = OpenLocationApi.JZLocationConverter.gcj02ToWgs84(
                                new OpenLocationApi.JZLocationConverter.LatLng(
                                        location.getLatitude(),
                                        location.getLongitude()
                                )
                        );

                        json.put("latitude", result.latitude);
                        json.put("longitude", result.longitude);
                    } else {
                        json.put("latitude", location.getLatitude());
                        json.put("longitude", location.getLongitude());

                    }
                    json.put("speed", location.getSpeed());
                    json.put("accuracy", location.getAccuracy());
                    json.put("altitude", location.getAltitude());
                    caller.success(json);
                } catch (JSONException e) {
                    caller.fail(e);
                }
                locationManager.removeUpdates(this);
            }
        };
        Runnable timeout = () -> {
            locationManager.removeUpdates(locationListener);
            caller.fail(new Exception("超时"));
        };
        locationManager.requestLocationUpdates(1000, 1000, criteria, locationListener, Looper.getMainLooper());
        new Handler(Looper.getMainLooper()).postDelayed(timeout, MAX_EXPIRE_TIME);
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
