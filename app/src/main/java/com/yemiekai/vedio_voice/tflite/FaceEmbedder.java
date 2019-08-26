package com.yemiekai.vedio_voice.tflite;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;
/**
 * 人脸特征器
 * 用.tflite获取人脸特征
 * */
public abstract class FaceEmbedder {

    private static final int DIM_PIXEL_SIZE = 3;

    /** Preallocated buffers for storing image data in. */
    private final int[] intValues = new int[getImageSizeX() * getImageSizeY()];


    /** The loaded TensorFlow Lite model. */
    private MappedByteBuffer tfliteModel;

    /** An instance of the driver class to run model inference with Tensorflow Lite. */
    protected Interpreter tflite;

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs. */
    protected ByteBuffer imgData = null;

    /** Labels corresponding to the output of the vision model. */
    private List<String> labels;

    /** Options for configuring the Interpreter. */
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    /** Initializes a {@code FaceEmbedder}. */
    protected FaceEmbedder(Activity activity, int numThreads) throws IOException {
        tfliteModel = loadModelFile(activity);

        tfliteOptions.setNumThreads(numThreads);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        imgData = ByteBuffer.allocateDirect(
                getBatchSize()
                * getImageSizeX()
                * getImageSizeY()
                * DIM_PIXEL_SIZE
                * getNumBytesPerChannel());
        imgData.order(ByteOrder.nativeOrder());

//        labels = loadLabelList(activity);
    }


    /** Runs inference and returns the embedding. */
    public void getFaceEmbedding(final Bitmap bitmap) {

        convertBitmapToByteBuffer(bitmap);

        long startTime = SystemClock.uptimeMillis();
        runInference();
        long endTime = SystemClock.uptimeMillis();
        debug_print("Timecost to run model inference: " + (endTime - startTime));

        return ;
    }

    /**
     * Creates a classifier with the provided configuration.
     *
     * @param activity The current Activity.
     * @param numThreads The number of threads to use for classification.
     * @return A classifier with the desired configuration.
     */
    public static FaceEmbedder create(Activity activity, int numThreads) throws IOException {
        return new FaceEmbedderMobileNetV3(activity, numThreads);
    }

    /** Writes Image data into a {@code ByteBuffer}. */
    private void  convertBitmapToByteBuffer(Bitmap bitmap) {
        // 检查图像尺寸
        if (bitmap.getWidth() != getImageSizeX() && bitmap.getHeight() != getImageSizeY()){
            debug_print("e", "image size require: " + getImageSizeX() + ", but get:" +bitmap.getWidth());
            return;
        }

        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        long startTime = SystemClock.uptimeMillis();
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
        long endTime = SystemClock.uptimeMillis();
        debug_print("[FaceEmbedder]:" + "Time cost to put values into ByteBuffer: " + (endTime - startTime));
    }

    /** Reads label list from Assets. */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labels = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /** Closes the interpreter and model to release resources. */
    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }

        tfliteModel = null;
    }

    /**
     * Get the image size along the x axis.
     *
     * @return
     */
    public abstract int getImageSizeX();

    /**
     * Get the image size along the y axis.
     *
     * @return
     */
    public abstract int getImageSizeY();

    /**
     * Get the batch size.
     *
     * @return
     */
    public abstract int getBatchSize();

    /**
     * Get the name of the label file stored in Assets.
     *
     * @return
     */
    protected abstract String getLabelPath();

    /**
     * Get the name of the model file stored in Assets.
     *
     * @return
     */
    protected abstract String getModelPath();

    /**
     * Get the total number of labels.
     *
     * @return
     */
    protected int getNumLabels() {
        return labels.size();
    }

    /**
     * Get the number of bytes that is used to store a single color channel value.
     *
     * @return
     */
    protected abstract int getNumBytesPerChannel();

    /**
     * Add pixelValue to byteBuffer.
     *
     * @param pixelValue
     */
    protected abstract void addPixelValue(int pixelValue);

    /**
     * Run inference using the prepared input in {@link #imgData}. Afterwards, the result will be
     * provided by getProbability().
     *
     * <p>This additional method is necessary, because we don't have a common base for different
     * primitive data types.
     */
    protected abstract void runInference();
}
