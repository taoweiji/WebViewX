# Android WebViewX 增强框架，提供同步/异步API、网页离线运行、页面事件等通用功能

[![Maven Central](https://img.shields.io/maven-central/v/io.github.taoweiji.webviewx/webviewx)](https://search.maven.org/search?q=io.github.taoweiji.webviewx)

WebViewX：WebView 能力增强框架，提供易用的异步API，简化原生为JS提供接口的开发成本；提供网站离线运行能力，大大提高前后端分离架构的访问体验；提供页面事件订阅，让H5可以感知 Activity 的生命周期，并提供通用的事件，实现原生发到事件到页面。

- 提供网页离线运行，提升“前后端分离模式”和“静态网页”下的体验；
- 提供同步、异步 API功能，支持全局及临时注册方式，简化 H5 调用原生能力；
- 提供原生页面生命周期监听，让H5可以感知原生页面的生命周期，实现合理的打点；
- 提供通用的事件注册，简化原生调用H5函数的方式；
- 提供多个常用 API。

### 添加依赖

```groovy
implementation 'io.github.taoweiji.webviewx:webviewx:+'
```



### 创建 WebViewX

必须保证 onResume、onPause、destroy 正常调用，页面事件是依赖这几个方法实现的，否则会造成页面事件不准确。

```java
public class WebViewXBridgeActivity extends AppCompatActivity {
    WebViewX webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebViewX(this);
        setContentView(webView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}
```



### 注册离线资源

通过离线运行，可以加快“前后端分离模式”和“静态网页”加载速度，提升用户体验，支持assets路径，也支持文件路径。
```java
webViewX.addLocalResource("https://2048.com", "file:///android_asset/2048");
String path = new File(this.getFilesDir(),"1024").getAbsolutePath();
webViewX.addLocalResource("https://1024.com", path);
webViewX.loadUrl("https://2048.com");
```



#### 全局注册API

```java
public class GetUserApi implements Api {
    @Override
    public String name() {
        return "getUser";
    }
    @Override
    public boolean allowInvokeSync() {
      	// 是否支持同步调用
        return true;
    }
    @Override
    public void invoke(@NonNull ApiCaller caller) throws Exception {
        int id = caller.getParams().optInt("id");
        if (id == 0){
            caller.fail(new Exception("请求参数异常"));
        }else {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("name", "Wiki");
            caller.success(json);
        }
    }
}

```

在Application 注册

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiManager.getInstance().register(new GetUserApi());
    }
}
```



#### 临时注册API

如果有些功能只需要在当前的WebView使用，可以通过拦截器临时实现实现相关的功能。

```java
// 通过拦截器临时注册API
WebViewX webViewX = new WebViewX(this);
webViewX.addInterceptor(new WebViewXBridge.Interceptor() {
    @Override
    public boolean invoke(ApiCaller caller) {
        if ("getTestData".equals(caller.getApiName())) {
            caller.successData("Hello World");
            return true;
        }
        return false;
    }
    @Override
    public boolean interrupt(@Nullable String url, @NonNull String apiName) {
        return false;
    }
});
```



#### 触发页面事件

##### setLoadOptions

这个方法用于设置页面事件的onLoad(data)函数，可以实现告知网页当前页面的一些访问信息

```java
JSONObject json = new JSONObject();
json.put("from", "home");
webViewX.setLoadOptions(json);
```



#### 发送普通事件
H5端必须已经注册才能收到事件，如果是在H5加载中发送事件，可能会导致无法收到事件
```java
JSONObject json = new JSONObject();
json.put("id", id);
json.put("name", "Wiki"); 
webViewX.postEvent("loginChanged",json);
```

#### 发送粘性事件
粘性事件，如果在H5已经注册，那么发送时就可以收到事件，如果H5还在加载中，当H5注册事件时会收到事件，页面重新注册也会收到。
```java
JSONObject json = new JSONObject();
json.put("id", id);
json.put("name", "Wiki"); 
webViewX.postEvent("loginChanged",json);
```

#### 取消粘性事件
由于粘性事件可以重复接收，如果不喜欢事件继续传播，可以在JS端中断事件的发送。
```Javascript
// 取消eventName所有的事件
webViewX.removeStickyEvent('eventName');
// 取消单一事件对象
webViewX.removeStickyEvent(event);
```
## H5则使用WebViewX

使用 WebViewX框架需要在H5中引入 webviewx.js 文件，提供2种引入方式。

##### 直接引入js文件

需要把 [js文件](https://github.com/taoweiji/WebViewX/blob/master/webviewx/src/main/assets/webviewx/webviewx.js) 拷贝到web工程中，不用担心js版本和客户端版本不一致问题，在客户端使用的时候会自动加载客户端匹配的 js 文件。

```html
<html>
<body>
    <script src="js/webviewx.js"></script>
<body>
</html>
```
##### 动态注册（推荐）

动态注册的方式是从客户端的sdk获取当前的sdk的js框架代码，通过eval方式注册，好处是不用添加js文件，缺点是不方便调试。

```html
<html>
<body>
    <script>
        window.webViewX = function () {
            if (window['webViewXBridge'] == undefined) {
                return {
                    registerPageEvent(data) { },
                    registerEvent(name, data) { },
                    invoke(name, data) { },
                    invokeSync(name, data) { },
                    postEvent(name, data){ }
                };
            }
            return eval(webViewXBridge.getSource());
        }();
    </script>
<body>
</html>
```
#### 注册页面事件

- onLoad：在页面加载完毕时候执行，事件带有options参数可以用于实现注入参数，比如获取访问来源等；
- onShow：页面显示时候触发，可以在这里做页面刷新的行为，可以配合onHide实现页面访问时长统计；
- onHide：页面隐藏时候触发；
- onUnload：在页面被关闭的时候触发，通常用于单 WebView 只加载一个页面的场景；

```javascript
webViewX.registerPageEvent({
    onLoad: function (options) {
        // Do some initialize when page load.
        console.log('onLoad ' + JSON.stringify(options));
    },
    onShow: function () {
        // Do something when page show.
        console.log('onShow');
    },
    onHide: function () {
        // Do something when page hide.
        console.log('onHide');
    },
    onUnload: function () {
        // Do something when page close.
        console.log('onUnload');
    }
})
```
#### 注册普通事件
```javascript
webViewX.registerEvent('onThemeChanged',function (data){
    alert(JSON.stringify(data));
})
```
#### 同步调用API

如果成功返回值是json，出错情况下会抛出错误。

```javascript
var data = webViewX.invokeSync('getTestData')
alert(res.data)
```
#### 异步调用API
```javascript
webViewX.invoke('getUser',{
	id: 101,
	success(res) {
		alert(JSON.stringify(res))
	},
  fail(error){
    alert(error)
  }
})
```



### 完整测试示例

```html
<html>
<body>
    <script>
        var webViewX = eval(webViewXBridge.getSDKSource());
        function getUser1(){
          	// 测试正常响应
            webViewX.invoke('getUser',{
                id: 101,
                success(res) {
                    alert(res.data)
                }
            })
        }
        function getUser2(){
          	// 测试错误响应
            webViewX.invoke('getUser'{
                fail(error) {
                    alert(error)
                }
            })
        }
        function getTestData(){
          	// 同步请求接口
          	var data = webViewX.invokeSync('getTestData')
          	alert(res.data)
        }
    </script>
    <button onclick="getUser1()">测试正常响应</button>
    <button onclick="getUser2()">测试错误响应</button>
  	<button onclick="getTestData()">同步请求接口</button>
</body>
</html>
```



### API 公共参数

| 属性     | 类型     | 默认值 | 必填 | 说明      |
| :------- | :------- | :----- | :--- | :----------------------------------------------- |
| success  | function |        | 否   | 接口调用成功的回调函数                           |
| fail     | function |        | 否   | 接口调用失败的回调函数                           |
| complete | function |        | 否   | 接口调用结束的回调函数（调用成功、失败都会执行） |


#### 示例

```javascript
webViewX.invoke('showToast',{
  title: 'title',
  success(res){
    alert(JSON.stringly(res));
  },
  fail(error){
    alert(error);
  },
  complete(){
    console.log('complete')
  }
})
```


| API 名称         | 说明 |
| ---------------- | ---- |
| getSystemInfo    | 获取手机型号、系统版本、屏幕宽高、屏幕密度等 |
| getNetworkType   | 获取当前网络类型 |
| setClipboardData | 设置内容到粘贴板 |
| getClipboardData | 获取粘贴板内容 |
| showToast        | 显示 toast |
| showModal        | 显示对话框 |
| share            | 系统分享 |
| openLocation     | 打开地图 |
| getLocation      | 获取当前定位 |
| addPhoneCalendar | 添加事件到日历 |
