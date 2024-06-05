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
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.team.model.Team;
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
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.Utils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.society.adapter.CommitteeAdatper;
import com.xej.xhjy.ui.society.bean.CommentConfig;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.contract.CircleContract;
import com.xej.xhjy.ui.society.mvp.presenter.CirclePresenter;
import com.xej.xhjy.ui.society.widgets.CommentListView;
import com.xej.xhjy.ui.society.widgets.MyTextViewNum;
import com.xej.xhjy.ui.society.widgets.UpdateNewMessageEvent;
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
 * @class CommitteeActivity 专委会板块详情，帖子列表
 * @Createtime 2018/11/23 10:21
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class CommitteeActivity extends BaseActivity implements CircleContract.View {
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.img_message_new)
    ImageView img_message_new;
    @BindView(R.id.ll_contact)
    LinearLayout llContact;
    @BindView(R.id.tv_committee_name)
    TextView tvCommitteeName;
    @BindView(R.id.editTextBodyLl)
    LinearLayout edittextbody;
    @BindView(R.id.ll_new_message_num)
    LinearLayout ll_new_message_num;
    @BindView(R.id.ll_im)
    LinearLayout llIm;
    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.img_message)
    ImageView img_message;
    @BindView(R.id.tv_new_mess)
    TextView tv_new_mess;
    @BindView(R.id.circleEt)
    EditText editText;//编辑评论
    @BindView(R.id.sendIv)
    ImageView sendIv; //发送评论
    @BindView(R.id.recycle_committee_list)
    CommonRecyclerView recycleCommitteeList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.committee_empty)
    View committee_empty;
    @BindView(R.id.tv_message_num)
    MyTextViewNum tv_message_num;
    CommitteeAdatper mAdapter;
    LinearLayoutManager layoutManager;
    RelativeLayout bodyLayout;
    private ClubLoadingDialog mLoadingDialog;
    private int page = 0;
    private int allPage = 1;
    private List<PostListBean.ContentBean> mList;
    private CirclePresenter presenter;
    private CommentConfig commentConfig;
    private String mOrgId;
    public static final int RESULT_BACK = 224110;
    private boolean isFollows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        //编辑框输入时软键盘会遮挡输入框，处理办法StatusBarUtil设置颜色来解决
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        refreshLayout.autoRefresh();
    }

    /**
     * 初始化View
     */

    private void initView() {
        mLoadingDialog = new ClubLoadingDialog(this);
        presenter = new CirclePresenter(this, mLoadingDialog);
        String title = getIntent().getStringExtra("title");
        mOrgId = getIntent().getStringExtra("orgId");
        titleview.setTitle(title);
        titleview.setMessageVisibile(true);
        img_edit.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        mAdapter = new CommitteeAdatper(this, mList, new CommitteeAdatper.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //如果id相同则删除，如果不同则关注过删除
                if (PerferenceUtils.get(AppConstants.User.IM_POST_ID, "").equals(mList.get(position).getPosterId())) {
                    ClubDialog dialog = new ClubDialog(CommitteeActivity.this);
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
                        ClubDialog dialog = new ClubDialog(CommitteeActivity.this);
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
        recycleCommitteeList.setEmptyView(committee_empty);
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                postData(false);
                getMessageCount();
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
                postData(true);
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleCommitteeList.setLayoutManager(layoutManager);
        mAdapter.setCirclePresenter(presenter);
        recycleCommitteeList.setAdapter(mAdapter);
        recycleCommitteeList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });
        recycleCommitteeList.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        Toast.makeText(CommitteeActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
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
    private void postData(final boolean isMore) {
        String TAG = "committee_post_list";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("commitId", mOrgId);
        maps.put("pageNum", Integer.toString(page));
        maps.put("pageSize", "10");
        String url = NetConstants.POST_LIST;
//        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
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
                                recycleCommitteeList.scheduleLayoutAnimation();
                            }
                        } else {
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
                if (isMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        });


    }


    /**
     * 获取未读消息（评论或点赞消息）
     */
    private void getMessageCount() {
        String TAG = "post_list_count";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("commitId", mOrgId);
        String url = NetConstants.QUERY_NOREAD_MESSAGE;
        LogUtils.dazhiLog("未读消息maps---->" + maps);
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("未读消息---->" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if ("0".equals(code)) {
                        String num = jsonObject.optString("content");

                        if (!"0".equals(num)) {
                            ll_new_message_num.setVisibility(View.VISIBLE);
                            tv_new_mess.setText(num + "条新消息");
                        } else {
                            ll_new_message_num.setVisibility(View.GONE);
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
        LogUtils.dazhiLog("关注" + maps);
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

    /**
     * 跳转通讯录
     */
    @OnClick(R.id.ll_contact)
    void goConstantList() {
        Intent intent = new Intent(mActivity, ContactListActivity.class);
        intent.putExtra("orgId", mOrgId);
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                "eventBtnContact", "orgId="+mOrgId);
        mActivity.startActivityWithAnim(intent);
    }

    /**
     * 跳转聊天
     */
    @OnClick(R.id.ll_im)
    void goIM() {
        String url = null;
        String TAG = "query_Id_groupId";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("id", mOrgId);
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                "eventBtnChats", "orgId="+mOrgId);
        url = NetConstants.QUERY_ORGID_FOR_GROUP_ID;
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if ("0".equals(code)) {
                        String tid = jsonObject.getJSONObject("content").optString("tid");
                        if (!TextUtils.isEmpty(tid)) {
                            //查询自己在这个群组才可以进入
                            Team t = NimUIKit.getTeamProvider().getTeamById(tid);
                            if (t != null) {
                                if (t.isMyTeam()) {
                                    NimUIKit.startTeamSession(CommitteeActivity.this, tid);
                                } else {
                                    queryGroup();
                                }
                            } else {
                                queryGroup();
                            }
                        } else {
                            ToastUtils.shortToast(CommitteeActivity.this, "没有查到群组ID");
                        }
                    } else {
                        ToastUtils.shortToast(CommitteeActivity.this, "查群群组ID失败");
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
     * 查询群组
     */

    private void queryGroup() {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(CommitteeActivity.this);
        dialog.setMessage("你不属于该群组,无法进入群聊");
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {

            }
        });
        dialog.show();
    }

    /**
     * 跳转消息
     */
    @OnClick(R.id.img_message)
    void goPost() {
        Intent intent = new Intent(mActivity, MessageActivity.class);
        mActivity.startActivityWithAnim(intent);

    }

    /**
     * 去发帖子
     */
    @OnClick(R.id.img_edit)
    void goPostEdit() {
        Intent intent = new Intent(CommitteeActivity.this, PostEditActivity.class);
        intent.putExtra("orgId", mOrgId);
        intent.putExtra("postFlag", false);
//        intent.putExtra("topicName","#"+getIntent().getStringExtra("title"));
        CommitteeActivity.this.startActivityForResultWithAnim(intent, 19780);
    }


    /**
     * 去查看新消息
     */
    @OnClick(R.id.ll_new_message_num)
    void goNewsMessage() {
        Intent intent = new Intent(CommitteeActivity.this, NewNoteMessageActivity.class);
        intent.putExtra("orgId", mOrgId);
        mActivity.startActivityWithAnim(intent);

    }


    @Override
    public void update2AddFavorite(int circlePosition, PostListBean.ContentBean.SocialLikeBean bean) {
        if (bean != null && mList != null) {
            mList.get(circlePosition).getSocialLike().add(bean);
            //增加赞为true
            mList.get(circlePosition).setWhetherLike(true);
            LogUtils.dazhiLog("SocialLike长度---->" + mList.get(circlePosition).getSocialLike().size());
            mAdapter.notifyItemChanged(circlePosition);
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
        titleview.setNewMessageVisibile(event.getHasMessage());
        if (event.getHasMessage() == false) {
            img_message_new.setVisibility(View.GONE);
        }
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

    /**
     * 收到已查看消息更新帖子消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateNewMessage(UpdateNewMessageEvent event) {
        /**
         *  返回来时候刷新最新消息
         */
        getMessageCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_BACK && requestCode == 19780) {
            /*** 发帖子回调回来刷新页面 */
            if (mList != null && mList.size() > 0) {
                mList.clear();
            }
            refreshLayout.autoRefresh();
        }

    }


}
