package com.yemiekai.vedio_voice.utils.views;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

// 这个是用来填充FrameLayout的, 其实就是为了显示一张图片
public class FacePictureView extends View {
    private Bitmap bitmap;
    private int width;
    private int height;
    private RectF bound;
    private Paint paint;

    public FacePictureView(Context context, Bitmap bm, int width, int height) {
        super(context);
        this.bitmap = bm;
        this.width = width;
        this.height = height;
        this.bound = new RectF(0, 0, width, height);
        this.paint = new Paint();
    }

    public FacePictureView(Context context, int width, int height) {
        super(context);
        this.bitmap = null;
        this.width = width;
        this.height = height;
        this.bound = new RectF(0, 0, width, height);
        this.paint = new Paint();
    }
    //重写View类的onDraw()方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制
        canvas.drawBitmap(bitmap, null, bound, paint);

        //判断图片是否回收,木有回收的话强制收回图片
        if(bitmap.isRecycled())
        {
            bitmap.recycle();
        }
    }
}
