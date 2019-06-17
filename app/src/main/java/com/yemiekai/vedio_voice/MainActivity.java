package com.yemiekai.vedio_voice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.viewpager.widget.ViewPager;

import com.yemiekai.vedio_voice.utils.MainVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// 视频用法 : https://juejin.im/post/5cd2bf945188251a4442a869
// 视频框架 : https://blog.csdn.net/u010181592/article/details/49301703
// 视频轮播 : https://blog.csdn.net/watts2008/article/details/52710193
// 图片轮播： https://www.jb51.net/article/101517.htm
public class MainActivity extends BasicActivity {
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
        image = (ImageView)findViewById(R.id.main_image);

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
        // Uri uri = Uri.parse(video_path);  // 将路径转换成uri

        // 从u盘播放视频, u盘挂载地址为: /mnt/sda/sda1
        File file = new File("/mnt/sda/sda1", "video1.mp4");
        String video_path = file.getPath();
        Uri uri = Uri.parse(file.getPath());  // 将路径转换成uri
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
            public void handleMessage(Message msg) {//接收消息，并处理
                super.handleMessage(msg);
                image.setBackgroundResource(imageResIds[msg.what]);
            }
        };



        new Thread(new Runnable() {
            @Override
            public void run() {
                int num = 0;
                while (true) {
                    // 动画效果
                    AnimationSet set = new AnimationSet(true);
                    ScaleAnimation scale = new ScaleAnimation(0.92f, 1.0f, 0.92f, 1.0f ,Animation.RELATIVE_TO_SELF, 0.5f , Animation.RELATIVE_TO_SELF, 0.5f);
                    scale.setDuration(1500);
                    set.addAnimation(scale);
                    image.setAnimation(set);

                    Message message = new Message();
                    message.what = num;
                    handler.sendMessage(message);  // 发送消息

                    num++;
                    if(num >= imageResIds.length)  // 图片播放完，重置
                        num = 0;

                    try {
                        Thread.sleep(5000);  //暂停 5 秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
