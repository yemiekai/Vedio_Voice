package com.yemiekai.vedio_voice.tflite;

import android.app.Activity;

import java.io.IOException;

public class FaceEmbedderMobileNetV3 extends FaceEmbedder {

    /** MobileNet requires additional normalization of the used input. */
    private static final float IMAGE_MEAN = 127.5f;
    private static final float IMAGE_STD = 127.5f;

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
     * of the super class, because we need a primitive array here.
     */
    private float[][] embeddingArray = null;

    /**
     * Initializes a {@code ClassifierFloatMobileNet}.
     *
     * @param activity
     */
    public FaceEmbedderMobileNetV3(Activity activity, int numThreads) throws IOException {
        super(activity, numThreads);
        embeddingArray = new float[1][getNumLabels()];
    }

    @Override
    public int getImageSizeX() {
        return 224;
    }

    @Override
    public int getImageSizeY() {
        return 224;
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    protected String getLabelPath() {
        return "labels.txt";
    }

    @Override
    protected String getModelPath() {
        // you can download this file from
        // see build.gradle for where to obtain this file. It should be auto
        // downloaded into assets.
        return "mobilenet_v1_1.0_224.tflite";
    }


    @Override
    protected int getNumBytesPerChannel() {
        return 4; // Float.SIZE / Byte.SIZE;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    }

    @Override
    protected void runInference() {
        tflite.run(imgData, embeddingArray);
    }
}
