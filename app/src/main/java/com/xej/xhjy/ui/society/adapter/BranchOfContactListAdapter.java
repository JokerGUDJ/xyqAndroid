package com.xej.xhjy.ui.society.adapter;

import android.content.Context;

import com.xej.xhjy.R;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.ui.society.bean.ContactBean;

import java.util.List;

/**
 * @author lihaiyuan
 * @class SearchContactListAdapter
 * @Createtime 2019/5/15 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class BranchOfContactListAdapter extends CommonRecycleAdapter<ContactBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public BranchOfContactListAdapter(Context context, List<ContactBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_name_list);
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, ContactBean.ContentBean bean) {
        String str = bean.getName();
//        CharSequence charSequence = TextViewColor.matcherSearchText(Color.rgb(255, 115, 0), str, keyword);
        holder.setText(R.id.tv_search_name, str)
                .setCommonClickListener(commonClickListener);
        holder.setText(R.id.tv_search_org, bean.getRoleRole().getUsertype()).setCommonClickListener(commonClickListener);

    }
}
