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

    private FaceEnterDialog faceEnterDialog;
    private FaceEmbedder faceEmbedder;
    private Button bt_shoot;
    private Camera cameraObject;
    private ShowCamera showCamera;
    private FrameLayout preview;
    private Context mContext;
    private Activity mActivity;
    private Bitmap face;

    MTCNN mtcnn;


    final Handler mhandler = new Handler() {
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
                    MTCNNUtils.drawRect(bitmapDetected, boxes.get(i).transform2Rect());  // 画矩形
                }

                // 只拿一个脸
                Box faceBox = boxes.get(0);
                face = Bitmap.createBitmap(bitmapOrigin, faceBox.left(), faceBox.top(), faceBox.width(), faceBox.height());

                // 显示对话框
                faceEnterDialog = new FaceEnterDialog(mActivity, mContext, face);
                sendMsgInner(DISPLAY_ENTRY_DIALOG);

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "检测失败", Toast.LENGTH_SHORT).show();
                return;
            }

            preview.removeAllViews();
            preview.addView(new FacePictureView(mContext, bitmapDetected, 800, 600));
            cameraObject.release();


        }
    };



//    @Override
//    protected void onResume() {
//        super.onResume();
//        preview.removeAllViews();
//        preview.addView(showCamera);
//        bt_shoot.setVisibility(View.VISIBLE);
//        bt_shoot.setEnabled(true);
//    }

    // 点击了拍照
    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
        bt_shoot.setVisibility(View.INVISIBLE);
        bt_shoot.setEnabled(false);
    }

    // MTCNN检测人脸
    public void processMTCNNImage(Bitmap bitmap){
        Bitmap bm = MTCNNUtils.copyBitmap(bitmap);
        try {
            Vector<Box> boxes=mtcnn.detectFaces(bm,100);  // 检测人脸
            for (int i=0;i<boxes.size();i++){
                MTCNNUtils.drawRect(bm, boxes.get(i).transform2Rect());  // 画矩形
                MTCNNUtils.drawPoints(bm,boxes.get(i).landmark);  //画点
            }
        }catch (Exception e){
            debug_print("[*]detect false:"+e);
        }
    }

    // 保存照片
    public void saveBitmap(Bitmap bitmap){
        String fileName = Environment.getExternalStorageDirectory().toString()
                +File.separator
                +"AppTest"
                + File.separator
                +"PicTest_"+System.currentTimeMillis()+".jpg";
        File file = new File(fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();//创建文件夹
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
            bos.flush();
            bos.close();

            Toast.makeText(getApplicationContext(), "拍照成功，照片保存在"+fileName+"文件之中！", Toast.LENGTH_LONG).show();
            debug_print("拍照成功，照片保存在"+fileName+"文件中");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            Toast.makeText(getApplicationContext(), "拍照失败！"+e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void sendMsgInner(int what) {
        Message msg = new Message();
        msg.what = what;
        mhandler.sendMessage(msg);
    }

}
