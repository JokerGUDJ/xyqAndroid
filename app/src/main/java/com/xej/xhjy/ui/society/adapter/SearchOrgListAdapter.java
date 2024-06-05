package com.xej.xhjy.ui.society.adapter;

import android.content.Context;
import android.graphics.Color;

import com.xej.xhjy.R;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.ui.society.bean.OrgBean;
import com.xej.xhjy.ui.society.widgets.TextViewColor;

import java.util.List;

/**
 * @author lihaiyuan
 * @class SearchContactListAdapter
 * @Createtime 2019/5/15 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class SearchOrgListAdapter extends CommonRecycleAdapter<OrgBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private String keyword;

    public SearchOrgListAdapter(Context context, List<OrgBean.ContentBean> dataList, String keyword, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_complany_search);
        this.commonClickListener = commonClickListener;
        this.keyword = keyword;
    }

    @Override
    public void bindData(CommonViewHolder holder, OrgBean.ContentBean bean) {
        String str = bean.getOrgName();
        CharSequence charSequence = TextViewColor.matcherSearchText(Color.rgb(255, 115, 0), str, keyword);
        holder.setText(R.id.tv_search_name, charSequence)
                .setCommonClickListener(commonClickListener);

    }
}
