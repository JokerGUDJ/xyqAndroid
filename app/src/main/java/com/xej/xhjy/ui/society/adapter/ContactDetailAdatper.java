package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.bumptech.glide.Glide;
import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.ui.society.ContactDetailActivity;
import com.xej.xhjy.ui.society.ImagePagerActivity;
import com.xej.xhjy.ui.society.TopicDetailActivity;
import com.xej.xhjy.ui.society.adapter.viewholder.CircleViewHolder;
import com.xej.xhjy.ui.society.adapter.viewholder.ImageViewHolder;
import com.xej.xhjy.ui.society.adapter.viewholder.VideoViewHolder;
import com.xej.xhjy.ui.society.bean.PhotoInfo;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.presenter.CirclePresenter;
import com.xej.xhjy.ui.society.spannable.SpannableClickable;
import com.xej.xhjy.ui.society.widgets.ExpandTextView;
import com.xej.xhjy.ui.society.widgets.MultiImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dazhi
 * @class CommitteeAdatper
 * @Createtime 2018/11/26 15:36
 * @description 个人主页帖子列表
 * @Revisetime
 * @Modifier
 */

public class ContactDetailAdatper extends BaseRecycleViewAdapter {
    private Context mContext;
    private onItemCommonClickListener mCommonClickListener;
    private List<PostListBean.ContentBean> mList;
    private long mLasttime = 0;
    private CirclePresenter presenter;
    private String mPath;//从哪里进入的
    private View headerView;


    public ContactDetailAdatper(Context context, String path, List<PostListBean.ContentBean> list, onItemCommonClickListener commonClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mPath = path;
        this.mCommonClickListener = commonClickListener;

    }


    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
        PostListBean.ContentBean item = mList.get(position);
        if (position == 0) {
            itemType = CircleViewHolder.TYPE_HEADER;
        }
        if (!TextUtils.isEmpty(item.getFileType())) {
            switch (Integer.parseInt(item.getFileType())) {
                case 1:
                    itemType = CircleViewHolder.TYPE_IMAGE;
                    break;
                case 2:
                    itemType = CircleViewHolder.TYPE_VIDEO;
                    break;
            }
        } else {
            itemType = CircleViewHolder.TYPE_NORMAL;
        }
        return itemType;


    }

    public void addHeader(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item_society_list, viewGroup, false);
        if (viewType == CircleViewHolder.TYPE_IMAGE) {
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
            viewHolder = new VideoViewHolder(view);
        } else {
            viewHolder = new CircleViewHolder(view, CircleViewHolder.TYPE_NORMAL) {
                @Override
                public void initSubView(int viewType, ViewStub viewStub) {

                }


            };
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final int circlePosition = position;
        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final PostListBean.ContentBean bean = mList.get(circlePosition);
        final List<PostListBean.ContentBean.SocialLikeBean> favortDatas = bean.getSocialLike();
        final List<PostListBean.ContentBean.ReplyListBean> replyDatas = bean.getReplyList();
        boolean hasFavort = bean.isWhetherLike();
        boolean hasComment = bean.isWhetherReply();
        final String content = bean.getContent();
        holder.head_search_item.setVisibility(View.GONE);
        holder.digCommentBody.setVisibility(View.GONE);
        holder.img_love.setVisibility(View.GONE);
        holder.img_more.setVisibility(View.GONE);
        holder.tv_delete.setVisibility(View.GONE);
        String url = NetConstants.HEAD_IMAG_URL + bean.getUserCenterId() + ".jpg";
        ImageLoadUtils.loadImageDefault((Activity) mContext, url, holder.headIv, R.drawable.ic_user_default_icon, R.drawable.ic_image_loding);
        holder.nameTv.setText(bean.getPosterName());
        holder.timeTv.setText(DateUtils.FormatTime(bean.getCreateTime()));
        holder.tv_complany.setText(bean.getOrgNm());
        if (!TextUtils.isEmpty(bean.getSite())) {
            holder.tv_address.setText(bean.getSite());
        } else {
            holder.tv_address.setVisibility(View.GONE);
        }
        String tipicName = bean.getTopicName();
        /**
         * 头像
         */
        holder.headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("postId", mList.get(position).getPosterId());
                intent.putExtra("followId", mList.get(position).getWhetherAttention());
                mContext.startActivity(intent);


            }
        });

        /**
         * 发布内容
         */
        //收起、打开
        holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
            @Override
            public void statusChange(boolean isExpand) {


            }
        });
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            if(!TextUtils.isEmpty(tipicName)) {
                SpannableString spannableString = new SpannableString("#" + tipicName + "#");
                spannableString.setSpan(new SpannableClickable(Color.parseColor("#4760A8")) {
                                            @Override
                                            public void onClick(View widget) {
                                                /**
                                                 * 话题
                                                 */
                                                Intent intent = new Intent(mContext, TopicDetailActivity.class);
                                                intent.putExtra("topicId", mList.get(position).getTopicId());
                                                intent.putExtra("title", mList.get(position).getTopicName());
                                                intent.putExtra("content", mList.get(position).getContent());
                                                mContext.startActivity(intent);
                                            }
                                        }, 0, spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
            }
            if(!TextUtils.isEmpty(content)){
                String str = URLDecoder.decode(content, "utf-8");
                builder.append(str);
            }
            holder.contentTv.setText(builder);
        } catch (UnsupportedEncodingException e) {
            holder.contentTv.setText("");
        }
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        String adShow = mList.get(position).getAdShow();
        if (!TextUtils.isEmpty(adShow) && !"6".equals(adShow)) {
            holder.tv_top_setting.setVisibility(View.VISIBLE);
        } else {
            holder.tv_top_setting.setVisibility(View.INVISIBLE);
        }

        /**********************************处理图片或视频**********************************/

        switch (holder.viewType) {
            case CircleViewHolder.TYPE_IMAGE:
                if (holder instanceof ImageViewHolder) {
                    String photos = bean.getAccessoryUoloadList();
                    if (!TextUtils.isEmpty(photos)) {
                        String[] list = photos.split(",");
                        if (list.length > 0) {
                            List<PhotoInfo> listPhoto = new ArrayList<>();
                            for (String str : list) {
                                PhotoInfo info = new PhotoInfo();
                                info.setUrl(NetConstants.POST_IMAGE + str);
                                listPhoto.add(info);
                            }
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setList(listPhoto);
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //imagesize是作为loading时的图片size
                                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                                    List<String> photoUrls = new ArrayList<String>();
                                    for (PhotoInfo photoInfo : listPhoto) {
                                        photoUrls.add(photoInfo.url);
                                    }
                                    ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);


                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }

                    } else {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                    }
                } else {
                    ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                }
                break;
            case CircleViewHolder.TYPE_VIDEO:
                if (holder instanceof VideoViewHolder) {
                    String video = bean.getAccessoryUoloadList();
                    if (!TextUtils.isEmpty(video)) {
                        String[] list = video.split(",");
                        if (list.length == 2) {
                            String str = list[0];
                            String MP4 = str.substring(str.lastIndexOf(".") + 1);
                            if ("mp4".equals(MP4)) {
                                ((VideoViewHolder) holder).videoPlay.setUp(NetConstants.POST_IMAGE + str, "");
                                LogUtils.dazhiLog("视频地址=====" + NetConstants.POST_IMAGE + str);
                            } else {
                                ((VideoViewHolder) holder).videoPlay.setVisibility(View.GONE);
                            }
                            String strjpg = list[1];
                            String jpg = strjpg.substring(strjpg.lastIndexOf(".") + 1);
                            if ("jpg".equals(jpg)) {
                                Glide.with(mContext).load(NetConstants.POST_IMAGE + strjpg).into(((VideoViewHolder) holder).videoPlay.thumbImageView);
                                LogUtils.dazhiLog("图片地址=====" + NetConstants.POST_IMAGE + strjpg);
                            } else {
                                ((VideoViewHolder) holder).videoPlay.setVisibility(View.GONE);
                            }
                        } else {
                            ((VideoViewHolder) holder).videoPlay.setVisibility(View.GONE);
                        }

                    } else {
                        ((VideoViewHolder) holder).videoPlay.setVisibility(View.GONE);
                    }

                } else {
                    ((VideoViewHolder) holder).videoPlay.setVisibility(View.GONE);
                }

                break;

        }


    }


    @Override
    public int getItemCount() {
        int totalCount = mList.size();
        if (headerView != null) totalCount += 1;
        return totalCount;
    }

    public interface onItemCommonClickListener {

        void onItemClickListener(int position);

        void onItemLongClickListener(int position);

    }
}