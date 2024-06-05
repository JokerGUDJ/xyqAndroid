package com.xej.xhjy.ui.society;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.society.adapter.NoteMessageListAdapter;
import com.xej.xhjy.ui.society.bean.PostNewMessageBean;
import com.xej.xhjy.ui.society.widgets.UpdateNewMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class NewNoteMessageActivity 帖子新消息列表
 * @Createtime 2018/11/21 10:15
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class NewNoteMessageActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycle_message_list)
    CommonRecyclerView recycleMessageList;
    @BindView(R.id.head_back)
    ImageView head_back;
    @BindView(R.id.meet_empty)
    View emptyView;
    private int mPages = 0, mAllPages;
    private NoteMessageListAdapter mAdapter;
    private List<PostNewMessageBean.ContentBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_message_list);
        ButterKnife.bind(this);
        String orgId = getIntent().getStringExtra("orgId");
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getTopicListData(orgId, false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getTopicListData(orgId, true);
            }
        });
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleMessageList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, R.anim.layout_animation_fall_down);
        recycleMessageList.setLayoutAnimation(animation);
        mAdapter = new NoteMessageListAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        recycleMessageList.setEmptyView(emptyView);
        recycleMessageList.setAdapter(mAdapter);
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }


    @OnClick(R.id.head_back)
    void headBack() {
        EventBus.getDefault().post(new UpdateNewMessageEvent("更新帖子消息"));
        finish();
    }


    /**
     * 获取话题列表
     *
     * @param isLoadMore 是否是加载更多
     */
    private void getTopicListData(String orgId, final boolean isLoadMore) {
        String TAG = "TopicList";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", mPages + "");
//        map.put("pageSize", "10");
        map.put("commitId", orgId);
        String url = NetConstants.QUERY_NOREADNOTICE;
        LogUtils.dazhiLog("帖子消息map----" + map);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("帖子消息----" + jsonString);
                PostNewMessageBean bean = JsonUtils.stringToObject(jsonString, PostNewMessageBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<PostNewMessageBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
//                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                recycleMessageList.scheduleLayoutAnimation();
                            }
                        } else {//无数据的情况
                            if (!isLoadMore) {
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        if (mPages == mAllPages) {
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
                    }

                }
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new UpdateNewMessageEvent("更新帖子消息"));
        finishWithAnim();
    }


}
