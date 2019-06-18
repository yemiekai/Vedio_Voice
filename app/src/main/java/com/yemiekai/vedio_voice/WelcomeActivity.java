package com.yemiekai.vedio_voice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import static com.yemiekai.vedio_voice.utils.StringUtils.print;


public class WelcomeActivity extends AppCompatActivity {

    private Activity context = null;
    private boolean bVideoComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        final ConstraintLayout layout = context.findViewById(R.id.welcome_layout);
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
                layout.setBackgroundResource(R.drawable.welcome_image);  // 显示欢迎图片
                vv.setVisibility(View.INVISIBLE);  // 隐藏视频控件
                bVideoComplete = true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        print("event... keyDown");

        if(bVideoComplete){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            context.finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
