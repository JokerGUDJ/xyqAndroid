package com.xej.xhjy.ui.adapter;

import android.content.Context;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.viewpager.widget.PagerAdapter;

import com.xej.xhjy.ui.view.CustomBanner;
import com.xej.xhjy.ui.view.ViewCreator;

import java.util.List;

public class PageAdatper<T> extends PagerAdapter {

    private Context context;
    private List<T> mData;
    private ViewCreator<T> mCreator;
    private SparseArray<View> views = new SparseArray<>();
    private CustomBanner.OnPageClickListener listener;

    public PageAdatper(Context context, ViewCreator<T> creator, List<T> data){
        this.context = context;
        mData = data;
        mCreator = creator;
    }

    @Override
    public int getCount() {
        return mData == null || mData.isEmpty() ? 0 : mData.size() + 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        final int item = getActualPosition(position);

        View view = views.get(position);

        if (view == null) {
            view = mCreator.createView(context, item);
            views.put(position, view);
        }

        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }

        final T t = mData.get(item);

        mCreator.updateUI(context, view, item, t);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onPageClick(position,t);
                }
            }
        });

        container.addView(view);
        return view;
    }

    public void setOnPageClickListener(CustomBanner.OnPageClickListener listener){
        this.listener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        //Warning：不要在这里调用removeView
    }

    public int getActualPosition(int position) {
        if (position == 0) {
            return mData.size() - 1;
        } else if (position == getCount() - 1) {
            return 0;
        } else {
            return position - 1;
        }
    }
}
