package com.xej.xhjy.common.view.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @class CommonRecycleAdapter 通用的RecycleAdapter
 * @author dazhi
 * @Createtime 2018/6/20 10:24
 * @description describe
 * @Revisetime
 * @Modifier
 */
public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    protected LayoutInflater layoutInflater;

    protected List<T> dataList;

    protected int layoutId;

    public CommonRecycleAdapter(Context context, List<T> dataList, int layoutId) {
        this.layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    public void setDatas(List<T> mList){
        dataList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        bindData(holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public abstract void bindData(CommonViewHolder holder, T data);

}
