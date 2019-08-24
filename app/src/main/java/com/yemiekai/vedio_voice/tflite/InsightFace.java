package com.yemiekai.vedio_voice.tflite;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class InsightFace {
    private static final String MODEL_FILE  = "file:///android_asset/MobileNetV3_InsightFace_frozen.pb";
    private static final String InName  = "import/inputs:0";
    private static final String OutName = "import/Logits_out/output:0";

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
            debug_print("d","load model success");
        }catch(Exception e){
            debug_print("d","load model failed " + e);
            return false;
        }
        return true;
    }
}
