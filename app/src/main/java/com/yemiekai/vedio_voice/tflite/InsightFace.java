package com.yemiekai.vedio_voice.tflite;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.yemiekai.vedio_voice.utils.tools.ImageUtils;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

/*
这个是直接用.pb文件来跑
 */
public class InsightFace {
    private static final String MODEL_FILE  = "file:///android_asset/MobileNetV3_InsightFace_frozen_100000.pb";
    private static final String InName  = "inputs:0";
    private static final String[] OutName = new String[]{"Logits_out/output:0"};

    private static final int ImageSize = 224;  // 网络输入尺寸
    private static final double threshold = 0.302868;  // 人脸对比阈值

    private InsightFace myself;

    public  long lastProcessTime;   //最后一张图片处理的时间ms
    private AssetManager assetManager;
    private TensorFlowInferenceInterface inferenceInterface;

    public InsightFace(AssetManager mgr){
        assetManager = mgr;
        loadModel();
    }

    private boolean loadModel() {
        //AssetManager
        try {
            inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
            debug_print("d","load InsightFace model success");
        }catch(Exception e){
            debug_print("d","load InsightFace model failed " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 获取人脸特征
    public float[] getFaceEmebeding(Bitmap face){

        float[] embedding = new float[512];
        float[] networkInput = ImageUtils.normalizeImage(face);

        inferenceInterface.feed(InName,networkInput,1,ImageSize,ImageSize,3);
        inferenceInterface.run(OutName,false);

        inferenceInterface.fetch(OutName[0], embedding);
        return embedding;
    }

    // 向量点积
    public static double vector_dot_multiply(float[] v1, float[] v2){
        if(v1.length != v2.length){
            return 0;
        }

        float result = 0;
        for(int i = 0; i < v1.length; i++)
        {
            result += v1[i]*v2[i];
        }

        return Double.parseDouble(String.valueOf(result));
    }

    // 向量的模
    public static double vector_norm(float[] v){
        float summary = 0;
        for(float f : v)
        {
            summary += f*f;
        }
        return Math.sqrt(Double.parseDouble(String.valueOf(summary)));
    }



}
