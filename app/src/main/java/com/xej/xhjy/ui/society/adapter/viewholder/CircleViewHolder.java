package com.xej.xhjy.ui.society.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.xej.xhjy.R;
import com.xej.xhjy.ui.society.widgets.CommentListView;
import com.xej.xhjy.ui.society.widgets.ExpandTextView;
import com.xej.xhjy.ui.society.widgets.PraiseListView;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.adapter
 * @ClassName: CircleViewHolder
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class CircleViewHolder extends RecyclerView.ViewHolder {
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_HEADER = 3;
    public final static int TYPE_SEARCH = 4; //后增加为搜索


    public int viewType;

    public HeadImageView headIv;
    public ImageView img_love, img_more, img_topic;
    public TextView nameTv, tv_address, tv_toptic, tv_complany,tv_top_setting;
    /**
     * 动态的内容
     */
    public ExpandTextView contentTv;
    public TextView timeTv;
    public TextView tv_delete;
    public TextView ll_zan, ll_comments;

    /**
     * 点赞列表
     */
    public PraiseListView praiseListView;

    public LinearLayout digCommentBody;
    public View digLine;
    public LinearLayout head_search_item;
    public Button mButton;

    /**
     * 评论列表
     */
    public CommentListView commentList;

    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);
        mButton=(Button)itemView.findViewById(R.id.contact_input_content);
        head_search_item=(LinearLayout)itemView.findViewById(R.id.head_search_item);
        headIv = (HeadImageView) itemView.findViewById(R.id.img_usericon);
        nameTv = (TextView) itemView.findViewById(R.id.tv_item_name);
        tv_top_setting=(TextView)itemView.findViewById(R.id.tv_item_top);
        digLine = itemView.findViewById(R.id.lin_dig);
        tv_address = itemView.findViewById(R.id.tv_address);
        contentTv = (ExpandTextView) itemView.findViewById(R.id.tv_content);
        timeTv = (TextView) itemView.findViewById(R.id.tv_time);
        tv_toptic = itemView.findViewById(R.id.tv_toptic);
        tv_complany = itemView.findViewById(R.id.tv_complany);
        ll_comments = (TextView) itemView.findViewById(R.id.tv_comments);//评论
        ll_zan = (TextView) itemView.findViewById(R.id.tv_zan);//赞
        praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);
        img_love = itemView.findViewById(R.id.img_love);
        digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);
        commentList = (CommentListView) itemView.findViewById(R.id.commentList);
        tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        img_more = (ImageView)itemView.findViewById(R.id.img_more);
        img_topic = (ImageView)itemView.findViewById(R.id.img_topic);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);


}
