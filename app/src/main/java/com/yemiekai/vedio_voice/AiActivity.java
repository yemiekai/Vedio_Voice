package com.yemiekai.vedio_voice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yemiekai.vedio_voice.utils.tools.MyNetworkUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AiActivity extends BasicActivity {
    Context context;
    TextView time;
    Timer timeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        context = AiActivity.this;
        time = (TextView) findViewById(R.id.ai_time);

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

        Button bn_face_match = findViewById(R.id.ai_bn_face_match);
        Button bn_face_entry = findViewById(R.id.ai_bn_face_entry);

        // 匹配人脸
        bn_face_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPrompt(context,"匹配人脸","暂未开通");
//                startActivity(new Intent(AiActivity.this, VideoFaceActivity.class));
            }
        });

        // 录入人脸
        bn_face_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AiActivity.this, FaceEnterActivity.class));
            }
        });

        MyNetworkUtils.getInstance(this).sayHello(595);
    }
}
