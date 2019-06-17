package com.yemiekai.vedio_voice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BasicActivity {
    Context context;
    TextView date;
    TextView time;
    Timer timeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        date = (TextView) findViewById(R.id.main_date);
        time = (TextView) findViewById(R.id.main_time);

        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long sysTime = System.currentTimeMillis();//获取系统时间
                final CharSequence sysDateStr = DateFormat.format("MM/dd", sysTime);
                final CharSequence sysDayStr = DateFormat.format("E", sysTime);
                final CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(sysTimeStr);
                        String date_day = sysDateStr+"\n"+sysDayStr;
                        date.setText(date_day);
                    }
                });
            }
        },0,1000);

    }
}