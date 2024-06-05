package com.xej.xhjy.common.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CommonSlideRecyclerView extends SlideRecyclerView {
    private View emptyView;
    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
            if (emptyView != null){
                Adapter adapter = getAdapter();
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(VISIBLE);
                    CommonSlideRecyclerView.this.setVisibility(INVISIBLE);
                } else {
                    emptyView.setVisibility(INVISIBLE);
                    CommonSlideRecyclerView.this.setVisibility(VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }
    };

    public CommonSlideRecyclerView(Context context) {
        super(context);
    }

    public CommonSlideRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEmptyView(View view) {
        this.emptyView = view;
//        ((ViewGroup) this.getRootView()).addView(view);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }
}
