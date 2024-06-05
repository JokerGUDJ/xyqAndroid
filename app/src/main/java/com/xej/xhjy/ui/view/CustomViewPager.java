package com.xej.xhjy.ui.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean bScroll = true;
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public void setbScroll(boolean bScroll){
        this.bScroll = bScroll;
    }

    @Override
    public void scrollTo(int x, int y){
        if (bScroll){
            super.scrollTo(x, y);
        }
    }
}