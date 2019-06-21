package com.yemiekai.vedio_voice.utils.datas;

import android.view.View;

/**
 * 用于MainActivity的RecyclerView
 *
 */
public class MyRecyclerButton {
    private String name;
    private int imageSrcResId;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public MyRecyclerButton(String name, int imageSrcResId) {
        this.name = name;
        this.imageSrcResId = imageSrcResId;
    }

    public MyRecyclerButton(String name, int imageSrcResId, View.OnClickListener clickListener) {
        this.name = name;
        this.imageSrcResId = imageSrcResId;
        this.clickListener = clickListener;
    }

    public String getName() {
        return name;
    }

    public int getImageSrcResId() {
        return imageSrcResId;
    }

    public View.OnClickListener getClickListener(){
        return clickListener;
    }
}
