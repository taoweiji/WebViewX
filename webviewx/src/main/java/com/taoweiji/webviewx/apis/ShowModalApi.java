package com.taoweiji.webviewx.apis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowModalApi extends Api {
    @Override
    public String name() {
        return "showModal";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {
        runOnUiThread(() -> {
            String title = caller.getParams().optString("title", "");
            String content = caller.getParams().optString("content", "");
            boolean showCancel = caller.getParams().optBoolean("showCancel", true);
            String cancelText = caller.getParams().optString("cancelText", "取消");
            String cancelColor = caller.getParams().optString("cancelColor", "");
            String confirmText = caller.getParams().optString("confirmText", "确定");
            String confirmColor = caller.getParams().optString("confirmColor", "");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(caller.getContext()).setOnCancelListener(dialog -> {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cancel", true);
                    jsonObject.put("confirm", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                caller.success(jsonObject);
            });
            if (!TextUtils.isEmpty(title)) {
                dialogBuilder.setTitle(title);
            }
            if (!TextUtils.isEmpty(content)) {
                dialogBuilder.setMessage(content);
            }
            if (showCancel) {
                dialogBuilder.setNegativeButton(cancelText, (dialog, which) -> dialog.cancel());
            }
            dialogBuilder.setPositiveButton(confirmText, (dialog, which) -> {
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cancel", false);
                    jsonObject.put("confirm", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                caller.success(jsonObject);
            });
            AlertDialog dialog = dialogBuilder.show();
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveButton != null && !TextUtils.isEmpty(confirmColor)) {
                positiveButton.setTextColor(Color.parseColor(confirmColor));
            }
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeButton != null && !TextUtils.isEmpty(cancelColor)) {
                negativeButton.setTextColor(Color.parseColor(cancelColor));
            }
        });
    }

    @Override
    public boolean allowInvokeSync() {
        return false;
    }
}
