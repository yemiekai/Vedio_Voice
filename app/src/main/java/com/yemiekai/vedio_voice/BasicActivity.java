package com.yemiekai.vedio_voice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

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


}
