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
import com.yemiekai.vedio_voice.utils.tools.AtyContainer;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends BasicActivity {

    private Activity context = null;
    private boolean bVideoComplete = false;

    private TextView tx_time;
    private TextView tx_date;
    private ConstraintLayout layout2;
    private Timer timeTimer;
    private static int backKeyCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);


        final VideoView vv = (VideoView) this.findViewById(R.id.welcome_video);

        final String uri_video = "android.resource://" + getPackageName() + "/" + R.raw.welcome_vedio2;
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
        debug_print("OnKeyDown, keyCode:" + keyCode);

        // 连按8次返回键可退出程序
        if(keyCode==KEYCODE_BACK){
            backKeyCount++;
        }else {
            backKeyCount = 0;
        }

        // 视频放完了才能响应遥控器
        if(bVideoComplete)
        {
            switch (keyCode){
                case KEYCODE_BACK:  // 连按8次返回键可退出程序
                    if(backKeyCount >= 8) {
                        AtyContainer.getInstance().finishAllActivity();  // 关闭所有Activity, 完全退出程序
                    }
                    break;
                case KEYCODE_DPAD_CENTER:  // 确认键
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  // 防止重复启动
                    startActivity(intent);

                default:
                    return super.onKeyDown(keyCode, event);
            }

        }

        return false;
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
