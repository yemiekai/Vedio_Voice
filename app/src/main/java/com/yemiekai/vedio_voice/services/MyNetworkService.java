package com.yemiekai.vedio_voice.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.yemiekai.vedio_voice.utils.datas.TestData_p;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;


//  关于Android Service真正的完全详解，你需要知道的一切:
//  https://blog.csdn.net/javazejian/article/details/52709857
public class MyNetworkService extends Service {
    public static final int MSG_SAY_HELLO = 1;

    public MyNetworkService() {
    }

    /**
     * 用于接收从客户端传递过来的数据
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    debug_print("Receive msg: " + msg.arg1);

                    //回复客户端信息,该对象由客户端传递过来
                    Messenger client=msg.replyTo;
                    //获取回复信息的消息实体
                    Message replyMsg=Message.obtain(null, MyNetworkService.MSG_SAY_HELLO);
                    Bundle bundle=new Bundle();
                    bundle.setClassLoader(TestData_p.class.getClassLoader());
                    TestData_p data = new TestData_p(123);
//                    bundle.setClassLoader(getClass().getClassLoader());
//                    bundle.putString("reply","ok~,I had receiver message from you! ");
                    bundle.putParcelable("ppp", data);
                    replyMsg.setData(bundle);
                    //向客户端发送消息
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 创建Messenger并传入Handler实例对象
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * 绑定服务时才会调用, 必须要实现的方法
     */
    @Override
    public IBinder onBind(Intent intent) {
        debug_print("MyNetworkService -- onBind invoke");
        return mMessenger.getBinder();
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        debug_print("MyNetworkService -- onCreate invoke");
        super.onCreate();
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        debug_print("MyNetworkService -- onStartCommand invoke");
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;  // 比较高的优先级，在内存资源紧张时也不会被杀掉。
    }

    @Override
    public boolean onUnbind(Intent intent) {
        debug_print("MyNetworkService -- onUnbind invoke");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        debug_print("MyNetworkService -- onDestroy invoke");
        super.onDestroy();
    }
}
