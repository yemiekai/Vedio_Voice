package com.yemiekai.vedio_voice;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.FaceDetector;
import android.os.SystemClock;
import android.util.TypedValue;
import android.widget.Toast;

import com.yemiekai.vedio_voice.tflite.FaceEmbedder;
import com.yemiekai.vedio_voice.utils.tools.BorderedText;
import com.yemiekai.vedio_voice.utils.tools.ImageUtils;
import com.yemiekai.vedio_voice.utils.tools.Size;
import com.yemiekai.vedio_voice.utils.tools.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

/**
 * 这个类继承了CameraActivity, 实现了CameraActivity里的抽象方法
 *
 * 程序逻辑可以直接看CameraActivity里的方法
 */
public class VideoFaceActivity extends CameraActivity {

    private static final Size DESIRED_PREVIEW_SIZE = new Size(800, 600);
    private static final float TEXT_SIZE_DIP = 10;
    private static final boolean MAINTAIN_ASPECT = true;

    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private BorderedText borderedText;
    private FaceEmbedder faceEmbedder;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private Integer sensorOrientation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                           TEXT_SIZE_DIP,
                                                           getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        recreateFaceEmbedder(getNumThreads());
        if (faceEmbedder == null) {
            debug_print("e", "No faceEmbedder on preview!");
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        debug_print("i", String.format(Locale.CHINA,"Camera orientation relative to screen canvas: %d", sensorOrientation));

        debug_print("i", String.format(Locale.CHINA,"Initializing at size %dx%d", previewWidth, previewHeight));
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(faceEmbedder.getImageSizeX(), faceEmbedder.getImageSizeY(), Bitmap.Config.ARGB_8888);

        frameToCropTransform = ImageUtils.getTransformationMatrix(
                previewWidth,
                previewHeight,
                faceEmbedder.getImageSizeX(),
                faceEmbedder.getImageSizeY(),
                sensorOrientation,
                MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);
    }

    @Override
    protected void processImage() {
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        if (faceEmbedder != null) {
                            final long startTime = SystemClock.uptimeMillis();
                            faceEmbedder.getFaceEmbedding(croppedBitmap);
                            long lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                            debug_print("v", String.format(Locale.CHINA,"Detect times: %d ms", lastProcessingTimeMs));
                            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);

                            runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                            });
                        }
                        readyForNextImage();
                    }
                });
    }

    private void recreateFaceEmbedder(int numThreads) {
        if (faceEmbedder != null) {
            debug_print("Closing faceEmbedder.");
            faceEmbedder.close();
            faceEmbedder = null;
        }

        try {
            debug_print(String.format(Locale.CHINA,"Creating classifier (numThreads=%d)", numThreads));
            faceEmbedder = FaceEmbedder.create(this, numThreads);
        } catch (IOException e) {
            debug_print("e", "Failed to create classifier.");
        }
    }

    @Override
    protected void onInferenceConfigurationChanged() {
        if (croppedBitmap == null) {
            // Defer creation until we're getting camera frames.
            return;
        }
        final int numThreads = getNumThreads();
        runInBackground(new Runnable() {
            @Override
            public void run() {
                recreateFaceEmbedder(numThreads);
            }
        });
    }
}
