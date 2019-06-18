package com.yemiekai.vedio_voice;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// 视频用法 : https://juejin.im/post/5cd2bf945188251a4442a869
// 视频框架 : https://blog.csdn.net/u010181592/article/details/49301703
// 视频轮播 : https://blog.csdn.net/watts2008/article/details/52710193
// 图片轮播： https://www.jb51.net/article/101517.htm
public class MainActivity extends BasicActivity {
    public final static int MSG_CHANGE_IMAGE             = 1000;       // 切换图片
    public final static int MSG_ZOOM_OUT_IMAGE           = 1001;       // 淡出

    public final static int TIME_IMAGE_ZOOM_OUT          = 700;        // 切换图片时,图片淡出动画时长

    Context context;
    private VideoView video;
    private ImageView image;
    private ImageButton bt_tv;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;

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

        video = (VideoView) findViewById(R.id.main_video);
        image = (ImageView) findViewById(R.id.main_image);

        bt_tv = (ImageButton)findViewById(R.id.main_bn_tv);
        bt_tv.requestFocus();

        // 时间日期
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

        // 轮播图片
        init_circulate_pictures();

        // 从网络播放视频
        // String video_path = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";

        // 从u盘播放视频, u盘挂载地址为: /mnt/sda/sda1; 本机存储空间地址为: /storage/emulated/0
        String video_path = new File("/storage/emulated/0/Movies", "video1.mp4").getPath();
        Uri uri = Uri.parse(video_path);  // 将路径转换成uri

        video.setVideoURI(uri);  // 为视频播放器设置视频路径
        video.start();
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video.seekTo(0);
                video.start();  // 循环播放
            }
        });
    }

    private void init_circulate_pictures(){
        imageResIds = new int[]{R.drawable.recycle1, R.drawable.recycle2, R.drawable.recycle3, R.drawable.recycle4};

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case MSG_CHANGE_IMAGE:
                        image.setAnimation(createScaleAnim());  // 动画效果
                        image.setBackgroundResource(imageResIds[msg.arg1]);
                        break;
                    case MSG_ZOOM_OUT_IMAGE:
                        image.startAnimation(createZoomOutAnim());
                        break;
                    default:
                        break;
                }

            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                int num = 0;
                while (true) {
                    // 发送消息, 切换图片
                    Message msg_change_image = new Message();
                    msg_change_image.what = MSG_CHANGE_IMAGE;
                    msg_change_image.arg1 = num;
                    handler.sendMessage(msg_change_image);

                    num++;
                    if(num >= imageResIds.length){
                        num = 0;
                    }

                    try {
                        Thread.sleep(7000);  // 暂停一下(展示时长)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 发送消息, 图片淡出
                    Message msg_zoom_out = new Message();
                    msg_zoom_out.what = MSG_ZOOM_OUT_IMAGE;
                    handler.sendMessage(msg_zoom_out);

                    try {
                        Thread.sleep(TIME_IMAGE_ZOOM_OUT);  // 暂停一下, 图片淡出
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /** 创建一个淡出的动画 **/
    public Animation createZoomOutAnim() {
        // 淡出动画动画
        Animation anim = new AlphaAnimation(1f, 0f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(TIME_IMAGE_ZOOM_OUT);
        anim.setFillAfter(true);

        // 动画集合?
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(true);  // 动画结束后保留状态

        // 加入动画
        set.addAnimation(anim);
        return set;
    }

    /** 创建一个缩放动画 **/
    public Animation createScaleAnim() {
        // 缩放动画
        ScaleAnimation scale = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f , Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(800);

        // 淡入动画
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setInterpolator(new DecelerateInterpolator());
        alpha.setDuration(800);
        alpha.setFillAfter(true);

        // 加入两个动画
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scale);
        set.addAnimation(alpha);
        return set;
    }

}
