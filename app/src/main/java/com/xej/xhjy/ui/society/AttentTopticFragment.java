package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.adapter.TopicFollowListAdapter;
import com.xej.xhjy.ui.society.bean.HottopicBean;
import com.xej.xhjy.ui.view.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dazhi
 * @class AttentPeopleFragment 关注的话题
 * @Createtime 2018/11/28 15:54
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class AttentTopticFragment extends BaseFragment {
    @BindView(R.id.recycle_im_list_toptic)
    CommonRecyclerView recycle_im_list_toptic;
    Unbinder unbinder;
    TopicFollowListAdapter mAdapter;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPages = 0, mAllPages;
    private List<HottopicBean.ContentBean> mList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_attent_toptic, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        mList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_im_list_toptic.setLayoutManager(layoutManager);
        mAdapter = new TopicFollowListAdapter(mActivity,R.layout.item_topic_follow, mList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity,TopicDetailActivity.class);
                intent.putExtra("topicId",mList.get(position).getId());
                intent.putExtra("title","#"+mList.get(position).getTitle());
                intent.putExtra("content",mList.get(position).getContent());
                intent.putExtra("accessoryId",mList.get(position).getAccessoryId());
                mActivity.startActivityWithAnim(intent);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ClubDialog dialog = new ClubDialog(mActivity);
                dialog.setTitle("温馨提示");
                dialog.setMessage("确定取消关注该话题吗？");
                dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                dialog.setPositiveListener("确定", new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        String TAG = "follow_add_or_unfollow_topic";
                        mActivity.addTag(TAG);
                        Map<String, String> maps = new HashMap<>();
                        maps.put("attId", mList.get(position).getId());
                        maps.put("tab","1");
                        String url = NetConstants.POST_UNFOLLOW;
                        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack() {
                            @Override
                            public void onSucess(String jsonString) {
                                mList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String errorMsg) {

                            }
                        });

                    }
                });
                dialog.setNegativeListener("取消", new NegativeListener() {
                    @Override
                    public void onNegativeClick() {

                    }
                });
                dialog.show();



            }
        });
        recycle_im_list_toptic.setEmptyView(meetEmpty);
        recycle_im_list_toptic.setAdapter(mAdapter);
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
        getTopicListData(false);
    }

    /**
     * 获取话题列表
     *
     * @param isLoadMore 是否是加载更多
     *
     */
    private void getTopicListData(final boolean isLoadMore) {
        String TAG = "TopicList";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        //tab 关注的人0  关注的话题1
//        map.put("tab","1");
        map.put("pageNum", mPages + "");
        map.put("pageSize", "15");
        String url = NetConstants.MY_HOT_TOPIC;
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
                                recycle_im_list_toptic.scheduleLayoutAnimation();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
