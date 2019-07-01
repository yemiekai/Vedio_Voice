package com.yemiekai.vedio_voice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.yemiekai.vedio_voice.utils.tools.AtyContainer;
import com.yemiekai.vedio_voice.utils.tools.StringUtils;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

/**
 * 本程序所有Activity都要继承这个, 用于统一管理
 *
 */
public class BasicActivity extends Activity {
    public static final String ACTION_FORCE_NOTIFY = "com.yemiekai.vedio_voice.ACTION_NOTIFY";

    MyBasicReceiver myBasicReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);  // 添加Activity到堆栈
        initMyBasicReceiver(myBasicReceiver);  // 初始化广播接收器
    }

    // 初始化广播接收器
    public void initMyBasicReceiver(MyBasicReceiver myBasicReceiver){
        myBasicReceiver = new MyBasicReceiver();
        IntentFilter filter = new IntentFilter();  // 创建IntentFilter
        filter.addAction(ACTION_FORCE_NOTIFY);  // 指定BroadcastReceiver监听的Action
        registerReceiver(myBasicReceiver, filter);  // 注册BroadcastReceiver
    }

    /**
     * 弹出对话框
     */
    public static void DialogPrompt(Context context, String title, String msg) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.bn_confirm, null)
                .show();
    }

    /**
     * 查看屏幕信息
     */
    public static void screenInformation(Context context){
        DisplayMetrics metric = context.getResources().getDisplayMetrics();

        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX）

        float xdpi = context.getResources().getDisplayMetrics().xdpi;
        float ydpi = context.getResources().getDisplayMetrics().ydpi;

        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）

        String info = String.format(" \nWidth:%dpx %.0fdpi\nHeight:%dpx %.0fdpi\nDensity:%.2f\nDensity dpi:%ddpi",
                width, xdpi, height, ydpi, density, densityDpi);
        StringUtils.debug_print(info);
    }

    /**
     * 自定义的广播接收器  （插播医院通知）
     *
     * 继承BasicActivity的所有Activity都能响应广播
     */
    public class MyBasicReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            debug_print("BasicActivity -- MyBasicReceiver -- Get Action:  " + action);
            switch (action){

                case ACTION_FORCE_NOTIFY:  // 强制插播
                    Intent intent1 = new Intent(context, NotifyActivity.class);
                    startActivity(intent1);
                    break;

                default:
                     break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
