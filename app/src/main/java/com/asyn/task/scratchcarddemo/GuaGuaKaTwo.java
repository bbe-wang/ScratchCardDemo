package com.asyn.task.scratchcarddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ThinkPad on 2019/11/8.
 *
 * 写字的刮刮卡
 */

public class GuaGuaKaTwo extends View {
    private Paint mOutterPaint;

    private Path mPath;

    private Canvas mCanvas;

    private Bitmap mBitmap;

    private Bitmap mBackBitmap;

    private int mLastX;
    private int mLastY;

    public GuaGuaKaTwo(Context context) {
        super(context);
        init(context);
    }

    public GuaGuaKaTwo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuaGuaKaTwo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        mOutterPaint =new Paint();
        mPath=new Path();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth=getMeasuredWidth();
        int  mHegiht=getMeasuredHeight();
        mBackBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.guagua);
        mBitmap=Bitmap.createBitmap(mWidth,mHegiht, Bitmap.Config.ARGB_8888);
        initPaint();
        /**
         *这里需要注意  这里面又初始化了一个画布，这个画布让透明画笔的运动路径作用到灰色的bitmap上
         */
        mCanvas =new Canvas(mBitmap);
        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
   }

    private void initPaint(){
        mOutterPaint = new Paint();
        mOutterPaint.setAlpha(0);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeWidth(80);
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //这个画布需要去加载两个图片
        canvas.drawBitmap(mBackBitmap,0,0,null);
        canvas.drawBitmap(mBitmap,0,0,null);//这步是为了将灰色的背景添加到这个自定义view上   ====》关联 一下
        mCanvas.drawPath(mPath, mOutterPaint);//使mcanvas承载绘制轨迹的动作
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
