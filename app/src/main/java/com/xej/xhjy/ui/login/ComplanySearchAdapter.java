package com.xej.xhjy.ui.login;

import android.content.Context;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.SearchComplanyBean;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;

import java.util.List;

/**
 * @class MettingListAdapter
 * @author dazhi
 * @Createtime 2018/6/20 11:08
 * @description 会议列表adapter
 * @Revisetime
 * @Modifier
 */
public class ComplanySearchAdapter extends CommonRecycleAdapter<SearchComplanyBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public ComplanySearchAdapter(Context context, List<SearchComplanyBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_complany_search);
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, SearchComplanyBean.ContentBean bean) {
        holder.setText(R.id.tv_search_name, bean.getOrgName())
                .setCommonClickListener(commonClickListener);
    }
}
