package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.xej.xhjy.R;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.bean.HottopicBean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society
 * @ClassName: HottopicAdapter
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HottopicAdapter extends CommonRecycleAdapter<HottopicBean.ContentBean> {
    private Context mContext;
    private List<HottopicBean.ContentBean> mList;
    private CommonViewHolder.onItemCommonClickListener mCommonClickListener;

    public HottopicAdapter(Context context, List<HottopicBean.ContentBean> list, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, list, R.layout.item_topic);
        this.mContext = context;
        this.mList = list;
        this.mCommonClickListener = commonClickListener;

    }


    @Override
    public void bindData(CommonViewHolder holder, HottopicBean.ContentBean data) {
        String title=data.getTitle();
        if (!TextUtils.isEmpty(title)){
            title="#"+data.getTitle();
        }
        holder.setText(R.id.tv_item_name, title)
                .setText(R.id.tv_item_content, data.getContent())
                .setCommonClickListener(mCommonClickListener);
        String url = NetConstants.POST_IMAGE + "/"+data.getAccessoryId();
        ImageLoadUtils.loadImageNoAnim((FragmentActivity)mContext, url, holder.getView(R.id.img_item_icon));
    }


}
