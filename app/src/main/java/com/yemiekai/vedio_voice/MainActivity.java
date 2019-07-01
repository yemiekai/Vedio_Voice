package com.yemiekai.vedio_voice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yemiekai.vedio_voice.services.MyNetworkService;
import com.yemiekai.vedio_voice.utils.datas.MyRecyclerButton;
import com.yemiekai.vedio_voice.utils.tools.MyNetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// 视频用法 : https://juejin.im/post/5cd2bf945188251a4442a869
// 视频框架 : https://blog.csdn.net/u010181592/article/details/49301703
// 视频轮播 : https://blog.csdn.net/watts2008/article/details/52710193
// 图片轮播： https://www.jb51.net/article/101517.htm

// RecyclerView: https://blog.csdn.net/qq_37217804/article/details/80178583
//               https://www.jianshu.com/p/80083550e9b0
//               https://blog.csdn.net/qq_34801506/article/details/80538944    (响应按键)
//               https://www.jianshu.com/p/4f9591291365   (这篇内容太多看不来)
public class MainActivity extends BasicActivity {
    public final static int MSG_CHANGE_IMAGE             = 1000;       // 切换图片
    public final static int MSG_ZOOM_OUT_IMAGE           = 1001;       // 淡出

    public final static int TIME_IMAGE_ZOOM_OUT          = 700;        // 切换图片时,图片淡出动画时长

    private Context context;
    private MyNetworkUtils myNetworkUtils;
    private VideoView video;
    private ImageView image;
    private RecyclerView recyclerView;
    private int[] imageResIds;
    private ArrayList<MyRecyclerButton> buttonList = new ArrayList<>();

    TextView date;
    TextView time;
    Timer timeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenInformation(this);

        context = MainActivity.this;

        // 启动Service, 用于网络通信
        myNetworkUtils = MyNetworkUtils.getInstance(MainActivity.this);
        myNetworkUtils.startNetworkService();

        setContentView(R.layout.activity_main);

        date = (TextView) findViewById(R.id.main_date);
        time = (TextView) findViewById(R.id.main_time);
        video = (VideoView) findViewById(R.id.main_video);
        image = (ImageView) findViewById(R.id.main_image);

        // 按钮栏
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);

        init_dateTime();   // 刷新时间日期
        init_pictures();  // 轮播图片
        init_buttons();  // 可滚动显示的按钮
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_video();  // 轮播视频
    }

    // 刷新时间日期
    private void init_dateTime(){
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
    }

    // 轮播视频
    private void init_video(){
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

    // 轮播图片
    private void init_pictures(){
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

                    // 暂停一下(展示时长)
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 发送消息, 图片淡出
                    Message msg_zoom_out = new Message();
                    msg_zoom_out.what = MSG_ZOOM_OUT_IMAGE;
                    handler.sendMessage(msg_zoom_out);

                    // 暂停一下, 图片淡出
                    try {
                        Thread.sleep(TIME_IMAGE_ZOOM_OUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // 可滚动显示的按钮
    private void init_buttons(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);  // 线性
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);  // 横向
        recyclerView.setLayoutManager(layoutManager);

        // 添加按钮
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_tv), R.drawable.tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LiveTVActivity.class);
                startActivity(intent);
            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_video), R.drawable.video, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DemandVideoActivity.class);
                startActivity(intent);
            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_navigation), R.drawable.navigation, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NavigationActivity.class);
                startActivity(intent);
            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_doctor), R.drawable.doctor, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorActivity.class);
                startActivity(intent);
            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_service), R.drawable.service, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /***
                 * todo
                 * 暂时用这个按键测试一下services的功能
                 */
//                myNetworkUtils.sayHello(123);
                myNetworkUtils.testHTTP();

            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_introduce), R.drawable.introduce, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IntroduceActivity.class);
                startActivity(intent);
            }
        }));
        buttonList.add(new MyRecyclerButton(getString(R.string.main_bt_ai), R.drawable.ai, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AiActivity.class);
                startActivity(intent);
            }
        }));

        recyclerView.setAdapter(new MyButtonAdapter(context, buttonList));
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

    public class MyButtonAdapter extends RecyclerView.Adapter<MyButtonAdapter.ViewHolder> {

        //该值仅仅为了传递Activity
        private Context context;
        ArrayList<MyRecyclerButton> buttonList;

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton mButton;

            public ViewHolder(View itemView) {
                super(itemView);
                mButton = itemView.findViewById(R.id.main_recycler_button);
            }
        }

        public MyButtonAdapter(Context context, ArrayList<MyRecyclerButton> buttonList){
            this.context = context;
            this.buttonList = buttonList;
        }

        @Override
        public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_buttons,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MyRecyclerButton buttonInfo = buttonList.get(position);
            holder.mButton.setImageResource(buttonInfo.getImageSrcResId());  // 设置按钮图片
            holder.mButton.setOnClickListener(buttonInfo.getClickListener());  // 设置按钮监听

            if(position==0){
                holder.mButton.requestFocus();
            }
        }

        @Override
        public int getItemCount() {
            return buttonList.size();
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

    }
}
