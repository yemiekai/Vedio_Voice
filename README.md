# Vedio_Voice
智慧医疗程序
&
602实验室安防程序--语音--图像  </br>  

## 待定问题：
#### 1. APP界面没有适配其它dpi的屏幕，只在机顶盒电视机上可以正常显示  
#### 2. 退出程序没有正确关闭Service  
#### 3. 没有做实时人脸检测和对比。一是检测算法和对比算法还不够高效，二是camera preview不好做。目前是用MTCNN和MobileNetV3-InsightFace  

## 遇到的坑：
### 1. 发送Messenge时，用Bundle传递实现了Parcelable接口的数据, 运行程序出错
```
2019-06-24 21:29:07.414 4074-4074/com.yemiekai.vedio_voice E/Parcel: Class not found when unmarshalling: com.yemiekai.vedio_voice.utils.datas.TestData_p
    java.lang.ClassNotFoundException: com.yemiekai.vedio_voice.utils.datas.TestData_p
        at java.lang.Class.classForName(Native Method)
        at java.lang.Class.forName(Class.java:400)
        at android.os.Parcel.readParcelableCreator(Parcel.java:2508)
        at android.os.Parcel.readParcelable(Parcel.java:2462)
        at android.os.Parcel.readValue(Parcel.java:2365)
        at android.os.Parcel.readArrayMapInternal(Parcel.java:2732)
        at android.os.BaseBundle.unparcel(BaseBundle.java:269)
        at android.os.Bundle.getParcelable(Bundle.java:864)
        at com.yemiekai.vedio_voice.utils.tools.MyNetworkUtils$ReceiverReplyMsgHandler.handleMessage(MyNetworkUtils.java:113)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:154)
        at android.app.ActivityThread.main(ActivityThread.java:6119)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)
     Caused by: java.lang.ClassNotFoundException: com.yemiekai.vedio_voice.utils.datas.TestData_p
        at java.lang.Class.classForName(Native Method)
        at java.lang.BootClassLoader.findClass(ClassLoader.java:1346)
        at java.lang.BootClassLoader.loadClass(ClassLoader.java:1406)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:312)
        at java.lang.Class.classForName(Native Method) 
        at java.lang.Class.forName(Class.java:400) 
        at android.os.Parcel.readParcelableCreator(Parcel.java:2508) 
        at android.os.Parcel.readParcelable(Parcel.java:2462) 
        at android.os.Parcel.readValue(Parcel.java:2365) 
        at android.os.Parcel.readArrayMapInternal(Parcel.java:2732) 
        at android.os.BaseBundle.unparcel(BaseBundle.java:269) 
        at android.os.Bundle.getParcelable(Bundle.java:864) 
        at com.yemiekai.vedio_voice.utils.tools.MyNetworkUtils$ReceiverReplyMsgHandler.handleMessage(MyNetworkUtils.java:113) 
        at android.os.Handler.dispatchMessage(Handler.java:102) 
        at android.os.Looper.loop(Looper.java:154) 
        at android.app.ActivityThread.main(ActivityThread.java:6119) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776) 
     Caused by: java.lang.NoClassDefFoundError: Class not found using the boot class loader; no stack trace available  
2019-06-24 21:29:07.415 4074-4074/com.yemiekai.vedio_voice E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.yemiekai.vedio_voice, PID: 4074
    android.os.BadParcelableException: ClassNotFoundException when unmarshalling: com.yemiekai.vedio_voice.utils.datas.TestData_p
        at android.os.Parcel.readParcelableCreator(Parcel.java:2536)
        at android.os.Parcel.readParcelable(Parcel.java:2462)
        at android.os.Parcel.readValue(Parcel.java:2365)
        at android.os.Parcel.readArrayMapInternal(Parcel.java:2732)
        at android.os.BaseBundle.unparcel(BaseBundle.java:269)
        at android.os.Bundle.getParcelable(Bundle.java:864)
        at com.yemiekai.vedio_voice.utils.tools.MyNetworkUtils$ReceiverReplyMsgHandler.handleMessage(MyNetworkUtils.java:113)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:154)
        at android.app.ActivityThread.main(ActivityThread.java:6119)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)    
```

主要有2个原因：(1)class loader的问题; (2)`TestData_p`类中序列化和反序列化(`writeToParcel`和`createFromParcel`)处理数据的顺序没对应好.
本问题解决办法：在`putParcelable`和`getParcelable`前, 对bundle设置class loader, 下面`TestData_p`是实现了`Parcelable`接口的类, 根据实际情况修改
```
Bundle bundle=new Bundle();
bundle.setClassLoader(TestData_p.class.getClassLoader());
bundle.putParcelable("TAG", new TestData_p());
```
参考：https://stackoverflow.com/questions/18126249/badparcelableexception-classnotfoundexception-when-unmarshalling-myclass-wh;
https://blog.csdn.net/Bettarwang/article/details/45315091

### 2. ADV模拟器黑屏问题
第一次新建模拟器后, 启动没有问题。
关掉模拟器或者重启电脑后, 再次启动, 模拟器一直黑屏, AndroidStudio显示waiting for target device to come online。   
![图片](https://github.com/yemiekai/Tests/tree/master/raw/dark_screen.png);  
解决办法, 冷启动设备：  
打开AndroidStudio的虚拟管理器Android Virtual Device Manager, 找到要开启的设备, 在最右边Actions下拉框中选择`Cool Boot Now`.  
  
## 参考：
MTCNN: https://github.com/vcvycy/MTCNN4Android
TensorflowLite:1.https://github.com/tensorflow/examples/tree/master/lite/examples/image_classification/android  
  2.https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android    
  

