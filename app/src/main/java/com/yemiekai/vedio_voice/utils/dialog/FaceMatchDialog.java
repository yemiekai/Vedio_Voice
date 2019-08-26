package com.yemiekai.vedio_voice.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yemiekai.vedio_voice.R;
import com.yemiekai.vedio_voice.tflite.InsightFace;
import com.yemiekai.vedio_voice.utils.datas.FaceCompare;
import com.yemiekai.vedio_voice.utils.datas.FaceCompare.SortByAngle;
import com.yemiekai.vedio_voice.utils.datas.FaceDBDao;
import com.yemiekai.vedio_voice.utils.datas.FaceInfo;
import com.yemiekai.vedio_voice.utils.tools.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class FaceMatchDialog extends Dialog {
    private Context mContext;
    private Activity mActivity;
    private Handler mHandler;
    private ImageView imageView;
    private TextView tv_name;
    
    private TextView tv_IDnum;
    
    private Button bt_confirm;
    private Bitmap bitmap;
    private Bitmap croppedFace;
    private float[] face_embedding;
    private FaceDBDao faceDBDao;

    private FaceInfo faceInfo;
    private int check_count;

    public FaceMatchDialog(Activity activity, Context context, Handler handler, Bitmap bm) {
        this(activity, context, handler, R.style.dialog_custom);
        bitmap = bm;
        croppedFace = Bitmap.createScaledBitmap(bm,224,224, true);
    }

    private FaceMatchDialog(Activity activity, Context context, Handler handler, int theme) {
        super(context, theme);
        mContext = context;
        mActivity = activity;
        mHandler = handler;
        check_count = 0;
        faceInfo = new FaceInfo();
        faceDBDao = new FaceDBDao(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.dialog_face_match);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCancelable(false);

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*3/5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);

        // 找到组件
        imageView = (ImageView) this.findViewById(R.id.dialog_face_match_img);
        tv_name = (TextView) this.findViewById(R.id.dialog_face_match_name);
        tv_IDnum = (TextView) this.findViewById(R.id.dialog_face_match_id);
        bt_confirm = (Button) this.findViewById(R.id.dialog_face_match_confirm);

        // 显示人脸
        imageView.setImageBitmap(bitmap);

        Toast.makeText(mContext, "正在匹配人脸...", Toast.LENGTH_SHORT).show();
        try {
            // 用pb文件读取神经网络并且得到512特征
            InsightFace insight = new InsightFace(mActivity.getAssets());
            face_embedding = insight.getFaceEmebeding(croppedFace);
            debug_print("d","Face Embedding:\n"+ Arrays.toString(face_embedding));
        }catch (Exception e){
            debug_print("e",e.toString());
            e.printStackTrace();
        }


        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_name.setText("");
                tv_IDnum.setText("");
                dismiss();
            }
        });

        // 找到最接近的人脸
        FaceCompare closelyFace = find_face(face_embedding);
        if(closelyFace==null){
            Toast.makeText(mContext, "没有匹配", Toast.LENGTH_LONG).show();
        }else {
            tv_name.setText(closelyFace.getName());
            tv_IDnum.setText(closelyFace.getIDnumber());
            Toast.makeText(mContext, "匹配成功", Toast.LENGTH_LONG).show();
        }

    }

    private FaceCompare find_face(float[] faceEmbedding){
        if(faceEmbedding==null){
            return null;
        }

        // 从数据库获取所有人脸
        List<FaceInfo> faceDBList = faceDBDao.getAllDate();
        if(faceDBList==null || faceDBList.size()==0){
            return null;
        }
        for(FaceInfo face:faceDBList) {
            debug_print(face.toString()); // Logcat看看数据库情况
        }

        // 逐个计算与faceEmbedding的夹角
        ArrayList<FaceCompare> faceCompares = new ArrayList<>();
        for(FaceInfo faceInfo:faceDBList){
            double angle = Math.abs(InsightFace.cacualte_vector_angle(faceEmbedding, faceInfo.getEmbeddings()));
            faceCompares.add(new FaceCompare(faceInfo.getName(), faceInfo.getIDnumber(), angle));
        }

        // 排序
        Collections.sort(faceCompares, new SortByAngle());
        for(FaceCompare face:faceCompares){
            debug_print("d", face.toString()); // Logcat看看排序情况
        }

        // 获取最接近的脸
        FaceCompare closelyFace = faceCompares.get(0);
        debug_print(MathUtils.compareDoubles(closelyFace.getAngle(), InsightFace.getThreshold()));
        if(MathUtils.compareDoubles(closelyFace.getAngle(), InsightFace.getThreshold()) == 1){
            return null;  // 大于阈值, 不行
        }else {
            return closelyFace;
        }

    }
//    @Override
//    protected void (Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
//        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
//        setContentView(layoutResID);
//
//        WindowManager windowManager = ((Activity) context).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
//        getWindow().setAttributes(lp);
//        setCanceledOnTouchOutside(true);// 点击Dialog外部消失
//        //遍历控件id,添加点击事件
//        for (int id : listenedItems) {
//            findViewById(id).setOnClickListener(this);
//        }
//    }
}
