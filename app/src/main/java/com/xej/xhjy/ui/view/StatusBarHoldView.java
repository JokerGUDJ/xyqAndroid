package com.xej.xhjy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xej.xhjy.common.utils.AppConstants;

/**
 * @author dazhi
 * @class StatusBarHoldView 沉浸式状态栏占位view，自己适配状态栏高度而不需要手动设置
 * @Createtime 2018/6/28 11:15
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class StatusBarHoldView extends View {
    public StatusBarHoldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, AppConstants.STATUS_BAR_HEIGHT);
    }
}
