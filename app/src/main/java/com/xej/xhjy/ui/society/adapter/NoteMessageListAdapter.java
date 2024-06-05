package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.bean.PostNewMessageBean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author dazhi
 * @class MettingListAdapter
 * @Createtime 2018/6/20 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class NoteMessageListAdapter extends CommonRecycleAdapter<PostNewMessageBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public NoteMessageListAdapter(Context context, List<PostNewMessageBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_note_message);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, PostNewMessageBean.ContentBean bean) {
        String content = bean.getContent();
        holder.setCommonClickListener(commonClickListener);
        holder.setText(R.id.tv_item_name, bean.getUserName())
                .setText(R.id.tv_item_time, DateUtils.FormatTime(bean.getCreatTime()))
                .setCommonClickListener(commonClickListener);
        if (!TextUtils.isEmpty(content)) {
            if ("AUTO".equals(content)) {
                holder.setText(R.id.tv_item_content, "赞了你的帖子");
            } else {
                //转换表情字符
                String contentBodyStr = bean.getContent();
                if (!TextUtils.isEmpty(contentBodyStr)) {
                    try {
                        contentBodyStr = URLDecoder.decode(contentBodyStr, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        contentBodyStr="";
                    }
                }
                holder.setText(R.id.tv_item_content, "评论了你的帖子："+contentBodyStr);
            }

        }
        //头像
        String urlHead = NetConstants.HEAD_IMAG_URL + bean.getCenterId() + ".jpg";
        ImageLoadUtils.loadImageDefault((Activity) mContext, urlHead, holder.setImageView(R.id.img_item_icon), R.drawable.ic_user_default_icon, R.drawable.ic_image_loding);


        //如果是发图片或视频需要处理
        String accList = bean.getAccessoryList();

        if (!TextUtils.isEmpty(accList)) {
            String[] list = accList.split(",");
            if (list.length > 0) {
                String urlImg = list[0];
                String url = NetConstants.POST_IMAGE + urlImg;
                ImageLoadUtils.loadImageDefault((Activity) mContext, url, holder.setImageView(R.id.img_note_icon), R.drawable.ic_image_error, R.drawable.ic_image_loding);
            }

        } else {
            holder.setViewVisibility(R.id.img_note_icon, View.GONE);
        }


    }
}
