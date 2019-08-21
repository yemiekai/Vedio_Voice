package com.yemiekai.vedio_voice.utils.tools;

import android.util.Log;

public class StringUtils {
    public static void debug_print(String s){
        Log.d("yekai",s);
    }

    public static void debug_print(int i){
        Log.d("yekai",""+i);
    }

    public static void debug_print(String level, String s){
        switch (level){
            case "d":
            case "D":
                Log.d("yekai",s);
                break;
            case "v":
            case "V":
                Log.v("yekai",s);
                break;
            case "i":
            case "I":
                Log.i("yekai",s);
                break;
            case "w":
            case "W":
                Log.w("yekai",s);
                break;
            case "e":
            case "E":
                Log.e("yekai",s);
                break;
            default:
                Log.d("yekai",s);
        }
    }

}

