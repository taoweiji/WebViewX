

### showToast

显示消息提示框

| 属性     | 类型     | 默认值  | 必填 | 说明                                             |
| :------- | :------- | :------ | :--- | :----------------------------------------------- |
| title    | string   |         | 是   | 提示的内容                                       |
| icon     | string   | success | 否   | 图标                                             |
| image    | string   |         | 否   | 自定义图标的本地路径，image 的优先级高于 icon    |
| duration | number   | 1500    | 否   | 提示的延迟时间                                   |
| mask     | boolean  | false   | 否   | 是否显示透明蒙层，防止触摸穿透                   |
| success  | function |         | 否   | 接口调用成功的回调函数                           |
| fail     | function |         | 否   | 接口调用失败的回调函数                           |
| complete | function |         | 否   | 接口调用结束的回调函数（调用成功、失败都会执行） |

#### 示例

```javascript
webViewX.invoke('showToast',{
  title: 'title'
})
```



### showModal

显示模态对话框

| 属性         | 类型     | 默认值  | 必填 | 说明                                               |
| :----------- | :------- | :------ | :--- | :------------------------------------------------- |
| title        | string   |         | 否   | 提示的标题                                         |
| content      | string   |         | 否   | 提示的内容                                         |
| showCancel   | boolean  | true    | 否   | 是否显示取消按钮                                   |
| cancelText   | string   | 取消    | 否   | 取消按钮的文字，最多 4 个字符                      |
| cancelColor  | string   | #000000 | 否   | 取消按钮的文字颜色，必须是 16 进制格式的颜色字符串 |
| confirmText  | string   | 确定    | 否   | 确认按钮的文字，最多 4 个字符                      |
| confirmColor | string   | #576B95 | 否   | 确认按钮的文字颜色，必须是 16 进制格式的颜色字符串 |
| success      | function |         | 否   | 接口调用成功的回调函数                             |
| fail         | function |         | 否   | 接口调用失败的回调函数                             |
| complete     | function |         | 否   | 接口调用结束的回调函数（调用成功、失败都会执行）   |

#### object.success 回调函数

| 属性    | 类型    | 说明                                                         |
| :------ | :------ | :----------------------------------------------------------- |
| content | string  | editable 为 true 时，用户输入的文本                          |
| confirm | boolean | 为 true 时，表示用户点击了确定按钮                           |
| cancel  | boolean | 为 true 时，表示用户点击了取消（用于 Android 系统区分点击蒙层关闭还是点击取消按钮关闭） |

#### 示例

```javascript
webViewX.invoke('showModal',{
  title: 'title',
  content: 'content',
  success(res){
    alert(res.confirm)
  }
})
```

### getLocation

获取当前的地理位置、速度。

| 属性                   | 类型     | 默认值 | 必填 | 说明                                                         |
| :--------------------- | :------- | :----- | :--- | :----------------------------------------------------------- |
| type                   | string   | wgs84  | 否   | wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标 |
| altitude               | string   | false  | 否   | 传入 true 会返回高度信息，由于获取高度需要较高精确度，会减慢接口返回速度 |
| isHighAccuracy         | boolean  | false  | 否   | 开启高精度定位                                               |
| highAccuracyExpireTime | number   |        | 否   | 高精度定位超时时间(ms)，指定时间内返回最高精度，该值3000ms以上高精度定位才有效果 |
| success                | function |        | 否   | 接口调用成功的回调函数                                       |
| fail                   | function |        | 否   | 接口调用失败的回调函数                                       |
| complete               | function |        | 否   | 接口调用结束的回调函数（调用成功、失败都会执行）             |

#### object.success 回调函数

| 属性               | 类型   | 说明                                         |
| :----------------- | :----- | :------------------------------------------- |
| latitude           | number | 纬度，范围为 -90~90，负数表示南纬            |
| longitude          | number | 经度，范围为 -180~180，负数表示西经          |
| speed              | number | 速度，单位 m/s                               |
| accuracy           | number | 位置的精确度                                 |
| altitude           | number | 高度，单位 m                                 |
| verticalAccuracy   | number | 垂直精度，单位 m（Android 无法获取，返回 0） |
| horizontalAccuracy | number | 水平精度，单位 m                             |

#### 示例

```javascript
webViewX.invoke('getLocation',{
  type: 'wgs84',
  success(res){
    alert(res.latitude + ',' + res.longitude)
  }
})
```

### 

