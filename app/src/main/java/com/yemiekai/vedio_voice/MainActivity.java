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
    TextView time;
    Timer timeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        time = (TextView) findViewById(R.id.main_time);

        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long sysTime = System.currentTimeMillis();//获取系统时间
                final CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd HH:mm:ss", sysTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(sysTimeStr);
                    }
                });
            }
        },0,1000);

        Button bn_voice = findViewById(R.id.bn_voice);
        Button bn_video = findViewById(R.id.bn_video);


        bn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPrompt(context,"语音识别","暂未开通");
            }
        });

        bn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
    }
}
