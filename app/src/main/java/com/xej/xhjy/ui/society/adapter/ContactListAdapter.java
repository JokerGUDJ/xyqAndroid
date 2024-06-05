package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.xej.xhjy.R;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.bean.ContactBean;

import java.util.List;

/**
 * @author dazhi
 * @class MettingListAdapter
 * @Createtime 2018/6/20 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class ContactListAdapter extends CommonRecycleAdapter<ContactBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public ContactListAdapter(Context context, List<ContactBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_contact);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, ContactBean.ContentBean bean) {
        holder.setCommonClickListener(commonClickListener);
        holder.setText(R.id.tv_item_name, bean.getName())
                .setText(R.id.tv_item_complany, bean.getOrgInfoLess().getOrgName())
                .setCommonClickListener(commonClickListener);
        String url = "";
        if (bean.getCenterId() != null) {
            url = NetConstants.HEAD_IMAG_URL + bean.getCenterId() + ".jpg";
        }
        ImageLoadUtils.loadImageDefault((Activity) mContext, url, holder.setImageView(R.id.img_item_icon), R.drawable.ic_user_default_icon, R.drawable.ic_image_loding);
        if (bean.getIsManager().equals("yes")) {
            holder.setViewVisibility(R.id.tv_item_admin, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.tv_item_admin, View.GONE);
        }
    }
}
