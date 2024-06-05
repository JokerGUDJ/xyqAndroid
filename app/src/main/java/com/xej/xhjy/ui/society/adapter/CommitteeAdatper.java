package com.xej.xhjy.ui.society.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.image.GlideRoundTransform;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.society.ContactDetailActivity;
import com.xej.xhjy.ui.society.ImagePagerActivity;
import com.xej.xhjy.ui.society.SearchPostListActivity;
import com.xej.xhjy.ui.society.TopicDetailActivity;
import com.xej.xhjy.ui.society.adapter.viewholder.CircleViewHolder;
import com.xej.xhjy.ui.society.adapter.viewholder.ImageViewHolder;
import com.xej.xhjy.ui.society.adapter.viewholder.VideoViewHolder;
import com.xej.xhjy.ui.society.bean.CommentConfig;
import com.xej.xhjy.ui.society.bean.PhotoInfo;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.presenter.CirclePresenter;
import com.xej.xhjy.ui.society.spannable.SpannableClickable;
import com.xej.xhjy.ui.society.widgets.CommentDialog;
import com.xej.xhjy.ui.society.widgets.CommentListView;
import com.xej.xhjy.ui.society.widgets.ExpandTextView;
import com.xej.xhjy.ui.society.widgets.MultiImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihy_0203
 * @class CommitteeAdatper
 * @Createtime 2018/11/26 15:36
 * @description 通用帖子列表
 * @Revisetimea
 * @Modifier
 */

public class CommitteeAdatper extends BaseRecycleViewAdapter {
    private Context mContext;
    private onItemCommonClickListener mCommonClickListener;
    private List<PostListBean.ContentBean> mList;
    private long mLasttime = 0;
    private CirclePresenter presenter;
    private String accId;//获取云信ID
    private String userId;
    private PopupWindow popupWindow;


    public CommitteeAdatper(Context context, List<PostListBean.ContentBean> list, onItemCommonClickListener commonClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mCommonClickListener = commonClickListener;
        accId = PerferenceUtils.get(AppConstants.User.IM_POST_ID, "");
        userId = PerferenceUtils.get(AppConstants.User.ID, "");
    }

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
        PostListBean.ContentBean item = mList.get(position);
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_society_list, viewGroup, false);
        //头部增加搜索功能
        if (viewType == CircleViewHolder.TYPE_IMAGE) {
            //图片
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
            //视频
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
        CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final int circlePosition = position;
        final PostListBean.ContentBean bean = mList.get(circlePosition);
        final List<PostListBean.ContentBean.SocialLikeBean> favortDatas = bean.getSocialLike();
        final List<PostListBean.ContentBean.ReplyListBean> replyDatas = bean.getReplyList();
        boolean hasFavort = bean.isWhetherLike();
        boolean hasComment = bean.isWhetherReply();
        final String content = bean.getContent();
        if (position == 0) {
            holder.head_search_item.setVisibility(View.VISIBLE);
        } else {
            holder.head_search_item.setVisibility(View.GONE);
        }
        /**
         * 获取基本信息
         */
        if (!TextUtils.isEmpty(userId)) {
            if (userId.equals(bean.getUserCenterId())) {
                holder.tv_delete.setVisibility(View.VISIBLE);
                holder.img_love.setVisibility(View.GONE);
            } else {
                holder.tv_delete.setVisibility(View.GONE);
                holder.img_love.setVisibility(View.VISIBLE);
                if ("1".equals(bean.getWhetherAttention())) {
                    holder.img_love.setImageResource(R.drawable.ic_love_full);
                } else {
                    holder.img_love.setImageResource(R.drawable.ic_love_empty);
                }
            }
        }
        String url = NetConstants.HEAD_IMAG_URL + bean.getUserCenterId() + ".jpg";

        ///ImageLoadUtils.showHttpImageRound((Activity) mContext, url, holder.headIv, 10);
        String adShow = mList.get(position).getAdShow();

        if (!TextUtils.isEmpty(adShow) && !"6".equals(adShow)) {
            holder.tv_top_setting.setVisibility(View.VISIBLE);
        } else {
            holder.tv_top_setting.setVisibility(View.INVISIBLE);
        }

        holder.nameTv.setText(bean.getPosterName());
        holder.timeTv.setText(DateUtils.FormatTime(bean.getCreateTime()));
        holder.tv_complany.setText(bean.getOrgNm());
        if (!TextUtils.isEmpty(bean.getSite())) {
            if (!"位置".equals(bean.getSite())) {
                holder.tv_address.setText(bean.getSite());
                holder.tv_address.setVisibility(View.VISIBLE);
            } else {
                holder.tv_address.setVisibility(View.GONE);
            }
        } else {
            holder.tv_address.setVisibility(View.GONE);
        }
        String tipicName = bean.getTopicName();
        /**
         * 搜所
         */
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchPostListActivity.class);
                intent.putExtra("commitId", bean.getCommitId());
                mContext.startActivity(intent);
            }
        });


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
                                                intent.putExtra("topicName", mList.get(position).getTopicName());
                                                intent.putExtra("title", mList.get(position).getTopicName());
                                                intent.putExtra("content", mList.get(position).getTopicContent());
                                                intent.putExtra("accessoryId", mList.get(position).getAccessoryList());
                                                intent.putExtra("orgId", mList.get(position).getOrgId());
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

        //发布的图片

        if(TextUtils.isEmpty(bean.getAccessoryUoloadList())){
            holder.img_topic.setVisibility(View.GONE);
        }else{
            Glide.with(mContext).load(NetConstants.POST_IMAGE + bean.getAccessoryUoloadList())
                    .apply(new RequestOptions().transform(new GlideRoundTransform(mContext,5)))
                    .into(holder.img_topic);
            holder.img_topic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                    List<String> photoUrls = new ArrayList<String>();
                    photoUrls.add(NetConstants.POST_IMAGE + bean.getAccessoryUoloadList());
                    ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
                }
            });
        }
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        /**
         * 关注/删除
         */
        holder.img_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCommonClickListener != null) {
                    mCommonClickListener.onItemClickListener(position);
                }

            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCommonClickListener != null) {
                    mCommonClickListener.onItemClickListener(position);
                }

            }
        });
        //更多
        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow(view, position, bean);
            }
        });

        /**************************************点赞和评论相关************************************/
        if (hasFavort || hasComment) {
            if (hasFavort) { //处理点赞列表
                if (favortDatas.size() == 0) {
                    holder.praiseListView.setVisibility(View.GONE);
                    holder.digLine.setVisibility(View.GONE);
                } else {
                    holder.praiseListView.setDatas(favortDatas);
                    holder.praiseListView.setVisibility(View.VISIBLE);
                    holder.digLine.setVisibility(View.VISIBLE);
                }

            } else {
                holder.praiseListView.setVisibility(View.GONE);
            }
            //处理评论列表
            if (hasComment) {
                holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        boolean utterId = getCommentId(accId, position, commentPosition);
                        if (utterId) {//复制或者删除自己的评论
                            PostListBean.ContentBean.ReplyListBean commentItem = replyDatas.get(commentPosition);
                            CommentDialog dialog = new CommentDialog(mContext, presenter, commentItem, circlePosition);
                            dialog.show();
                        } else {//回复别人的评论
                            if (presenter != null) {
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = circlePosition;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                //被回复内容ID  也就是ReplyList中的id
                                config.replyUser = bean.getReplyList().get(commentPosition).getUtterId();
                                //被回复人ID  也就是ReplyList中的utterId
                                config.fatherId = bean.getReplyList().get(commentPosition).getId();
                                //帖子ID (Id)
                                config.messageId = bean.getId();
                                //发帖人ID (posterId)
                                config.posterId = bean.getPosterId();
                                presenter.showEditTextBody(config);
                            }
                        }
                    }
                });
                holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int commentPosition) {

                    }
                });

                if (replyDatas.size() == 0) {
                    holder.commentList.setDatas(replyDatas);
                    holder.commentList.setVisibility(View.VISIBLE);
                    holder.digLine.setVisibility(View.GONE);
                } else {
                    holder.commentList.setDatas(replyDatas);
                    holder.commentList.setVisibility(View.VISIBLE);
                    holder.digLine.setVisibility(View.VISIBLE);
                }

            } else {
                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);

        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }
        holder.digLine.setVisibility(replyDatas.size() == 0 || favortDatas.size() == 0 ? View.GONE : View.VISIBLE);
        holder.digCommentBody.setVisibility(replyDatas.size() == 0 && favortDatas.size() == 0 ? View.GONE : View.VISIBLE);

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
                                ((VideoViewHolder) holder).videoPlay.setUp(NetConstants.POST_IMAGE + str,"");
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

    private void showPopWindow(View parent, int position, PostListBean.ContentBean bean ){
        if(popupWindow == null){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_social_more, null, false);
            popupWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });
            /**
             * 赞
             */
            boolean mFavorId = getCurUserFavortId(accId, position);
            TextView tv_zan = view.findViewById(R.id.tv_zan);
            if(mFavorId){
                tv_zan.setText("取消");
            }else{
                tv_zan.setText("赞");
            }
            tv_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    LogUtils.dazhiLog("打印SocialLike=" + bean.getSocialLike());
                    mLasttime = System.currentTimeMillis();
                    if (mFavorId) {
                        presenter.deleteFavort((BaseActivity) mContext, position, getId(accId, position));
                    } else {//取消点赞
                        presenter.addFavort((BaseActivity) mContext, position, bean.getPosterId(), bean.getId());
                    }
                    popupWindow.dismiss();
                }
            });
            /**
             * 评论
             */
            TextView tv_comments = view.findViewById(R.id.tv_comments);
            tv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = position;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        config.messageId = bean.getId();
                        config.posterId = bean.getPosterId();
                        presenter.showEditTextBody(config);
                        popupWindow.dismiss();
                    }

                }
            });
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int  mShowMorePopupWindowWidth = -view.getMeasuredWidth() -  GenalralUtils.dp2px(mContext,8);
            int  mShowMorePopupWindowHeight = -view.getMeasuredHeight() + GenalralUtils.dp2px(mContext,5);
            popupWindow.showAsDropDown(parent,mShowMorePopupWindowWidth, mShowMorePopupWindowHeight);
        }else{
            popupWindow.dismiss();
            popupWindow = null;
        }

    }

    /**
     * 自己赞过
     *
     * @param curUserId
     * @param pos
     * @return
     */
    public boolean getCurUserFavortId(String curUserId, int pos) {
        if (!TextUtils.isEmpty(curUserId) && mList.get(pos).isWhetherLike()) {
            for (PostListBean.ContentBean.SocialLikeBean item : mList.get(pos).getSocialLike()) {
                if (curUserId.equals(item.getPeopleId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取赞过的ID
     *
     * @param curUserId
     * @param pos
     * @return
     */
    public String getId(String curUserId, int pos) {
        String id = "";
        if (!TextUtils.isEmpty(curUserId) && mList.get(pos).isWhetherLike()) {
            for (PostListBean.ContentBean.SocialLikeBean item : mList.get(pos).getSocialLike()) {
                if (curUserId.equals(item.getPeopleId())) {
                    id = item.getId();
                    return id;
                }
            }
        }
        return id;
    }

    /**
     * 获取自己评论ID，去删除或复制操作，否则评论别人的帖子
     *
     * @param accId
     * @param pos
     * @return
     */
    public boolean getCommentId(String accId, int pos, int commentPos) {
        if (!TextUtils.isEmpty(accId) && mList.get(pos).isWhetherReply()) {
            List<PostListBean.ContentBean.ReplyListBean> list = mList.get(pos).getReplyList();
            for (int i = 0; i < list.size(); i++) {
                if (i == commentPos) {
                    if (accId.equals(list.get(commentPos).getUtterId())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /*
     * 设置点赞的drawable
     * bZan,是否点赞
     */
    private void setZanDrawable(boolean bZan, TextView view){
        Drawable drawable;
        if(bZan) {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_praise);
        }else {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_zan_not);
        }
        drawable.setBounds(new Rect(0,0,GenalralUtils.dp2px(mContext,13),GenalralUtils.dp2px(mContext,14)));
        view.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public interface onItemCommonClickListener {

        void onItemClickListener(int position);

        void onItemLongClickListener(int position);

    }
}