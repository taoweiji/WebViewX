if (window['webViewX'] == undefined) {
    var version = 1;
    if (self['webViewXBridge'] != undefined && version < webViewXBridge.getJSVersion()) {
        console.error('当前 WebViewX 的版本过低，已经加载客户端sdk最新代码');
        window.webViewX = eval(webViewXBridge.getSource());
    } else {
        window.webViewX = {
            version: version,
            callerId: 0,
            callers: {},
            registryEvents: [],
            canIUse(apiName) {
                if (self['webViewXBridge'] == undefined) {
                    console.error('当前环境无法使用 WebViewX')
                    return false;
                }
                return webViewXBridge.canIUse(apiName)
            },
            invoke(apiName, caller) {
                if (caller == undefined) {
                    caller = {}
                }
                var currentCallerId = this.callerId++;
                // 异步调用，需要caller记录下，用于回调
                this.callers[currentCallerId] = caller;
                caller["callerId"] = currentCallerId;
                if (self['webViewXBridge'] == undefined) {
                    setTimeout(function () {
                        console.error('当前环境无法使用 WebViewX')
                        webViewX.invokeCallback(currentCallerId, 'fail', '当前环境无法使用 WebViewX')
                    }, 0)
                } else {
                    webViewXBridge.invoke(currentCallerId, apiName, JSON.stringify(caller))
                }
                return caller;
            },
            invokeCallback(callerId, callbackMethod, data) {
                // invoke(apiName, caller) 的回调方法
                var caller = this.callers[callerId];
                if (caller == undefined) {
                    return
                }
                var method = caller[callbackMethod];
                if (method != undefined) {
                    if (data != undefined) {
                        method(data);
                    } else {
                        method();
                    }
                }
                if (callbackMethod == 'complete') {
                    delete this.callers[callerId];
                }
            },
            invokeSync(apiName, caller) {
                if (self['webViewXBridge'] == undefined) {
                    throw new Error('当前环境无法使用 WebViewX')
                }
                var result = webViewXBridge.invokeSync(apiName, JSON.stringify(caller))
                try {
                    if (result == null) {
                        return null;
                    }
                    return JSON.parse(result);
                } catch (e) {
                    throw new Error(apiName + ": " + result);
                }
            },
            createUUID() {
                function S4() {
                    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
                }
                return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
            },
            registerEvent(name, callback) {
                this.registryEvents.push([name, callback])
                if (self['webViewXBridge'] != undefined) {
                    var event = this.invokeSync('WebViewX.getStickyEvent', { 'name': name })
                    if (event != null) {
                        callback(event)
                    }
                }
            },
            // 注册页面的生命周期，解决页面刷新，预渲染等
            registerPageEvent(callback) {
                var eventNames = ['onLoad', 'onUnLoad', 'onShow', 'onHide'];
                for (const name of eventNames) {
                    if (callback[name] != undefined) {
                        this.registerEvent('WebViewX.' + name, function (data) {
                            if (name == 'onLoad') {
                                callback[name](data)
                            } else {
                                callback[name]()
                            }
                        })
                    }
                }
            },
            receiveEvent(name, data) {
                for (const iterator of this.registryEvents) {
                    if (iterator[0] == name) {
                        iterator[1](data)
                    }
                }
            },
            postEvent(name, data) {
                this.invokeSync('WebViewX.postEvent', {
                    'name': name,
                    'data': data
                })
            },
            postStickyEvent(name, data) {
                this.invokeSync('WebViewX.postStickyEvent', {
                    'name': name,
                    'data': data
                })
            },
            removeStickyEvent(name) {
                this.invokeSync('WebViewX.removeStickyEvent', {
                    'name': name
                })
            },
            broadcastEvent(name, data) {
                this.invokeSync('WebViewX.broadcastEvent', {
                    'name': name,
                    'data': data
                })
            },
        };
        webViewX.registerEvent('WebViewX.invokeCallback', function (event) {
            webViewX.invokeCallback(event['callerId'], event['method'], event['data'])
        })
    }
}
window.webViewX;