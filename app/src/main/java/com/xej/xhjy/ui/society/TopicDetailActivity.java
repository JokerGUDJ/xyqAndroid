package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.Utils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.society.adapter.TopicDetailAdatper;
import com.xej.xhjy.ui.society.bean.CommentConfig;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.contract.CircleContract;
import com.xej.xhjy.ui.society.mvp.presenter.CirclePresenter;
import com.xej.xhjy.ui.society.widgets.CommentListView;
import com.xej.xhjy.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class TopicDetailActivity
 * @Createtime 2018/11/23 10:21
 * @description 话题详情页
 * @Revisetime
 * @Modifier
 */
public class TopicDetailActivity extends BaseActivity implements CircleContract.View {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.topic_icon)
    ImageView topicIcon;
    @BindView(R.id.tv_topic_name)
    TextView tvTopicName;
    @BindView(R.id.tv_topic_content)
    TextView tvTopicContent;
    @BindView(R.id.recycle_topic_list)
    CommonRecyclerView recycleTopicList;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.img_message)
    ImageView img_message;
    @BindView(R.id.iv_follow)
    ImageView iv_follow;
    @BindView(R.id.circleEt)
    EditText editText;//编辑评论
    @BindView(R.id.sendIv)
    ImageView sendIv; //发送评论
    @BindView(R.id.editTextBodyLl)
    LinearLayout edittextbody;
    LinearLayoutManager layoutManager;
    private CirclePresenter presenter;
    TopicDetailAdatper mAdapter;
    private List<PostListBean.ContentBean> mList;
    private ClubLoadingDialog mLoadingDialog;
    private String topicId, topicName;
    private int page = 0;
    private int allPage = 0;
    private boolean isFollowTopic;//关注话题
    private boolean isFollows;//关注人
    private CommentConfig commentConfig;
    RelativeLayout bodyLayout;
    private static final int REQUEST_OK = 1890;
    public static final int RESULT_OK = 189055;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        ButterKnife.bind(this);
        initView();
        initData();
        queryFollow();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        refreshLayout.autoRefresh();
    }

    private void initView() {
        titleview.setMessageVisibile(true);
        img_edit.setVisibility(View.VISIBLE);
        mLoadingDialog = new ClubLoadingDialog(this);
        presenter = new CirclePresenter(this, mLoadingDialog);
        img_message.setVisibility(View.VISIBLE);
        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String accessoryId = getIntent().getStringExtra("accessoryId");
        String url = NetConstants.POST_IMAGE + "/" + accessoryId;
        LogUtils.dazhiLog("话题head=" + url);
        ImageLoadUtils.loadImageNoAnim(mActivity, url, topicIcon);
        if (!TextUtils.isEmpty(topicName)) {
            tvTopicName.setText(topicName);
        } else {
            tvTopicName.setText("");
        }
        try {
            String str = URLDecoder.decode(content, "utf-8");
            tvTopicContent.setText(str);
        } catch (UnsupportedEncodingException e) {
            tvTopicContent.setText("");

        }

        mList = new ArrayList<>();
        mAdapter = new TopicDetailAdatper(this, mList, new TopicDetailAdatper.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //如果id相同则删除，如果不同则关注过删除
                if (PerferenceUtils.get(AppConstants.User.IM_POST_ID, "").equals(mList.get(position).getPosterId())) {
                    ClubDialog dialog = new ClubDialog(TopicDetailActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("确定删除吗？");
                    dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                    dialog.setPositiveListener("删除", new PositiveListener() {
                        @Override
                        public void onPositiveClick() {
                            deleteMyPost(position);
                        }
                    });
                    dialog.setNegativeListener("取消", new NegativeListener() {
                        @Override
                        public void onNegativeClick() {

                        }
                    });
                    dialog.show();
                } else {
                    String flag = mList.get(position).getWhetherAttention();
                    if (!TextUtils.isEmpty(flag)) {
                        isFollows = flag.equals("1") ? true : false;
                    } else {
                        isFollows = false;
                    }
                    if (isFollows) {
                        ClubDialog dialog = new ClubDialog(TopicDetailActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("确定取消关注吗？");
                        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                        dialog.setPositiveListener("确定", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                followPost(position);
                            }
                        });
                        dialog.setNegativeListener("取消", new NegativeListener() {
                            @Override
                            public void onNegativeClick() {

                            }
                        });
                        dialog.show();
                    } else {
                        followPost(position);
                    }
                }
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleTopicList.setLayoutManager(layoutManager);
        recycleTopicList.setEmptyView(emptyView);
        recycleTopicList.setAdapter(mAdapter);
        mAdapter.setCirclePresenter(presenter);

        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                postTopicData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                if (page == allPage) {
                    refreshLayout.setNoMoreData(true);
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                postTopicData(true);
            }
        });
        recycleTopicList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });
        recycleTopicList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    String content ="";
                    try {
                        content = URLDecoder.decode(editText.getText().toString().trim(), "utf-8");
                    } catch (UnsupportedEncodingException e) {

                    }
                    String str = Utils.StringtoUtf8(content);
                    if (TextUtils.isEmpty(str)) {
                        Toast.makeText(TopicDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.dazhiLog("评论后的内容---------" + str);
                    presenter.addComment(mActivity, str, commentConfig);
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        setViewTreeObserver();
    }


    /**
     * 初始化数据
     */
    private void postTopicData(final boolean isMore) {
        String TAG = "post_topic_list";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("topicId", topicId);
        maps.put("pageNum", Integer.toString(page));
        maps.put("pageSize", "10");
        String url = NetConstants.HOT_TOPIC_DETAILS_CONTENT;
        LogUtils.dazhiLog("话题------>" + maps);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                PostListBean bean = JsonUtils.stringToObject(jsonString, PostListBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<PostListBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            allPage = bean.getPage().getTotalPages();
                            if (!isMore) {
                                recycleTopicList.scheduleLayoutAnimation();
                            }
                        } else {//无数据的情况
                            if (!isMore) {
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        if (page == allPage) {
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
                    }

                }
                if (isMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }

            }

            @Override
            public void onError(String errorMsg) {
            }
        });


    }

    /**
     * 关注查询
     */

    private void queryFollow() {
        String url = null;
        String TAG = "query_detail_follow";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("Id", topicId);
        url = NetConstants.QUERY_HOT_TOPIC_FOLLOW;
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                JSONObject js = null;
                try {
                    js = new JSONObject(jsonString);
                    if ("0".equals(js.optString("code"))) {
                        String whetherAtt = js.getJSONObject("content").optString("whetherAtt");
                        if (!TextUtils.isEmpty(whetherAtt)) {
                            if ("0".equals(whetherAtt)) {
                                iv_follow.setImageResource(R.drawable.ic_love_full);
                                isFollowTopic = true;
                            } else if ("1".equals(whetherAtt)) {
                                iv_follow.setImageResource(R.drawable.ic_love_empty);
                                isFollowTopic = false;
                            } else {
                                iv_follow.setImageResource(R.drawable.ic_love_empty);
                                isFollowTopic = false;
                            }
                        } else {
                            iv_follow.setImageResource(R.drawable.ic_love_empty);
                            isFollowTopic = false;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {

            }
        });


    }

    /**
     * 关注和取消关注话题
     */

    void getFollow() {
        String url = null;
        String TAG = "post_add_or_delete";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("attId", topicId);
        maps.put("tab", "1");
        if (isFollowTopic) {
            url = NetConstants.POST_UNFOLLOW;
        } else {
            url = NetConstants.POST_FOLLOW;
        }
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                JSONObject js = null;
                try {
                    js = new JSONObject(jsonString);
                    if ("0".equals(js.optString("code"))) {
                        if (isFollowTopic) {
                            isFollowTopic = false;
                            iv_follow.setImageResource(R.drawable.ic_love_empty);
                        } else {
                            isFollowTopic = true;
                            iv_follow.setImageResource(R.drawable.ic_love_full);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });


    }

    /**
     * 删除自己的发帖
     *
     * @param pos
     */
    private void deleteMyPost(int pos) {
        String TAG = "post_item_delate";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("id", mList.get(pos).getId());
        String url = NetConstants.DELATE_MY_POST;
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mList.remove(pos);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    /**
     * 关注或取消关注别人
     *
     * @param pos tab 0关注人  1关注话题
     */
    private void followPost(int pos) {
        String url = null;
        String TAG = "post_add_or_delete";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("attId", mList.get(pos).getPosterId());
        maps.put("tab", "0");
        if (isFollows) {
            url = NetConstants.POST_UNFOLLOW;
        } else {
            url = NetConstants.POST_FOLLOW;
        }
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                if (isFollows) {
                    mList.get(pos).setWhetherAttention("0");
                    isFollows = false;
                } else {
                    mList.get(pos).setWhetherAttention("1");
                    isFollows = true;
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void update2AddFavorite(int circlePosition, PostListBean.ContentBean.SocialLikeBean bean) {
        if (bean != null) {
            mList.get(circlePosition).getSocialLike().add(bean);
            mList.get(circlePosition).setWhetherLike(true);
            LogUtils.dazhiLog("SocialLike长度---->" + mList.get(circlePosition).getSocialLike().size());
            mAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        List<PostListBean.ContentBean.SocialLikeBean> socialLike = mList.get(circlePosition).getSocialLike();
        for (int i = 0; i < socialLike.size(); i++) {
            if (favortId.equals(socialLike.get(i).getId())) {
                socialLike.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, List<PostListBean.ContentBean.ReplyListBean> addItem) {
        if (addItem != null) {
            mList.get(circlePosition).setReplyList(addItem);
            mList.get(circlePosition).setWhetherReply(true);
            mAdapter.notifyDataSetChanged();
        }
        //清空评论文本
        editText.setText("");

    }


    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        List<PostListBean.ContentBean.ReplyListBean> replyList = mList.get(circlePosition).getReplyList();
        for (int i = 0; i < replyList.size(); i++) {
            if (commentId.equals(replyList.get(i).getId())) {
                replyList.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            KeybordUtils.openKeybord(editText, editText.getContext());

        } else if (View.GONE == visibility) {
            //隐藏键盘
            KeybordUtils.closeKeybord(editText, editText.getContext());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        LogUtils.dazhiLog("meet收到是否有消息----------" + event.getHasMessage());

    }

    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = GenalralUtils.getStatusBarHeight(mActivity);//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition, getListviewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - GenalralUtils.px2dp(mActivity, 200);
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        return listviewOffset;
    }


    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }


    @OnClick(R.id.iv_follow)
    void getFollowTopic() {
        if (isFollowTopic) {
            ClubDialog dialog = new ClubDialog(TopicDetailActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("确定取消关注吗？");
            dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
            dialog.setPositiveListener("确定", new PositiveListener() {
                @Override
                public void onPositiveClick() {
                    getFollow();
                }
            });
            dialog.setNegativeListener("取消", new NegativeListener() {
                @Override
                public void onNegativeClick() {

                }
            });
            dialog.show();
        } else {
            getFollow();
        }

    }

    /**
     * 去发帖子,带入orgId
     */
    @OnClick(R.id.img_edit)
    void goPostEdit() {
        Intent intent = new Intent(TopicDetailActivity.this, PostEditActivity.class);
        intent.putExtra("orgId", getIntent().getStringExtra("orgId"));
        //说明：从帖子详情发帖区分为1
        intent.putExtra("postFlag", true);
        intent.putExtra("topicId", topicId);
        intent.putExtra("topicName", topicName);
        TopicDetailActivity.this.startActivityForResultWithAnim(intent, REQUEST_OK);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.recycle();
        }
        //将遮罩层置空
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_OK) {
            /*** 发帖子回调回来刷新页面 */
            if (mList != null && mList.size() > 0) {
                mList.clear();
            }
            refreshLayout.autoRefresh();
        }

    }


}
