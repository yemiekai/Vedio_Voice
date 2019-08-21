package com.yemiekai.vedio_voice.utils.views;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yemiekai.vedio_voice.utils.tools.ImageUtils;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holdMe;
    private Camera theCamera;
    private Bitmap rgbFrameBitmap = null;
    protected int previewWidth = 0;
    protected int previewHeight = 0;
    private int[] rgbBytes = null;
//    private Camera.Size previewSize;
//    private byte[] mPreBuffer;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        theCamera = camera;
//        previewSize = theCamera.getParameters().getPreviewSize();
//        mPreBuffer = new byte[previewSize.width * previewSize.height];
        holdMe = getHolder();
        holdMe.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        }
        catch (IOException e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }


}
