package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.LayoutRes;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xej.xhjy.R;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.bean.HottopicBean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society
 * @ClassName: TopicFollowListAdapter
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TopicFollowListAdapter extends BaseQuickAdapter<HottopicBean.ContentBean, BaseViewHolder> {
    private Context mContext;
    private List<HottopicBean.ContentBean> mList;

    public TopicFollowListAdapter(Context context,@LayoutRes int layoutResId, List<HottopicBean.ContentBean> data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HottopicBean.ContentBean data) {
        String title = data.getTitle();
        if (!TextUtils.isEmpty(title)) {
            title = "#" + data.getTitle();
        }
        helper.setText(R.id.tv_item_name, title)
                .setText(R.id.tv_item_content, data.getContent());
        String url = NetConstants.POST_IMAGE + "/" + data.getAccessoryId();
        helper.addOnClickListener(R.id.tv_follow_topic);
        //ImageLoadUtils.loadImageNoAnim((Activity) mContext, url, helper.getView(R.id.img_item_icon));
    }

}
