package com.yemiekai.vedio_voice;
import android.os.Bundle;

/**
 * 用于插播医院通知, 用一个广播启动此活动：
 * Intent intent = new Intent();
 * intent.setAction(BasicActivity.ACTION_FORCE_NOTIFY);
 * sendBroadcast(intent);
 */
public class NotifyActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
    }

}
