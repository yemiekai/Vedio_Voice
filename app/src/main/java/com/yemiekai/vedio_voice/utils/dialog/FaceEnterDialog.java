package com.yemiekai.vedio_voice.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yemiekai.vedio_voice.R;
import com.yemiekai.vedio_voice.tflite.InsightFace;
import com.yemiekai.vedio_voice.utils.datas.FaceDBDao;
import com.yemiekai.vedio_voice.utils.datas.FaceInfo;

import java.util.Arrays;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class FaceEnterDialog extends Dialog {
    private Context mContext;
    private Activity mActivity;
    private Handler mHandler;
    private ImageView imageView;
    private EditText et_name;
    private EditText et_IDnum;
    private Button bt_cancel;
    private Button bt_confirm;
    private Bitmap bitmap;
    private Bitmap croppedFace;
    private float[] face_embedding;
    private FaceDBDao faceDBDao;

    private FaceInfo faceInfo;
    private int check_count;

    public FaceEnterDialog(Activity activity, Context context, Handler handler, Bitmap bm) {
        this(activity, context, handler, R.style.dialog_custom);
        bitmap = bm;
        croppedFace = Bitmap.createScaledBitmap(bm,224,224, true);
    }

    private FaceEnterDialog(Activity activity, Context context, Handler handler, int theme) {
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

        this.setContentView(R.layout.dialog_face_enter);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCancelable(false);

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*3/5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);

        // 找到组件
        imageView = (ImageView) this.findViewById(R.id.dialog_face_entry_img);
        et_name = (EditText) this.findViewById(R.id.dialog_face_enter_name);
        et_IDnum = (EditText) this.findViewById(R.id.dialog_face_enter_id);
        bt_cancel = (Button) this.findViewById(R.id.dialog_face_entry_cancel);
        bt_confirm = (Button) this.findViewById(R.id.dialog_face_entry_confirm);

        // 显示人脸
        imageView.setImageBitmap(bitmap);
        try {
            // 保存人脸(测试用)
//            ImageUtils.saveBitmap(croppedFace, "yekai.png");

//            // 用tflite文件读取神经网络并且得到512特征
//            FaceEmbedderMobileNetV3 faceEmbedder = new FaceEmbedderMobileNetV3(mActivity, 2);
//            faceEmbedder.getFaceEmbedding(croppedFace);
//            float[][] embdd = faceEmbedder.getEmbeddingArray();
//            debug_print("d","Embedding:\n"+ Arrays.toString(embdd[0]));

            // 用pb文件读取神经网络并且得到512特征
            InsightFace insight = new InsightFace(mActivity.getAssets());
            face_embedding = insight.getFaceEmebeding(croppedFace);
            debug_print("d","Face Embedding:\n"+ Arrays.toString(face_embedding));

        }catch (Exception e){
            debug_print("e",e.toString());
            e.printStackTrace();
        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_count = 0;
                dismiss();
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String IDnum = et_IDnum.getText().toString();

                if(name.length()==0){
                    Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(IDnum.length()==0){
                    Toast.makeText(mContext, "请输入身份证", Toast.LENGTH_SHORT).show();
                    return;
                }

                faceInfo.setName(name);
                faceInfo.setIDnumber(IDnum);
                faceInfo.setFaceBitmap(croppedFace);
                faceInfo.setEmbeddings(face_embedding);
                faceInfo.convert_for_db();

                // 检查是否存在数据库
                if(faceDBDao.checkOne(IDnum)){
                    if(check_count<2) {
                        Toast.makeText(mContext, "身份证重复, 确认2次将覆盖 -- "+check_count, Toast.LENGTH_SHORT).show();
                        check_count++;
                    }else {
                        faceDBDao.updateOrder(faceInfo);
                        Toast.makeText(mContext, "身份证重复, 已覆盖", Toast.LENGTH_LONG).show();
                        check_count = 0;
                        dismiss();
                    }
                    return;
                }else {
                    faceDBDao.insertDate(faceInfo);
                    Toast.makeText(mContext, "录入成功", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
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
