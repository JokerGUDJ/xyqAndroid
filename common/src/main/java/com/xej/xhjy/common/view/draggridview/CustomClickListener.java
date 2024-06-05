package com.xej.xhjy.common.view.draggridview;

import android.view.View;


/**
 * 处理快速点击
 */
public abstract class CustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 700L;

    public CustomClickListener() {

    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick(v);
        }
    }

    protected abstract void onSingleClick(View v);

    protected abstract void onFastClick(View v);
}