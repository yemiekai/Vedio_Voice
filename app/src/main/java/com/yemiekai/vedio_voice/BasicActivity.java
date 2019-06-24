package com.yemiekai.vedio_voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.yemiekai.vedio_voice.services.MyNetworkService;
import com.yemiekai.vedio_voice.utils.tools.StringUtils;

public class BasicActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void DialogPrompt(Context context, String title, String msg) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.bn_confirm, null)
                .show();
    }

    public static void screenInformation(Context context){
        DisplayMetrics metric = context.getResources().getDisplayMetrics();

        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX）

        float xdpi = context.getResources().getDisplayMetrics().xdpi;
        float ydpi = context.getResources().getDisplayMetrics().ydpi;

        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）


        String info = String.format(" \nWidth:%dpx %.0fdpi\nHeight:%dpx %.0fdpi\nDensity:%.2f\nDensity dpi:%ddpi",
                width, xdpi, height, ydpi, density, densityDpi);
        StringUtils.debug_print(info);
    }

}
