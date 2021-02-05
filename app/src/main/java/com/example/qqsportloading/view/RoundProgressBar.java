package com.example.qqsportloading.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.qqsportloading.R;

/**
 * Created by Administrator on 2021/2/5 0005 - 上午 9:26
 */
public class RoundProgressBar extends View {
    private int mMaxStep;   //最大数值
    private int mCurrentProgress;   //当前进度值
    private int mRoundProgressColor;    //半弧进度值
    private int mTextColor;     //字体色值
    private int mRoundColor;    //半弧底色
    private int mRadius;    //半径
    private int mCenterX;   //X轴
    private int mCenterY;   //Y轴
    private int mPercent;    //百分比
    private float mRoundWidth;    //半弧宽度
    private Paint mPaint;   //画笔

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceType")
    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
//        Log.e("TAG", "RoundProgressBar:-------> " + mTypedArray.toString() + "====" + mTypedArray.length());
        //获取自定义属性和默认值
        mRoundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.parseColor("#041EF7"));
        mRoundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.parseColor("#fd9426"));
        mTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.parseColor("#fd9426"));
        mRoundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);
        mMaxStep = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 10000);
//        mCurrentProgress = mTypedArray.getInteger(R.styleable.RoundProgressBar_currentProgress, 2134);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景圆弧
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        //设置圆弧画笔的宽度
        mPaint.setStrokeWidth(mRoundWidth);
        //设置为ROUND（就是笔头开始和结束都是圆弧状态）
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔颜色
        mPaint.setColor(mRoundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        //半径
        mRadius = (int) (mCenterX - mRoundWidth);
        @SuppressLint("DrawAllocation") RectF oval = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
//        Log.e(TAG, "onDraw: -------->" + mCenterX + "=====" + mCenterY + "======" + mRadius);   //onDraw: -------->540=====916======535
        //画背景圆弧
        canvas.drawArc(oval, 135, 270, false, mPaint);
        //画进度圆弧
        mPaint.setColor(mRoundProgressColor);
        //根据当前百分比计算圆弧扫过的角度
        canvas.drawArc(oval, 135, 270f * mCurrentProgress / mMaxStep, false, mPaint);
       /* //画数字进度值
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTextColor);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(25);
        //中间的进度百分比，先转换成float在进行计算，不然都为0
        mPercent = (int) (((float) mCurrentProgress / (float) mMaxStep) * 10000);
        //画进度值显示位置，这里也可将"mCurrentProgress"当作进度值
        canvas.drawText(mPercent + "", getWidth() / 2f, getHeight() / 2f, mPaint);*/
        //画文字步数
        mPaint.reset();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(30);
        mPaint.setAntiAlias(true);
        mPercent = (int) (((float) mCurrentProgress / (float) mMaxStep) * 10000);
        String mStep = mPercent + "";
        //测量文字的高度
        @SuppressLint("DrawAllocation") Rect textBounds = new Rect();
        mPaint.getTextBounds(mStep, 0, mStep.length(), textBounds);
        int dx = (getWidth() - textBounds.width()) / 2;
        // 获取画笔的FontMetrics
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // 计算文字的基线
        int baseLine = (int) (getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        // 绘制步数文字
        canvas.drawText(mStep, dx, baseLine, mPaint);
    }

    //设置当前最大步数
    public synchronized void setMaxStep(int mMaxStep) {
        if (mMaxStep < 0) {
            throw new IllegalArgumentException("maxStep不能为0");
        }
        this.mMaxStep = mMaxStep;
    }

    // 设置进度
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress 不能小于0!");
        }
        this.mCurrentProgress = progress;
        // 重新刷新绘制 -> onDraw()
        invalidate();
    }

    public synchronized int getCurrentProgress() {
        return mCurrentProgress;
    }

    public synchronized int getMaxStep() {
        return mMaxStep;
    }

    //启动方法
    public void start(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(1000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentProgress = (int) valueAnimator.getAnimatedValue();
                setProgress(currentProgress);
            }
        });
    }
}
