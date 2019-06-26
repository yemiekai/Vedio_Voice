package com.yemiekai.vedio_voice;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.yemiekai.vedio_voice.utils.views.ShowCamera;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class VideoActivity extends BasicActivity {
    private Camera cameraObject;
    private ShowCamera showCamera;

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

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }

            //保存照片
            String fileName= Environment.getExternalStorageDirectory().toString()
                    +File.separator
                    +"AppTest"
                    + File.separator
                    +"PicTest_"+System.currentTimeMillis()+".jpg";
            File file=new File(fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();//创建文件夹
            }
            try {
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
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


            cameraObject.release();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
    }

    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }

    private void test(){
        Date date = new Date();
        DateFormat df1 = DateFormat.getDateInstance();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

}
