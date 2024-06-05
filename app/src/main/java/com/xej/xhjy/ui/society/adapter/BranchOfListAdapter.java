package com.xej.xhjy.ui.society.adapter;

import android.content.Context;

import com.xej.xhjy.R;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.ui.society.bean.BranchOfBean;

import java.util.List;

/**
 * @author lihaiyuan
 * @class SearchContactListAdapter
 * @Createtime 2019/5/15 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class BranchOfListAdapter extends CommonRecycleAdapter<BranchOfBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public BranchOfListAdapter(Context context, List<BranchOfBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_complany_search);
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, BranchOfBean.ContentBean bean) {
        String str = bean.getName();
//        CharSequence charSequence = TextViewColor.matcherSearchText(Color.rgb(255, 115, 0), str, keyword);
        holder.setText(R.id.tv_search_name, str)
                .setCommonClickListener(commonClickListener);

    }
}
