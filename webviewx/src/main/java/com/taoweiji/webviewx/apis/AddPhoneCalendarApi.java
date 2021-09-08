package com.taoweiji.webviewx.apis;

import com.taoweiji.webviewx.Api;
import com.taoweiji.webviewx.ApiCaller;

public class AddPhoneCalendarApi extends Api {
    @Override
    public String name() {
        return "addPhoneCalendar";
    }

    @Override
    public void invoke(ApiCaller caller) throws Exception {

    }

    @Override
    public boolean allowInvokeSync() {
        return true;
    }
}
