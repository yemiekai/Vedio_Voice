# Vedio_Voice
智慧医疗程序
&
602实验室安防程序--语音--图像


## 遇到的坑：
### 1. 发送Messenge时，将实现了Parcelable接口的数据放到Bundle中出错
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
```

解决办法：在`putParcelable`和`getParcelable`前, 对bundle设置class loader, 下面`TestData_p`是实现了`Parcelable`接口的类, 根据实际情况修改
```
Bundle bundle=new Bundle();
bundle.setClassLoader(TestData_p.class.getClassLoader());
```
