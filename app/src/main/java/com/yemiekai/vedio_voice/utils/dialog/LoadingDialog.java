package com.yemiekai.vedio_voice.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.yemiekai.vedio_voice.R;

import org.w3c.dom.Text;

public class LoadingDialog extends Dialog {
    TextView tv_title;
    private String title;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public LoadingDialog(Context context) {
        super(context);
        this.title = null;
        // TODO Auto-generated constructor stub

    }

    public LoadingDialog(Context context, String title) {
        super(context);
        this.title = title;
        // TODO Auto-generated constructor stub

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        tv_title = (TextView) findViewById(R.id.dialog_loading_title);
        if(this.title != null){
            tv_title.setText(this.title);
        }
    }

}