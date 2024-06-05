package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

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
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.society.adapter.TopicListAdapter;
import com.xej.xhjy.ui.society.bean.HottopicBean;
import com.xej.xhjy.ui.view.EmptyView;
import com.xej.xhjy.ui.view.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dazhi
 * @class TopicListActivity 所有话题列表页，也可作为话题选择页
 * @Createtime 2018/11/21 10:15
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class TopicListActivity extends BaseActivity {
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.recycle_topic_list)
    CommonRecyclerView recycleTopicList;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPages = 0, mAllPages;
    private ClubLoadingDialog mDialog;
    private TopicListAdapter mAdapter;
    private List<HottopicBean.ContentBean> mList = new ArrayList<>();
    public static final int RESULTOK = 12001;
    private String orgId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        orgId = getIntent().getStringExtra(PostEditActivity.ORGID);
        mDialog = new ClubLoadingDialog(this);
        meetEmpty.setText("暂无话题");
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getTopicListData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getTopicListData(true);
            }
        });
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleTopicList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, R.anim.layout_animation_fall_down);
        recycleTopicList.setLayoutAnimation(animation);
        mAdapter = new TopicListAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (getIntent().getBooleanExtra(PostEditActivity.TIPICTYPE, false)) {
                    Intent intent = new Intent();
                    intent.putExtra("topicId", mList.get(position).getId());
                    intent.putExtra("topicName", mList.get(position).getTitle());
                    setResult(RESULTOK, intent);
                    finish();
                } else {
                    LoginCheckUtils.isCertification=true;
                    LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                        @Override
                        public void onlogin() {
                            Intent intent = new Intent(mActivity, TopicDetailActivity.class);
                            intent.putExtra("topicId", mList.get(position).getId());
                            intent.putExtra("title", "#"+mList.get(position).getTitle());
                            intent.putExtra("content", mList.get(position).getContent());
                            intent.putExtra("accessoryId",mList.get(position).getAccessoryId());
                            mActivity.startActivityWithAnim(intent);
                        }
                    }, mActivity);

                }
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        recycleTopicList.setEmptyView(meetEmpty);
        recycleTopicList.setAdapter(mAdapter);
        getTopicListData(false);
    }


    /**
     * 获取话题列表
     *
     * @param isLoadMore 是否是加载更多
     */
    private void getTopicListData(final boolean isLoadMore) {
        String TAG = "TopicList";
        mActivity.addTag(TAG);
        boolean topicType = getIntent().getBooleanExtra(PostEditActivity.TIPICTYPE,false);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", mPages + "");
        map.put("pageSize", "15");
        //如果是话题详情过来的发帖，则需要传commitId,否则不传
        if (topicType){
            map.put("commitId",orgId);
        }
        String url = NetConstants.HOT_TOPIC;
        LogUtils.dazhiLog("话题----"+map);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                HottopicBean bean = JsonUtils.stringToObject(jsonString, HottopicBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<HottopicBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                recycleTopicList.scheduleLayoutAnimation();
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
    public void onDestroy() {
        LoginCheckUtils.clear();
        super.onDestroy();
    }

}
