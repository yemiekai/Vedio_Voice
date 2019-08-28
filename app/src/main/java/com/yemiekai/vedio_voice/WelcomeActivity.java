package com.yemiekai.vedio_voice;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.yemiekai.vedio_voice.services.MyNetworkService;
import com.yemiekai.vedio_voice.utils.tools.AtyContainer;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends BasicActivity {
    public final static int DISPLAY_WAITING_VIDEO_DIALOG = 2000;  // 显示“等待视频”

    public final static int LOAD_VIDEO_FINISHED  = 2001;  // 加载视频完毕


    private Activity context = null;
    private VideoView vv;
    private boolean bVideoComplete = false;

    private TextView tx_time;
    private TextView tx_date;
    private ConstraintLayout layout2;
    private Timer timeTimer;
    private static int backKeyCount = 0;

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case DISPLAY_WAITING_VIDEO_DIALOG:  // 显示“正在加载”
                    showLoadingDialog(true);
                    break;

                case LOAD_VIDEO_FINISHED:  // 加载视频完毕
                    showLoadingDialog(false);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        vv = (VideoView) findViewById(R.id.welcome_video);

        // 提示正在加载视频
        sendMsgInner(DISPLAY_WAITING_VIDEO_DIALOG);

//        final String uri_video = "android.resource://" + getPackageName() + "/" + R.raw.welcome_vedio2;  // 本地视频
        final String uri_video = "http://192.168.1.106:7001/public/video/bootup/welcome.mp4";  // 从后台获取
        vv.setVideoURI(Uri.parse(uri_video));
        vv.start();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                sendMsgInner(LOAD_VIDEO_FINISHED);  // 关闭"正在加载"的对话框
                mp.start();
                mp.setLooping(false);
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bVideoComplete = true;
                dispayWishPage();  // 视频播放完毕, 显示祝福页
                sendMsgInner(LOAD_VIDEO_FINISHED);  // 关闭"正在加载"的对话框
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        debug_print("OnKeyDown, keyCode:" + keyCode);

        // 连按8次返回键可退出程序, 连按2次可以结束视频
        if(keyCode==KEYCODE_BACK){
            backKeyCount++;
        }else {
            backKeyCount = 0;
        }


        switch (keyCode){
            case KEYCODE_BACK:
                if(backKeyCount >= 2){  // 按2下返回, 停止视频, 切换页面
                    if(vv != null){
                        vv.suspend();
                        dispayWishPage();
                    }
                }
                if(backKeyCount >= 8) {  // 按8下返回键, 退出程序
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



        return false;
    }

    // 显示祝福页
    public void dispayWishPage(){
        // 切换布局
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


    public void sendMsgInner(int what) {
        Message msg = new Message();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

}
