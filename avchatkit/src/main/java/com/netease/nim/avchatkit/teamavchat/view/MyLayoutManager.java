package com.netease.nim.avchatkit.teamavchat.view;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 不可滑动
 */
public class MyLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public MyLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    /**
     * 禁止滑动
     * canScrollHorizontally（禁止横向滑动）
     *
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    /**
     * 禁止滑动
     * canScrollVertically（禁止竖向滑动）
     *
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }


}