package com.xej.xhjy.ui.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomViewPagerScroller extends Scroller {
    private int mScrollDuration = 360;
    private boolean sudden;

    public CustomViewPagerScroller(Context context) {
        super(context);
    }

    public CustomViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public CustomViewPagerScroller(Context context, Interpolator interpolator,
                                   boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, sudden ? 0 : mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, sudden ? 0 : mScrollDuration);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
    }

    public boolean isSudden() {
        return sudden;
    }

    public void setSudden(boolean zero) {
        this.sudden = zero;
    }
}
