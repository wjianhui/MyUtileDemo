package com.jianhui.myutilesdemo.ui.utiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;

import androidx.annotation.Nullable;

import com.jianhui.myutilesdemo.R;


/**
 * @author Administrator
 * 2020/09/11 0011 14:19
 */
public class CircleProgressBar extends View {


    // 录制时的环形进度条
    private Paint mRecordPaint;
    // 录制时点击的圆形按钮
    private Paint mBgPaint;
    // 画笔宽度
    private int mStrokeWidth;
    // 圆形按钮半径
    private int mRadius;
    //控件宽度
    private int mWidth;
    //控件高度
    private int mHeight;
    // 圆的外接圆
    private RectF mRectF;
    //progress max value
    private int mMaxValue = 50;
    //per progress value
    private int mProgressValue;
    //是否开始record
    private boolean mIsStartRecord = false;
    //Arc left、top value
    private int mArcValue;
    //录制 time
    private long mRecordTime;

    private Chronometer chronometer;

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ++mProgressValue;
            postInvalidate();
            //当没有达到最大值时一直绘制
            if (mProgressValue < mMaxValue) {
                Log.i("####", "mProgressValue : : " + mProgressValue + "  mMaxValue : : " + mArcValue);
                mHandler.sendEmptyMessageDelayed(0, 100);
            } else if (mProgressValue == mMaxValue) {
                Log.i("####", "mProgressValue : " + mProgressValue + "  mMaxValue : " + mArcValue);
            }
        }
    };


    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context);
    }

    private Context mContext;

    //初始化画笔操作
    private void initParams(Context context) {
        this.mContext = context;
//        mArcValue = mStrokeWidth = dp2px(context, 3);
        mArcValue = dp2px(context, 50);
        mStrokeWidth = 10;

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.WHITE);
        mBgPaint.setStrokeWidth(mStrokeWidth);
        mBgPaint.setStyle(Paint.Style.FILL);

        mRecordPaint = new Paint();
        mRecordPaint.setAntiAlias(true);
        mRecordPaint.setColor(mContext.getResources().getColor(R.color.color_0190FF));
        mRecordPaint.setStrokeWidth(mStrokeWidth);
        mRecordPaint.setStyle(Paint.Style.STROKE);

        mRadius = dp2px(context, 50);
        mRectF = new RectF();

    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();
        if (mWidth != mHeight) {
            int min = Math.min(mWidth, mHeight);
            mWidth = min;
            mHeight = min;
        }

        if (mIsStartRecord) {

//            canvas.drawCircle(mWidth / 2, mHeight / 2, (float) (mRadius * 1.2), mBgPaint);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);


            if (mProgressValue <= mMaxValue) {
                //left--->距Y轴的距离
                //top--->距X轴的距离
                //right--->距Y轴的距离
                //bottom--->距X轴的距离

                mRectF.left = mArcValue;
                mRectF.top = mArcValue;
                mRectF.right = mWidth - mArcValue;
                mRectF.bottom = mHeight - mArcValue;

                canvas.drawArc(mRectF, -90,
                        ((float) mProgressValue / mMaxValue) * 360, false, mRecordPaint);

                if (mProgressValue == mMaxValue) {
                    mProgressValue = 0;
                    mHandler.removeMessages(0);
                    mIsStartRecord = false;
                    //这里可以回调出去表示已到录制时间最大值
                    //code.....
                }
            }
        } else {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);
        }
    }

    //重新该方法来完成触摸时，圆变大的效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsStartRecord = true;
                mRecordTime = System.currentTimeMillis();
                mHandler.sendEmptyMessage(0);
                //这里可以回调出去表示已经开始录制了
                //code.....

                break;
            case MotionEvent.ACTION_UP:
                if (mRecordTime > 0) {
                    //录制的时间（单位：秒）
                    int actualRecordTime = (int) ((System.currentTimeMillis() - mRecordTime) / 1000);
                    //这里回调出去表示已经取消录制了
                    //code.....

                }
                mHandler.removeMessages(0);
                mIsStartRecord = false;
                mRecordTime = 0;
                mProgressValue = 0;
                postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                //这里可以回调出去表示已经取消录制了
                //code.....
                mHandler.removeMessages(0);
                mIsStartRecord = false;
                mRecordTime = 0;
                mProgressValue = 0;
                postInvalidate();
                break;
        }

        return true;
    }


}
