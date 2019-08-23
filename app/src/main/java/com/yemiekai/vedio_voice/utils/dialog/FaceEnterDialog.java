package com.yemiekai.vedio_voice.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yemiekai.vedio_voice.R;
import com.yemiekai.vedio_voice.tflite.FaceEmbedderMobileNetV3;

import java.util.Arrays;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class FaceEnterDialog extends Dialog {
    private Context mContext;
    private Activity mActivity;
    private ImageView imageView;
    private EditText editText;
    private Button bt_cancel;
    private Button bt_confirm;
    private Bitmap bitmap;
    private Bitmap face;

    public FaceEnterDialog(Activity activity, Context context, Bitmap bm) {
        this(activity, context, R.style.dialog_custom);
        bitmap = bm;
        face = Bitmap.createScaledBitmap(bm,224,224, true);
    }

    private FaceEnterDialog(Activity activity, Context context, int theme) {
        super(context, theme);
        mContext = context;
        mActivity = activity;
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
        editText = (EditText) this.findViewById(R.id.dialog_face_entry_name);
        bt_cancel = (Button) this.findViewById(R.id.dialog_face_entry_cancel);
        bt_confirm = (Button) this.findViewById(R.id.dialog_face_entry_confirm);

        // 显示人脸
        imageView.setImageBitmap(bitmap);
        //                try {
//                    FaceEmbedderMobileNetV3 faceEmbedder = new FaceEmbedderMobileNetV3(context, 2);
//                    faceEmbedder.getFaceEmbedding(croppedBitmap);
//                }catch (Exception e){
//                    debug_print("e",e.toString());
//                }
        try {
            FaceEmbedderMobileNetV3 faceEmbedder = new FaceEmbedderMobileNetV3(mActivity, 2);
            faceEmbedder.getFaceEmbedding(face);
            float[][] embdd = faceEmbedder.getEmbeddingArray();
            debug_print("d","Embedding:\n"+ Arrays.toString(embdd[0]));

        }catch (Exception e){
            debug_print("e",e.toString());
            e.printStackTrace();
        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();

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
