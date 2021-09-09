# Android WebViewX 增强框架，提供同步/异步API、网页离线运行、页面事件等通用功能

[![Maven Central](https://img.shields.io/maven-central/v/io.github.taoweiji.webviewx/webviewx)](https://search.maven.org/search?q=io.github.taoweiji.webviewx)

WebViewX：WebView 能力增强框架，提供易用的异步API，简化原生为JS提供接口的开发成本；提供网站离线运行能力，大大提高前后端分离架构的访问体验；提供页面事件订阅，让H5可以感知 Activity 的生命周期，并提供通用的事件，实现原生发到事件到页面。

- 提供网页离线运行，提升“前后端分离模式”和“静态网页”下的体验；
- 提供页面事件监听，并支持设置预置参数，让页面根据页面状态更新数据，打点等；
- 支持普通事件、粘性事件；支持跨WebView发送事件、定向发送事件，实现多WebView架构；
- 支持同步、异步调用API，支持全局注册API、局部注册API，支持API权限控制；
- 提供多个常用 API。



## 基础准备

### Java侧

#### 添加依赖

```groovy
implementation 'io.github.taoweiji.webviewx:webviewx:+'
// X5拓展
// implementation 'io.github.taoweiji.webviewx:x5:+'
```

#### 创建 WebView

必须保证 onResume、onPause、destroy 正常调用，页面事件是依赖这几个方法实现的，否则会造成页面事件不准确。

```java
public class WebViewXBridgeActivity extends AppCompatActivity {
    WebViewX webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebViewX(this);
        // X5内核版本
        // webView = new X5WebViewX(this);
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



### JS侧

使用 WebViewX框架需要在H5中引入 webviewx.js 文件。

需要把 [js文件](https://github.com/taoweiji/WebViewX/blob/master/webviewx/src/main/assets/webviewx/webviewx.js) 拷贝到web工程中。也可以直接使用下面代码，客户端会自动加载本地的 webviewx.js 文件。

```html
<html>
<body>
    <script src="webviewx/webviewx.js"></script>
<body>
</html>
```

## 网页离线运行

通过离线运行，可以加快“前后端分离模式”和“静态网页”加载速度，提升用户体验，支持assets路径，也支持文件路径。
```java
webViewX.addLocalResource("https://2048.com", "file:///android_asset/2048");
String path = new File(this.getFilesDir(),"1024").getAbsolutePath();
webViewX.addLocalResource("https://1024.com", path);
webViewX.loadUrl("https://2048.com");
```



## 页面事件

### Java侧
#### 设置预设参数
这个方法用于设置页面的预设参数，让页面可以在 onLoad 响应预设参数。

```java
JSONObject json = new JSONObject();
json.put("entry", "home");
webViewX.setLoadOptions(json);
```

### JS侧

#### 注册页面事件

```javascript
webViewX.registerPageEvent({
    onLoad: function (options) {
        // Do some initialize when page load.
    },
    onShow: function () {
        // Do something when page show.
    },
    onHide: function () {
        // Do something when page hide.
    },
    onUnload: function () {
        // Do something when page close.
    }
})
```

#### 获取预设参数

```javascript
var options = webViewX.invokeSync('WebViewX.getLoadOptions')
```

#### 获取页面状态

```javascript
var res = webViewX.invokeSync('WebViewX.isShowed')
console.log('isShowed', res.data);
```




## 事件、粘性事件
### Java侧
#### 发送普通事件
H5端必须已经注册才能收到事件，如果是在H5加载中发送事件，可能会导致无法收到事件
```java
JSONObject json = new JSONObject();
json.put("msg", "Hello World"); 
webViewX.postEvent("event_name",json);
```
#### 发送粘性事件
粘性事件，如果在H5已经注册，那么发送时就可以收到事件，如果H5还在加载中，当H5注册事件时会收到事件，页面重新注册也会收到。
```java
JSONObject json = new JSONObject();
json.put("msg", "Hello World"); 
webViewX.postStickyEvent("event_name",json);
```
#### 取消粘性事件
由于粘性事件可以重复接收，如果不希望事件继续传播，可以在JS端中断事件的发送。
```Javascript
webViewX.removeStickyEvent('event_name');
```
### JS侧

#### 注册事件

```Javascript
webViewX.registerEvent('event_name', function (data) {
    console.log(JSON.stringify(data))
})
```

#### 发送普通事件

```Javascript
webViewX.postEvent('event_name', { 'msg': 'Hello World' })
```

#### 发送粘性事件

```Javascript
webViewX.postStickyEvent('event_name', { 'msg': 'Hello World' })
```

#### 取消粘性事件
由于粘性事件可以重复接收，如果不希望事件继续传播，可以中断事件的发送。
```Javascript
webViewX.removeStickyEvent('event_name');
```



## 事件中心：跨WebView发送事件

默认情况下，事件只能在当前WebView收发，无法发送到其它的WebView，如果要适配类似微信小程序架构，每个页面都使用独立的WebView加载，如果要把事件发送到其它的WebView，那么就要事件注册中心。
### 广播事件
所有在事件中心注册的WebView都可以收到事件。
### Java侧
```java
webViewX.broadcastEvent("event_name",event);
```

### JS侧
```java
webViewX.broadcastEvent('event_name',event);
```



### 定向发送事件（TODO）

可以通过页面名称/ID从事件中心获取WebView接收者对象，从而实现定向发送。

```java
WebViewX.getEventCenter().getEventReceiver("").postEvent("event_name",event);
```

### JS侧
```java
webViewX.getEventReceiver('name').postEvent('event_name',event);
```

## API

### JS侧

#### 调用异步API

```javascript
webViewX.invoke('showToast',{
	title: 'title',
	success(res) {
		alert(JSON.stringify(res))
	},
  fail(error){
    alert(error)
  },
  omplete(){
    console.log('complete')
  }
})
```

#### 调用同步API

```javascript
var data = webViewX.invokeSync('getTestData')
alert(res.data)
```

### Java侧
#### 全局注册API

```java
public class GetUserApi extends Api {
    public String name() { return "getUser";}
    public boolean allowInvokeSync() {return true;}
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
// 在Application注册
ApiManager.getInstance().register(new GetUserApi());
```

#### 局部注册API

如果有些功能只需要在当前的WebView使用，可以通过拦截器临时实现实现相关的功能。

```java
WebViewX webViewX = new WebViewX(this);
webViewX.addInterceptor(new WebViewXBridge.Interceptor() {
    @Override
    public boolean invoke(ApiCaller caller) {
        if ("getTestData".equals(caller.getApiName())) {
            caller.successData("Hello World");
            return true;
        }
      	// 增加参数
      	caller.putExtra("webView", webView);
        return false;
    }
    @Override
    public boolean interrupt(@Nullable String url, @NonNull String apiName) {
      	// 黑名单中断请求
        if (url == null || url.contains("abc.com")) {
        	return false;
        }
        return false;
    }
});
```

### 内置API

| API 名称                | 说明                                         |
| ----------------------- | -------------------------------------------- |
| WebViewX.getLoadOptions | 获取页面预置参数                             |
| WebViewX.isShowed       | 获取页面显示状态                             |
| getSystemInfo           | 获取手机型号、系统版本、屏幕宽高、屏幕密度等 |
| getNetworkType          | 获取当前网络类型                             |
| setClipboardData        | 设置内容到粘贴板                             |
| getClipboardData        | 获取粘贴板内容                               |
| showToast               | 显示 toast                                   |
| showModal               | 显示对话框                                   |
| share                   | 系统分享                                     |
| openLocation            | 打开地图                                     |
| getLocation             | 获取当前定位                                 |
| addPhoneCalendar        | 添加事件到日历                               |



