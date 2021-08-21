//package com.taoweiji.webviewx.apis;
//
//import android.content.Intent;
//import android.net.Uri;
//
//import com.taoweiji.webviewx.Api;
//import com.taoweiji.webviewx.ApiCaller;
//
//public class MakePhoneCallApi implements Api {
//    @Override
//    public String name() {
//        return "makePhoneCall";
//    }
//
//    @Override
//    public void invoke(ApiCaller caller) throws Exception {
//        String phoneNumber = caller.getParams().optString("phoneNumber");
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + phoneNumber));
//        runOnUiThread(() -> {
//            caller.getContext().startActivity(intent);
//            caller.success();
//        });
//    }
//}
