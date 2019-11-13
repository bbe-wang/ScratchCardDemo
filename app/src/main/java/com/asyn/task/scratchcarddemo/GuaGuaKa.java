package com.asyn.task.scratchcarddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ThinkPad on 2019/11/8.
 *
 * 写字的刮刮卡
 */

public class GuaGuaKa extends View {
    private Paint mOutterPaint;

    private Path mPath;

    private Canvas mCanvas;

    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    public GuaGuaKa(Context context) {
        super(context);
        init(context);
    }

    public GuaGuaKa(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuaGuaKa(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        mOutterPaint =new Paint();
        mPath=new Path();
        setLayerType(View.LAYER_TYPE_SOFTWARE,mOutterPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        int mWidth=MeasureSpec.getSize(widthMeasureSpec);
        int  mHegiht=MeasureSpec.getSize(heightMeasureSpec);
        mBitmap=Bitmap.createBitmap(mWidth,mHegiht, Bitmap.Config.ARGB_8888);
        mCanvas =new Canvas(mBitmap);
        initPaint();

    }


    private void initPaint(){
        mOutterPaint = new Paint();
        // 设置画笔
        mOutterPaint.setColor(Color.RED);
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //沿着诡计
        canvas.drawPath(mPath,mOutterPaint);
        canvas.drawBitmap(mBitmap,0,0,null);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastX=x;
                mLastY=y;
                mPath.moveTo(mLastX,mLastY);

                break;
            case  MotionEvent.ACTION_MOVE:
                int dx= Math.abs(x - mLastX);

                int dy= Math.abs(y - mLastY);

                if (dx >3 || dy>3)

                    mPath.lineTo(x ,y);

                mLastX=dx;
                mLastY=dy;

                break;
        }
        invalidate();

        return true;

    }




}
