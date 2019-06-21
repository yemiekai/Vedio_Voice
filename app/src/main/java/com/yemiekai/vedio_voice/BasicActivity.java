package com.yemiekai.vedio_voice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.yemiekai.vedio_voice.utils.tools.StringUtils;
import com.yemiekai.vedio_voice.utils.tools.StringUtils.*;

public class BasicActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenInformation(this);
    }

    public static void DialogPrompt(Context context, String title, String msg) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.bn_confirm, null)
                .show();
    }

    public void ScreenInformation(Context context){
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);

        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX）

        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）

        String info = String.format(" \nWidth:%dpx\nHeight:%dpx\nDensity:%.2f\nDensity dpi:%ddpi",
                width, height, density, densityDpi);
//        String infos = "Width:" + width + "px\n" + "Height:" + height + "px\n" +
//                "Density:" + density + "\n" + "Density dpi:" + densityDpi + "dpi";
        StringUtils.print(info);
    }

}
