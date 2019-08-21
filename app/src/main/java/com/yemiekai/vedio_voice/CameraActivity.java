package com.yemiekai.vedio_voice;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.yemiekai.vedio_voice.fragments.LegacyCameraFragment;
import com.yemiekai.vedio_voice.utils.tools.Size;

import android.view.Surface;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.yemiekai.vedio_voice.utils.tools.ImageUtils;
import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

/**
 * Android API 19以上应该使用camera2.0
 * 由于机顶盒是Android4.4.2的, 所以用旧接口
 */
public abstract class CameraActivity extends BasicActivity implements Camera.PreviewCallback {

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private Handler handler;
    private HandlerThread handlerThread;

    private Runnable imageConverter;
    private Runnable postInferenceCallback;

    private int[] rgbBytes = null;
    private byte[][] yuvBytes = new byte[3][];
    protected int previewWidth = 0;
    protected int previewHeight = 0;

    private boolean isProcessingFrame = false;
    private int numThreads = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video);

        // 摄像头权限
        if (hasPermission()) {
            setFragment();
        } else {
            requestPermission();
        }

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

    }

    @Override
    public synchronized void onStart() {
        debug_print("d","onStart " + this);
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        debug_print("d","onResume " + this);
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public synchronized void onPause() {
        debug_print("d","onPause " + this);

        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {
            debug_print("e","Exception!");
        }

        super.onPause();
    }

    @Override
    public synchronized void onStop() {
        debug_print("d","onStop " + this);
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        debug_print("d","onDestroy " + this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String[] permissions,
                                           final int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setFragment();
            } else {
                requestPermission();
            }
        }
    }


    /**
     * 为了获得预览帧视频, 要在Activity里继承PreviewCallback
     * 一旦程序调用PreviewCallback接口，就会自动调用onPreviewFrame这个函数*/
    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        if (isProcessingFrame) {
            debug_print("w","Dropping frame!  Processing frame...");
            return;
        }

        try {
            // Initialize the storage bitmaps once when the resolution is known.
            if (rgbBytes == null) {
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                previewHeight = previewSize.height;
                previewWidth = previewSize.width;
                rgbBytes = new int[previewWidth * previewHeight];
                onPreviewSizeChosen(new Size(previewWidth, previewHeight), 0);
            }
        } catch (final Exception e) {
            debug_print("e", "Exception!");
            return;
        }

        isProcessingFrame = true;
        yuvBytes[0] = bytes;

        imageConverter = new Runnable() {
            @Override
            public void run() {
                ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
            }
        };

        postInferenceCallback = new Runnable() {
            @Override
            public void run() {
                camera.addCallbackBuffer(bytes);
                isProcessingFrame = false;
            }
        };

        processImage();
    }


    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    /**
     * 检查摄像头权限
     */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * 请求摄像头权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(
                        CameraActivity.this,
                        "Camera permission is required for this demo",
                        Toast.LENGTH_LONG)
                        .show();
            }
            requestPermissions(new String[] {PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
        }
    }

    protected int getNumThreads() {
        return numThreads;
    }

    private void setNumThreads(int numThreads) {
        if (this.numThreads != numThreads) {
            debug_print("d" ,"Updating  numThreads: " + numThreads);
            this.numThreads = numThreads;
            onInferenceConfigurationChanged();
        }
    }

    protected void setFragment() {
        Fragment fragment = new LegacyCameraFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
        getFragmentManager().beginTransaction().replace(R.id.camera_preview, fragment).commit();
    }

    protected int[] getRgbBytes() {
        imageConverter.run();
        return rgbBytes;
    }

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    protected void readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback.run();
        }
    }

    // 抽象方法
    protected abstract int getLayoutId();
    protected abstract Size getDesiredPreviewFrameSize();
    protected abstract void onPreviewSizeChosen(final Size size, final int rotation);
    protected abstract void processImage();
    protected abstract void onInferenceConfigurationChanged();

}
