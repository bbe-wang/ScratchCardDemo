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
 * Created by ThinkPad on 2019/11/12.
 */

public class GuaGuaKaLiPing extends View{
   private Paint mPaint;

    private Canvas mCanvas;

    private Bitmap mTxt,mSrc;


    private Bitmap mDex;

    private float mLastX,mLastY;

    private Path mPath;

    public GuaGuaKaLiPing(Context context) {
        super(context);
      //  init();
    }

    public GuaGuaKaLiPing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       // init();
    }

    public GuaGuaKaLiPing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }


    private void init(){
        //禁止硬件加速
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //初始化图片对象
        mTxt = BitmapFactory.decodeResource(getResources(), R.drawable.result);
        mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.eraser);
        /**
         *mSrc.getWidth()  取图片的宽   取图片的高  正好可以盖住
         */
        mDex = Bitmap.createBitmap(mSrc.getWidth(), mSrc.getHeight(), Bitmap.Config.ARGB_8888);
        //路径（贝塞尔曲线）
        mPath = new Path();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mTxt,0,0,mPaint);
        //因为使用离层绘制，导致擦除
        //设置离屏
        int mLayerId = canvas.saveLayer(0,0,getWidth(),getHeight(),mPaint,Canvas.ALL_SAVE_FLAG);
        //先将路径绘制到 bitmap上
        mCanvas =new Canvas(mDex);
        mCanvas.drawPath(mPath, mPaint);
        //这个是在离层上  又绘制了一个目标图像  一个原图像  然后让画笔作用到目标图像上   目标图像是一个空白的灰色图像
        //绘制 目标图像====mDex是用来承载path路径的==下层
        canvas.drawBitmap(mDex,0,0,mPaint);
       //设置 模式 为 SRC_OUT,取上层绘制非交集部分，交集部分变成透明
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//=这样轨迹所到之处都是相交的--所以为透明，漏出了mTXT
        //绘制源图像
        canvas.drawBitmap(mSrc,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(mLayerId);
        //然后将离屏再设置回去
        super.onDraw(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - mLastX) / 2 + mLastX;
                float endY = (event.getY() - mLastY) / 2 + mLastY;
                //画二阶贝塞尔曲线
                mPath.quadTo(mLastX, mLastY, endX, endY);
                mLastX = event.getX();
                mLastY = event.getY();

                break;
        }
        invalidate();
        return true; //消费事件

    }

}
