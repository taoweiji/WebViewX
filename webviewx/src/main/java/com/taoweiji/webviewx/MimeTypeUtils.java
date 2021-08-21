package com.taoweiji.webviewx;

import java.util.HashMap;
import java.util.Map;

class MimeTypeUtils {
    static Map<String, String> mimes = new HashMap<>();

    static {
        mimes.put("js", "application/javascript");
        mimes.put("pdf", "application/pdf");
        mimes.put("css", "text/css");
        mimes.put("gif", "image/gif");
        mimes.put("png", "image/png");
        mimes.put("jpg", "image/jpeg");
        mimes.put("jpeg", "image/jpeg");
        mimes.put("webp", "image/webp");
        mimes.put("mp3", "audio/mp3");
        mimes.put("html", "text/html");

        mimes.put("XX", "XX");
        mimes.put("XX", "XX");
        mimes.put("XX", "XX");
        mimes.put("XX", "XX");
        mimes.put("XX", "XX");
        mimes.put("XX", "XX");
    }

    public static String getMimeType(String fileSuffix) {
        return mimes.get(fileSuffix);
    }

}
