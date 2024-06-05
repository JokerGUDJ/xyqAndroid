package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import androidx.annotation.LayoutRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.xej.xhjy.R;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.society.bean.followPerson;

import java.util.List;

/**
 * @author dazhi
 * @class AttentPeopleListAdapter
 * @Createtime 2018/6/20 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class AttentPeopleListAdapter extends BaseQuickAdapter<followPerson.ContentBean, BaseViewHolder> {


    public AttentPeopleListAdapter(@LayoutRes int layoutResId, List<followPerson.ContentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, followPerson.ContentBean item) {
        helper.setText(R.id.tv_item_name, item.getUserName());
        helper.setText(R.id.tv_item_content, item.getOrgnizationName());
        helper.addOnClickListener(R.id.tv_follow_btn);
        String url = NetConstants.HEAD_IMAG_URL + item.getUserId() + ".jpg";
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_user_default_icon)
                .placeholder(R.drawable.ic_user_default_icon);
        Glide.with(mContext).load(url).apply(options).into((HeadImageView)helper.getView(R.id.img_item_icon));
    }
}
