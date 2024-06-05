package com.netease.nim.uikit.business.team.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.utils.ImUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society
 * @ClassName: ContactSeleteAdapter
 * @Description: 联系人选择器，建群。
 * @Author: lihy_0203
 * @CreateDate: 2019/7/2 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/2 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ContactSeleteAdapter extends CommonRecycleAdapter<ContactGroupBean.ContentBean> {
    private Context mContext;
    private List<ContactGroupBean.ContentBean> mList;
    private CommonViewHolder.onItemCommonClickListener mCommonClickListener;

    public ContactSeleteAdapter(Context context, List<ContactGroupBean.ContentBean> list, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context,list, R.layout.nim_item_contact_selete_list);
        this.mContext = context;
        this.mList = list;
        this.mCommonClickListener = commonClickListener;

    }

    @Override
    public void bindData(CommonViewHolder holder, ContactGroupBean.ContentBean data) {
        String title = data.getName();
        LogUtils.dazhiLog("orgName------"+data.getOrgName());
        holder.setText(R.id.tv_userName, title);
        holder.setCommonClickListener(mCommonClickListener);
        if (data.isForPhone()) {
            holder.setImageResource(R.id.iv_click, R.drawable.nim_contact_checkbox_checked_green);
        } else {
            holder.setImageResource(R.id.iv_click, R.drawable.nim_checkbox_not_checked);
        }
        holder.setText(R.id.tv_orgName,data.getOrgName());
        String url = ImUtils.HEAD_IMAG_URL + data.getCenterId() + ".jpg";
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_user_default_icon)
                .placeholder(R.drawable.ic_user_default_icon)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .apply(requestOptions)
                .into(holder.setImageView(R.id.ivAvatar));
    }
}
