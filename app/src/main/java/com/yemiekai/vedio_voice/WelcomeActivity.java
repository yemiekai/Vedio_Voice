package com.yemiekai.vedio_voice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class WelcomeActivity extends AppCompatActivity {

    public static Activity context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);


        final VideoView vv = (VideoView) this.findViewById(R.id.welcome_video);

        final String uri = "android.resource://" + getPackageName() + "/" + R.raw.welcome_vedio;
        vv.setVideoURI(Uri.parse(uri));
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
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                context.finish();
            }
        });
    }
}
