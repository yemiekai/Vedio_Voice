package com.yemiekai.vedio_voice.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.yemiekai.vedio_voice.services.MyNetworkService;
import com.yemiekai.vedio_voice.utils.datas.Doctor_p;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

/**
 * 用单例模式
 */
public class MyNetworkUtils {
    private Messenger mRecevierReplyMsg= new Messenger(new ReceiverReplyMsgHandler());
    private static Activity mActivity;
    private Messenger mService = null;  // 与服务端交互的Messenger
    private ServiceConnection mConnection;  // 与服务端链接的对象
    private boolean mBound;  // 是否绑定了

    // 单例
    private static MyNetworkUtils instance = new MyNetworkUtils();

    public static MyNetworkUtils getInstance(Activity activity){
        mActivity = activity;
        return instance;
    }

    // 构造函数, 私有, 单例
    private MyNetworkUtils(){
        this.mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                /**
                 * 通过服务端传递的IBinder对象,创建相应的Messenger
                 * 通过该Messenger对象与服务端进行交互
                 */
                debug_print("on Service Connected");
                mService = new Messenger(service);
                mBound = true;
            }

            public void onServiceDisconnected(ComponentName className) {
                debug_print("on Service Disconnected");
                mService = null;
                mBound = false;
            }
        };
    }

    public void setActivity(Activity activity){
        this.mActivity = activity;
    }

    /**
     * 启动服务
     */
    public void startNetworkService(){
        Intent intent = new Intent(mActivity, MyNetworkService.class);
        mActivity.startService(intent);
        mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 绑定服务
     * 调用完这个函数要等一等, 没这么快绑定成功
     */
    public boolean bindService(){
        Intent intent = new Intent(mActivity, MyNetworkService.class);
        return mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定服务
     */
    public void unbindService(){
        mActivity.unbindService(mConnection);
    }

    public void sayHello(int waitToSay) {
        if (!mBound) return;
        // 创建与服务交互的消息实体Message
        Message msg = Message.obtain(null, MyNetworkService.MSG_SAY_HELLO, waitToSay, 0);
        msg.replyTo = mRecevierReplyMsg;
        try {
            //发送消息
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class ReceiverReplyMsgHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接收服务端回复
                case MyNetworkService.MSG_SAY_HELLO:
                    Bundle bundle = msg.getData();
                    bundle.setClassLoader(Doctor_p.class.getClassLoader());

                    Doctor_p data = bundle.getParcelable("ppp");
                    String mmsg = bundle.getString("reply");
                    debug_print("receiver message from service:" + mmsg);
                    debug_print("receiver message from service:" + data.name);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
