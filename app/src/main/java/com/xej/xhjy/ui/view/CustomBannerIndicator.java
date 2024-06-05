package com.xej.xhjy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xej.xhjy.R;


public class CustomBannerIndicator extends View {

    private Paint paintFill;
    private Paint paintStroke;
    private int mNum;//个数
    private float mRadius;//半径
    private float mLength;//线长
    private float mOffset;//偏移量
    private int mSelected_color;//选中颜色
    private int mDefault_color;//默认颜色
    private float mDistance;//间隔距离
    private int mPosition;//第几张
    private float mPercent;
    private boolean mIsLeft;
    public CustomBannerIndicator(Context context) {
        super(context);
    }

    public CustomBannerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setStyleable(context, attrs);
        paintStroke = new Paint();
        paintFill = new Paint();
    }
    /**
     * 初始化画笔
     */
    private void initPaint() {
        //实心
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(mSelected_color);
        paintFill.setAntiAlias(true);
        paintFill.setStrokeWidth(3);
        //空心
        paintStroke.setStyle(Paint.Style.FILL);
        paintStroke.setColor(mDefault_color);
        paintStroke.setAntiAlias(true);
        paintStroke.setStrokeWidth(3);
    }

    /**
     * 绘制   invalidate()后 执行
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mNum <= 0)) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //canvas.translate(((width-(mRadius*2*mNum + mDistance*(mNum - 1))+mRadius)/2), height/ 2);
        canvas.translate(((width-(mLength*mNum + mDistance*(mNum - 1)+mLength))/2), height/ 2);
        //初始化画笔
        initPaint();
        // paintStroke.setStrokeWidth(mRadius);
        float startX = 0;
        float stopX = mLength;
        //默认
        float topClose = -mRadius/3;
        float bottomClose = mRadius/3;
        for (int i = 0; i < mNum; i++) {
            // RectF rectClose = new RectF(startX+i * mDistance, topClose, stopX+i * mDistance, bottomClose);// 设置个新的长方形
            //canvas.drawRoundRect(rectClose, mRadius, mRadius, paintStroke);
            canvas.drawCircle(mRadius+(2*mRadius+mDistance)*i, mRadius,mRadius, paintStroke);
        }
        //选中
        // paintFill.setStrokeWidth(mRadius);
        float startF = -(mNum - 1) * 0.5f * mDistance - mLength / 2;
        float stopF = -(mNum - 1) * 0.5f * mDistance + mLength / 2 + mOffset;
        RectF rectF = new RectF(startF, topClose, stopF, bottomClose);
        // canvas.drawRoundRect(rectF, mRadius, mRadius, paintStroke);
        canvas.drawCircle(mRadius+mOffset, mRadius,mRadius, paintFill);
    }

    /**
     * xml 参数设置  选中颜色 默认颜色  点大小 长度 距离 距离类型 类型 真实个数(轮播)
     *
     * @param context
     * @param attrs
     */
    private void setStyleable(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mSelected_color = array.getColor(R.styleable.ViewPagerIndicator_vpi_selected_color, 0xffffffff);
        mDefault_color = array.getColor(R.styleable.ViewPagerIndicator_vpi_default_color, 0xffcdcdcd);
        mRadius = array.getDimension(R.styleable.ViewPagerIndicator_vpi_radius, 20);//px
        mLength = array.getDimension(R.styleable.ViewPagerIndicator_vpi_length, 2 * mRadius);//px
        mDistance = array.getDimension(R.styleable.ViewPagerIndicator_vpi_distance, 3 * mRadius);//px
        array.recycle();
        invalidate();
    }

    /**
     * 移动指示点
     *
     * @param percent  比例
     * @param position 第几个
     * @param isLeft   是否左滑
     */
    public void move(float percent, int position, boolean isLeft) {
        mPosition = position;
        mPercent = percent;
        mIsLeft = isLeft;
        if (mPosition == mNum - 2 && !isLeft) {//第一个 右滑
            mOffset = (mLength+mDistance) * position;
        } else if (mPosition == -1 && !isLeft) {//最后一个 左滑
            mOffset = (mLength+mDistance)*(mNum-1);
        } else {//中间的
            mOffset = (mLength+mDistance) * (position);
        }

        invalidate();
    }

    /**
     * 个数
     *
     * @param num
     */
    public CustomBannerIndicator setNum(int num) {
        mNum = num;
        invalidate();
        return this;
    }
}
