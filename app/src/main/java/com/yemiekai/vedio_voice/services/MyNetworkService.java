package com.yemiekai.vedio_voice.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yemiekai.vedio_voice.utils.datas.Doctor_gson;
import com.yemiekai.vedio_voice.utils.datas.Doctor_p;
import com.yemiekai.vedio_voice.utils.tools.HttpUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;


//  关于Android Service真正的完全详解，你需要知道的一切:
//  https://blog.csdn.net/javazejian/article/details/52709857
public class MyNetworkService extends Service {
    public static final int MSG_STOP_SERVICE = 0;  // 关闭service
    public static final int MSG_SAY_HELLO = 1;
    public static final int MSG_SAY_HELLO2 = 12;
    public static final int MSG_GET_DOCTORS = 2;        // 从后端获取医生信息
    public static final int MSG_GET_DOCTORS_REPLY = 3;  // 获取医生信息后, 回复信息

    public MyNetworkService() {
    }

    /**
     * 接收客户端过来的消息
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            debug_print("MyNetworkService receive msg, msg.what=" + msg.what);
            Messenger client = msg.replyTo;  // 客户端的Messenger, 用于回复

            switch (msg.what) {
                case MSG_STOP_SERVICE:
                    // todo 销毁服务的问题貌似没解决
                    debug_print("get stop service");
                    onDestroy();
                    break;

                case MSG_SAY_HELLO:
                    Message replyMsg = Message.obtain(null, MyNetworkService.MSG_SAY_HELLO);
                    Bundle bundle = new Bundle();
                    bundle.setClassLoader(Doctor_p.class.getClassLoader());

                    ArrayList<Doctor_p> doctorList = new ArrayList<>();
                    Doctor_p doctorP1 = new Doctor_p();
                    doctorP1.name = "yekai1";
                    Doctor_p doctorP2 = new Doctor_p();
                    doctorP2.name = "yekai2";
                    doctorList.add(doctorP1);
                    doctorList.add(doctorP2);

                    bundle.putParcelableArrayList("ppp", doctorList);
                    bundle.putString("reply", "ok~,I had receiver message from you! ");
                    replyMsg.setData(bundle);
                    //向客户端发送消息
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

                case MSG_SAY_HELLO2:
                    int category1 = msg.arg1;  // 科室索引1
                    int category2 = msg.arg2;  // 科室索引2
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String resp = HttpUtil.executeGetMethod("http://112.74.186.129:7002/doctor/2", null);
                            List<Doctor_gson> doct = new Gson().fromJson(resp, new TypeToken<List<Doctor_gson>>(){}.getType());
                            debug_print(doct.toString());
                        }
                    }).start();


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
