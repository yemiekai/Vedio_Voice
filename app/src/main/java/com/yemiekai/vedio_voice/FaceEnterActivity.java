package com.yemiekai.vedio_voice;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import com.yemiekai.vedio_voice.tflite.FaceEmbedder;
import com.yemiekai.vedio_voice.tflite.MTCNN;
import com.yemiekai.vedio_voice.utils.dialog.FaceEnterDialog;
import com.yemiekai.vedio_voice.utils.tools.Box;
import com.yemiekai.vedio_voice.utils.tools.MTCNNUtils;
import com.yemiekai.vedio_voice.utils.views.FacePictureView;
import com.yemiekai.vedio_voice.utils.views.ShowCamera;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class FaceEnterActivity extends BasicActivity {
    public final static int DISPLAY_ENTRY_DIALOG             = 1000;  // 显示录入人脸对话框
    public final static int DISPLAY_PLEASE_ENTER_NAME        = 1001;  // 显示"请输入姓名"

    private FaceEnterDialog faceEnterDialog;
    private FaceEmbedder faceEmbedder;
    private Button bt_shoot;
    private Camera cameraObject;
    private ShowCamera showCamera;
    private FrameLayout preview;
    private Context mContext;
    private Activity mActivity;
    private Bitmap bitmap_face;

    MTCNN mtcnn;

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case DISPLAY_ENTRY_DIALOG://显示进度对话框
                    if(faceEnterDialog != null){
                        faceEnterDialog.dismiss();
                    }
                    faceEnterDialog.show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mContext = this;
        mActivity = this;
        mtcnn = new MTCNN(getAssets());
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        bt_shoot = (Button) findViewById(R.id.button_capture);

        cameraObject = isCameraAvailiable();

        showCamera = new ShowCamera(this, cameraObject);
        preview.removeAllViews();
        preview.addView(showCamera);
    }


    public static Camera isCameraAvailiable(){
        Camera object = null;
        try {
            object = Camera.open();
        } catch (Exception e){
            e.printStackTrace();
            object = null;
        }
        return object;
    }

    // 点击了“拍摄”
    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 获取bitmap
            Bitmap bitmapOrigin= BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmapOrigin == null) {
                Toast.makeText(getApplicationContext(), "检测失败", Toast.LENGTH_SHORT).show();
                return;
            }

            // 检测人脸
            Bitmap bitmapDetected = MTCNNUtils.copyBitmap(bitmapOrigin);
            try {
                Vector<Box> boxes = mtcnn.detectFaces(bitmapDetected,100);
                for (int i=0;i<boxes.size();i++){
                    // 画矩形, 框出人脸
                    MTCNNUtils.drawRect(bitmapDetected, boxes.get(i).transform2Rect());
                }

                // 只拿一个脸
                Box faceBox = boxes.get(0);
                bitmap_face = Bitmap.createBitmap(bitmapOrigin, faceBox.left(), faceBox.top(), faceBox.width(), faceBox.height());

                // 显示对话框, 开始录入信息
                faceEnterDialog = new FaceEnterDialog(mActivity, mContext, mHandler, bitmap_face);
                sendMsgInner(DISPLAY_ENTRY_DIALOG);

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "检测失败", Toast.LENGTH_SHORT).show();
                return;
            }

            // 展示有人脸框的图片, 并且停止视频
            preview.removeAllViews();
            preview.addView(new FacePictureView(mContext, bitmapDetected, 800, 600));
            cameraObject.release();


        }
    };


    // 点击了拍照
    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
        bt_shoot.setVisibility(View.INVISIBLE);
        bt_shoot.setEnabled(false);
    }


    public void sendMsgInner(int what) {
        Message msg = new Message();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

}
