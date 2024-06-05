package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.adapter.NewsMessageListAdapter;
import com.xej.xhjy.ui.society.bean.PlatformMessageBean;
import com.xej.xhjy.ui.view.EmptyView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dazhi
 * @class NewsMessageFragment 平台消息页面
 * @Createtime 2018/11/28 15:54
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class NewsMessageFragment extends BaseFragment {
    @BindView(R.id.recycle_news_list)
    CommonRecyclerView recycleNewsList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.news_empty)
    EmptyView news_empty;
    Unbinder unbinder;
    NewsMessageListAdapter mAdapter;
    private int mPages = 0, mAllPages;
    private List<PlatformMessageBean.ContentBean> mList;
    private ClubLoadingDialog mLoadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news_list, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        initData();
        return rootView;
    }

    private void initData() {
        mRefreshLayout.autoRefresh();
    }

    private void init() {
        mLoadingDialog = new ClubLoadingDialog(getActivity());
        mList = new ArrayList<>();
        //刷新数据
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getSystemMessage(false);
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getSystemMessage(true);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleNewsList.setLayoutManager(layoutManager);
        mAdapter = new NewsMessageListAdapter(mActivity, mList, new NewsMessageListAdapter.onItemNewsMessageClickListener() {
            @Override
            public void onItemClickListener(int position) {
                String mark = mList.get(position).getMark();
                if (!TextUtils.isEmpty(mark)) {
                    switch (Integer.parseInt(mark)) {
                        case 0:
                            isReadNewMessage(position, true);
                            break;
                        case 1:
                            isReadNewMessage(position, false);
                            ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                            dialog.setMessage("请在帖子列表中查看！");
                            dialog.setPositiveListener("确定", new PositiveListener() {
                                @Override
                                public void onPositiveClick() {

                                }
                            });
                            dialog.show();
                            break;
                    }

                }


            }
        });
        recycleNewsList.setEmptyView(news_empty);
        recycleNewsList.setAdapter(mAdapter);
    }

    private void isReadNewMessage(int pos, boolean flag) {
        String TAG = "read_system_message";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("id", mList.get(pos).getId());
        String url = NetConstants.POST__NEW_MESSAGE_READ;
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                if (flag) {

                    JSONObject js = null;
                    try {
                        js = new JSONObject(jsonString);
                        if ("0".equals(js.optString("code"))) {
                            String noticeType = mList.get(pos).getNoticeType();
                            if (TextUtils.isEmpty(noticeType)) {
                                noticeType = "0";
                            }
                            Intent intent = new Intent(mActivity, NewsMessageDetailsActivity.class);
                            intent.putExtra("title", mList.get(pos).getTitle());
                            intent.putExtra("content", mList.get(pos).getPushMessage());
                            intent.putExtra("time", mList.get(pos).getCreatTime());
                            intent.putExtra("noticeType", noticeType);
                            intent.putExtra("meetId", mList.get(pos).getMeetId());
                            intent.putExtra("placeName", mList.get(pos).getPlaceName());
                            intent.putExtra("id",mList.get(pos).getId());
                            mActivity.startActivityWithAnim(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
            }
        });
    }

    /**
     * 获得系统消息
     */
    private void getSystemMessage(final boolean isLoadMore) {
        String TAG = "query_system_message";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("pageSize", "10");
        maps.put("pageNum", mPages + "");
        String urlNew = NetConstants.SYSTEM_NEW_MESSAGE;
        RxHttpClient.doPostStringWithUrl(mActivity, urlNew, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                PlatformMessageBean bean = JsonUtils.stringToObject(jsonString, PlatformMessageBean.class);
                if (bean != null) {
                    if ("0".equals(bean.getCode())) {
                        List<PlatformMessageBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore&& recycleNewsList!=null) {
                                recycleNewsList.scheduleLayoutAnimation();
                            }
                        } else {  //无数据的情况
                            if (!isLoadMore) {
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        if (mPages == mAllPages) {
                            mRefreshLayout.setNoMoreData(true);
                        } else {
                            mRefreshLayout.setNoMoreData(false);
                        }


                    }
                }
                if (isLoadMore) {
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishRefresh();
                }

            }

            @Override
            public void onError(String errorMsg) {
                if (isLoadMore) {
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishRefresh();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
