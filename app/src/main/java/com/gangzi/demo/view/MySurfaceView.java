package com.gangzi.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MySurfaceView extends SurfaceView {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mSurfaceHolder=getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //surface创建时
                Log.i("TEST","surfaceCreated");
                draw();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {
                //surface大小发生变化
                Log.i("TEST","surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //surface销毁
                Log.i("TEST","surfaceDestroyed");
            }
        });
    }
    private void draw(){
        Canvas canvas=mSurfaceHolder.lockCanvas();//在Surface上绘制
        if (canvas!=null){
            canvas.drawCircle(100,100,50,mPaint);
            mSurfaceHolder.unlockCanvasAndPost(canvas);//呈现到前台
        }
    }
}
