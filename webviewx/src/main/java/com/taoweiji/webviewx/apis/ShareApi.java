package com.taoweiji.webviewx.apis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import java.util.ArrayList;

public class ShareApi extends Api {

    @Override
    public String name() {
        return "share";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        String type = caller.getParams().optString("type");
        String title = caller.getParams().optString("title");
        String text = caller.getParams().optString("text");
        String image = caller.getParams().optString("image");
        if ("text".equals(type)) {
            ShareUtil.shareText(caller.getContext(), text, title);
            caller.success();
        } else if ("image".equals(type)) {
            ShareUtil.shareImage(caller.getContext(), Uri.parse(image), title);
            caller.success();
        } else {
            caller.fail(new Exception("不支持"));
        }
    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }

    static class ShareUtil {

        public static void shareText(Context context, String text, String title) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
        }

        public static void shareImage(Context context, Uri uri, String title) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, title));
        }

        public static void sendMoreImage(Context context, ArrayList<Uri> imageUris, String title) {
            Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            mulIntent.setType("image/jpeg");
            context.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));
        }
    }
}
