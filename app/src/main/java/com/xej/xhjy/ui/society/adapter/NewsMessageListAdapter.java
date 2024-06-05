package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.bean.PlatformMessageBean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.adapter
 * @ClassName: NewsMessageListAdapter
 * @Description: 平台消息：包含会议消息 帖子消息 分布局加载
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午4:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午4:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */


public class NewsMessageListAdapter extends BaseRecycleViewAdapter {

    private Context mContext;
    private List<PlatformMessageBean.ContentBean> mList;
    private onItemNewsMessageClickListener messageClickListener;

    public NewsMessageListAdapter(Context context, List<PlatformMessageBean.ContentBean> list, onItemNewsMessageClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.messageClickListener = listener;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case PlatformMessageBean.MEETING:
                View banner = LayoutInflater.from(mContext).inflate(R.layout.item_news_message, viewGroup, false);
                return new MeetingHolder(banner);

            case PlatformMessageBean.SOCIETY:
                View recommend = LayoutInflater.from(mContext).inflate(R.layout.item_note_message, viewGroup, false);
                return new SocoetyHolder(recommend);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof MeetingHolder) {
            ((MeetingHolder) holder).tv_item_content.setText(mList.get(i).getPushMessage());
            ((MeetingHolder) holder).tv_item_name.setText(mList.get(i).getTitle());
            ((MeetingHolder) holder).tv_item_time.setText(DateUtils.FormatTime(mList.get(i).getCreatTime()));
            String notifyStt = mList.get(i).getNotifyStt();
            ((MeetingHolder) holder).ll_click_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageClickListener != null) {
                        messageClickListener.onItemClickListener(i);
                        ((MeetingHolder) holder).img_item_icon.setVisibility(View.INVISIBLE);
                    }
                }
            });

            if (!TextUtils.isEmpty(notifyStt)) {
                switch (Integer.parseInt(notifyStt)) {
                    case 0:
                        ((MeetingHolder) holder).img_item_icon.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ((MeetingHolder) holder).img_item_icon.setVisibility(View.INVISIBLE);
                        break;
                }

            } else {
                ((MeetingHolder) holder).img_item_icon.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof SocoetyHolder) {
            ((SocoetyHolder) holder).tv_item_time.setText(DateUtils.FormatTime(mList.get(i).getCreatTime()));
            ((SocoetyHolder) holder).tv_item_name.setText(mList.get(i).getUserName());
            String content = mList.get(i).getContent();
            if (!TextUtils.isEmpty(content)) {
                if ("AUTO".equals(content)) {
                    ((SocoetyHolder) holder).tv_item_content.setText("赞了你的帖子");
                } else {
                    //转换表情字符
                    if (!TextUtils.isEmpty(content)) {
                        try {
                            content = URLDecoder.decode(content, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            content = "";
                        }
                    }
                    ((SocoetyHolder) holder).tv_item_content.setText("评论了你的帖子："+content);
                }

            }
            String urlHead = NetConstants.HEAD_IMAG_URL + mList.get(i).getCenterId() + ".jpg";
            ImageLoadUtils.loadImageDefault((BaseActivity) mContext, urlHead, ((SocoetyHolder) holder).img_item_icon, R.drawable.ic_user_default_icon, R.drawable.ic_image_loding);
            //如果是发图片或视频需要处理
            String accList = mList.get(i).getAccessoryList();
            if (!TextUtils.isEmpty(accList)) {
                String[] list = accList.split(",");
                if (list.length > 0) {
                    String urlImg = list[0];
                    String url = NetConstants.POST_IMAGE + urlImg;
                    ImageLoadUtils.loadImageDefault((Activity) mContext, url, ((SocoetyHolder) holder).img_note_icon, R.drawable.ic_image_error, R.drawable.ic_image_loding);
                }

            } else {
                ((SocoetyHolder) holder).img_note_icon.setVisibility(View.GONE);
            }

            ((SocoetyHolder) holder).ll_new_message_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageClickListener != null) {
                        messageClickListener.onItemClickListener(i);
                        ((SocoetyHolder) holder).img_unread.setVisibility(View.INVISIBLE);
                    }

                }
            });
            String not = mList.get(i).getNotifyStt();
            if (!TextUtils.isEmpty(not)) {
                switch (Integer.parseInt(not)) {
                    case 0:
                        ((SocoetyHolder) holder).img_unread.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ((SocoetyHolder) holder).img_unread.setVisibility(View.INVISIBLE);
                        break;
                }

            } else {
                ((SocoetyHolder) holder).img_unread.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
        String mark = mList.get(position).getMark();
        switch (Integer.parseInt(mark)) {
            case 0:
                itemType = PlatformMessageBean.MEETING;
                break;
            case 1:
                itemType = PlatformMessageBean.SOCIETY;
                break;
        }
        return itemType;


    }

    static class MeetingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_item_content)
        TextView tv_item_content;
        @BindView(R.id.tv_item_time)
        TextView tv_item_time;
        @BindView(R.id.ll_click_content)
        LinearLayout ll_click_content;
        @BindView(R.id.img_item_icon)
        ImageView img_item_icon;

        public MeetingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SocoetyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_item_time)
        TextView tv_item_time;
        @BindView(R.id.tv_item_content)
        TextView tv_item_content;
        @BindView(R.id.img_item_icon)
        ImageView img_item_icon;
        @BindView(R.id.img_note_icon)
        ImageView img_note_icon;
        @BindView(R.id.img_unread)
        ImageView img_unread;
        @BindView(R.id.ll_new_message_item)
        LinearLayout ll_new_message_item;

        public SocoetyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onItemNewsMessageClickListener {

        void onItemClickListener(int position);
    }

}


