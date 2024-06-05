package com.xej.xhjy.common.view.recyclerview;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @class CommonRecyclerView 可设置空布局的Recycleview
 * @author dazhi
 * @Createtime 2018/6/20 10:33
 * @description describe
 * @Revisetime
 * @Modifier
 */

public class CommonRecyclerView extends RecyclerView {
    private View emptyView;
    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
            if (emptyView != null){
                Adapter adapter = getAdapter();
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(VISIBLE);
                    CommonRecyclerView.this.setVisibility(INVISIBLE);
                } else {
                    emptyView.setVisibility(INVISIBLE);
                    CommonRecyclerView.this.setVisibility(VISIBLE);
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

    public CommonRecyclerView(Context context) {
        super(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
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
