package com.yemiekai.vedio_voice;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import com.yemiekai.vedio_voice.services.MyNetworkService;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends BasicActivity {

    private Activity context = null;
    private boolean bVideoComplete = false;

    private TextView tx_time;
    private TextView tx_date;
    private ConstraintLayout layout2;
    private Timer timeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);


        final VideoView vv = (VideoView) this.findViewById(R.id.welcome_video);

        final String uri_video = "android.resource://" + getPackageName() + "/" + R.raw.welcome_vedio;
        vv.setVideoURI(Uri.parse(uri_video));
        vv.start();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(false);
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bVideoComplete = true;
                dispayWishPage();  // 视频播放完毕， 显示祝福页
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 视频放完了才能响应遥控器
        if(bVideoComplete){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
//            context.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 显示祝福页
    public void dispayWishPage(){
        setContentView(R.layout.activity_welcome2);
        layout2 = (ConstraintLayout) this.findViewById(R.id.welcome2_layout);
        tx_time = (TextView) this.findViewById(R.id.welecome2_time);
        tx_date = (TextView) this.findViewById(R.id.welecome2_date);

        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long sysTime = System.currentTimeMillis();//获取系统时间
                final CharSequence sysDateStr = DateFormat.format("yyyy/MM/dd E", sysTime);
                final CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tx_time.setText(sysTimeStr);
                        tx_date.setText(sysDateStr);
                    }
                });
            }
        },0,1000);

        layout2.setBackgroundResource(R.drawable.welcome_image);  // 显示欢迎图片
    }


}
