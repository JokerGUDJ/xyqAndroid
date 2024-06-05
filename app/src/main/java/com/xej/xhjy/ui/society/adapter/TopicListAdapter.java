package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.LogUtils;
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
public class TopicListAdapter extends CommonRecycleAdapter<HottopicBean.ContentBean> {
    private Context mContext;
    private List<HottopicBean.ContentBean> mList;
    private CommonViewHolder.onItemCommonClickListener mCommonClickListener;

    public TopicListAdapter(Context context, List<HottopicBean.ContentBean> list, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, list, R.layout.item_topic);
        this.mContext = context;
        this.mList = list;
        this.mCommonClickListener = commonClickListener;

    }


    @Override
    public void bindData(CommonViewHolder holder, HottopicBean.ContentBean data) {
        String title = data.getTitle();
        if (!TextUtils.isEmpty(title)) {
            title = "#" + data.getTitle();
        }
        holder.setText(R.id.tv_item_name, title)
                .setText(R.id.tv_item_content, data.getContent())
                .setCommonClickListener(mCommonClickListener);
        String url = NetConstants.POST_IMAGE + "/"+data.getAccessoryId();
        LogUtils.dazhiLog("话题head="+url);
        ImageLoadUtils.loadImageNoAnim((Activity) mContext, url, holder.setImageView(R.id.img_item_icon));

    }


}
