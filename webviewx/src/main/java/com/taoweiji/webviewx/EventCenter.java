package com.taoweiji.webviewx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventCenter {
    static EventCenter sEventCenter = new EventCenter();
    public List<RegisterDelegate> registerDelegates = new ArrayList<>();

    public static EventCenter getInstance() {
        return sEventCenter;
    }

    public void register(Register register) {
        registerDelegates.add(new RegisterDelegate(register));
    }

    public void unregister(Register register) {
        for (RegisterDelegate delegate : registerDelegates) {
            if (delegate.register == register) {
                registerDelegates.remove(delegate);
                break;
            }
        }
    }

    public RegisterDelegate getRegister(String registerId) {
        for (RegisterDelegate delegate : registerDelegates) {
            if (delegate.register.getEventRegisterId().equals(registerId)) {
                return delegate;
            }
        }
        return null;
    }

    public void broadcastEvent(String name, JSONObject event) {
        for (RegisterDelegate delegate : registerDelegates) {
            delegate.postEvent(name, event);
        }
    }

    public static class RegisterDelegate {
        private final Register register;

        public RegisterDelegate(Register register) {
            this.register = register;
        }

        public void postEvent(String name, JSONObject event) {
            register.getWebViewXBridge().postEvent(name, event);
        }

//        public void postStickyEvent(String name, JSONObject event) {
//            // 粘性应该是针对时间中心的，包括未创建的对象
//            register.getWebViewXBridge().postStickyEvent(name, event);
//        }
    }

    public interface Register {
        WebViewXBridge getWebViewXBridge();

        String getEventRegisterId();
    }
}
