package com.gangzi.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gangzi.demo.R;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MyView extends View implements Runnable{

    private Paint mPaint;//画笔
    private Bitmap mBitmap;
    private int x;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private boolean flag;

    /**
     * 以代码的方式动态添加view到容器中使用
     * @param context
     */
    public MyView(Context context) {
        super(context);
        init();
    }

    /**
     * 以XML布局文件的方式使用时，自动调用
     * @param context
     * @param attrs
     */
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);//单线条
        //mPaint.setStyle(Paint.Style.FILL);//填充
        mPaint.setStrokeWidth(3);//线条的宽度
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.banana);
        mBitmap=Bitmap.createScaledBitmap(mBitmap,100,100,true);//创建缩放的bitmap
    }

    /**
     * 当view需要呈现出来的时候自动调用
     *canvas 画布
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(50,50,50,mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(10,120,110,170,mPaint);
        canvas.drawBitmap(mBitmap,10,200,null);
        super.onDraw(canvas);
        moveBackground(canvas);
    }

    private void moveBackground(Canvas canvas) {
        x-=10;
        int x2=mBitmap.getWidth()-(-x);
        if (x2<=0){
            x=0;
            canvas.drawBitmap(mBitmap,x,10,null);
        }else{
            canvas.drawBitmap(mBitmap,x,10,null);
            canvas.drawBitmap(mBitmap,x2,10,null);
        }
    }

    @Override
    public void run() {
        while (flag){
            //不可以直接调用onDraw（）方法
            postInvalidate();//在线程中要求view重新呈现
        }
    }
}
