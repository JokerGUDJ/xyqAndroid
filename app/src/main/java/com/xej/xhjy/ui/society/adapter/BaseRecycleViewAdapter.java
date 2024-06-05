package com.xej.xhjy.ui.society.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.xej.xhjy.ui.society.listener.RecycleViewItemListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.adapter
 * @ClassName: BaseRecycleViewAdapter
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected RecycleViewItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getDatas() {
        if (datas == null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void setItemListener(RecycleViewItemListener listener) {
        this.itemListener = listener;
    }


}
